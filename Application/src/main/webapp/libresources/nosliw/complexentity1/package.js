//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithType;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_BUNDLE_NAME = "bundle";	

//package is runtime unit.
//
var node_createPackageCore = function(parm, configure){

	var loc_resourceId;
	
	var loc_bundleInfo;
	
	var loc_configure;
	var loc_configureValue;
	
	var loc_runtimeEnv;

	var loc_envInterface;
	
	var loc_parentView;
	
	var loc_packageDef;
	
	var loc_init = function(parm, configure){
		loc_configure = configure;
		loc_configureValue = node_createConfigure(configure).getConfigureValue();

		if(parm.bundleDef!=undefined){
			//parm is bundle entity
			loc_bundleInfo = parm;
		}
		else{
			//parm is resource id
			loc_resourceId = parm;
		}
	};
	
	var loc_getMainBundleRuntime = function(){
		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_BUNDLE_NAME).getChildValue();
	};

	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCorePackage", {}), handlers, request);

		if(loc_resourceId!=undefined){
			//load resource first
			var gatewayParm = {};
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYPACKAGE_COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID] = loc_resourceId;
			out.addRequest(nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(
					node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_PACKAGE, 
					node_COMMONATRIBUTECONSTANT.GATEWAYPACKAGE_COMMAND_LOADEXECUTABLEPACKAGE, 
					gatewayParm,
					{
						success : function(requestInfo, packageDef){
							loc_packageDef = packageDef;
							var bundleRuntimeRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("createBundleRuntime"));
							
							//load all related resources first
							bundleRuntimeRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(packageDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPACKAGE_DEPENDENCY], {
								success : function(requestInfo, resourceTree){
									var mainBundleRuntime = nosliw.runtime.getComplexEntityService().createBundleRuntime(loc_packageDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPACKAGE_MAINENTITYREF], loc_configure, request);
									loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_BUNDLE_NAME, mainBundleRuntime, true);
								}
							}));
							
							return bundleRuntimeRequest;
						}
					}
			));
		}
		else{
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				var mainBundleRuntime = nosliw.runtime.getComplexEntityService().createBundleRuntime(loc_bundleInfo, loc_configure, request);
				loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_BUNDLE_NAME, mainBundleRuntime, true);
			}));
		}

		return out;
	};

	var loc_out = {

		getDataType: function(){    return  "package";   },

		getMainBundleRuntime : function(){   return loc_getMainBundleRuntime();     },

		getMainEntityRuntime : function(){ return this.getMainBundleRuntime().getCoreEntity().getMainEntity();  },

		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},
			
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
			}
			else{
				if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
//					out.addRequest(loc_mainBundleRuntime.getLifeCycleRequest(transitName));
				}
				else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				}
			}
			return out;
		},
		
		updateView : function(view){    
			loc_parentView = view;
			loc_getMainBundleRuntime().updateView(view);     
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE);
	
	loc_init(parm, configure);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageCore", node_createPackageCore); 

})(packageObj);
