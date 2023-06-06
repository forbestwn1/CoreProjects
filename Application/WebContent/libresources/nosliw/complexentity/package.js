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
var node_createPackageCore = function(resourceId, configure){

	var loc_resourceId = resourceId;
	
	var loc_configure = configure;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	
	var loc_runtimeEnv;

	var loc_envInterface;
	
	var loc_parentView;
	
	var loc_packageDef;
	
	var loc_debugMode;
	var loc_debugView;

	var loc_init = function(){
		var debugConf = loc_configureValue[node_basicUtility.buildNosliwFullName("debug_package")];
		if("true"==debugConf){
			//debug mode
			loc_debugMode = true;
		}
	};
	
	var loc_getDebugView = function(){
		if(loc_debugView==undefined){
			loc_debugView = node_createPackageDebugView("Package: "+loc_out.getDataType()+"_"+loc_out.getId(), "blue");
		}
		return loc_debugView;
	};
	
	var loc_isDebugMode = function(){
		return loc_debugMode == true;
	};

	var loc_createMainBundleRuntime = function(request){
		var mainBundleRuntime = nosliw.runtime.getComplexEntityService().createBundleRuntime(loc_packageDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEPACKAGE_MAINENTITYID], loc_configure, request);
		loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_BUNDLE_NAME, mainBundleRuntime, true);
		return mainBundleRuntime;
	};
	
	var loc_getMainBundleRuntime = function(){
		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_BUNDLE_NAME).getChildValue();
	};

	
	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCorePackage", {}), handlers, request);
		//load resource first
		var gatewayParm = {};
		gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYPACKAGE_COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID] = resourceId;
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
								var mainBundleRuntime = loc_createMainBundleRuntime(requestInfo);
								return mainBundleRuntime.getPreInitRequest();
							}
						}));
						
						return bundleRuntimeRequest;
					}
				}
		));
		return out;
	};

	var loc_out = {

		getDataType: function(){    return  "package";   },

		getMainBundleRuntime : function(){   return loc_getMainBundleRuntime();     },

		setEnvironmentInterface : function(envInterface){		loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},
			
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextCorePackage", {}), handlers, request);
			
			loc_parentView = runtimeContext.view;

			var runtimeContextForBundle = node_componentUtility.makeChildRuntimeContext(runtimeContext, loc_BUNDLE_NAME); 
			
			if(loc_isDebugMode()){
				runtimeContextForBundle = loc_getDebugView().updateRuntimeContext(runtimeContextForBundle);
			}
			
			out.addRequest(loc_getMainBundleRuntime().getUpdateRuntimeContextRequest(runtimeContextForBundle));
			return out;
		},

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
		
		getPostInitRequest : function(handlers, request){	return loc_getMainBundleRuntime().getPostInitRequest(handlers, request);	},
		
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE);
	
	loc_init();
	
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
