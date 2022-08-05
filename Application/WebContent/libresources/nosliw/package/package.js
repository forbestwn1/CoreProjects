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
	var nod_createVariableDomain;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createPackageCore = function(resourceId, configure){

	var loc_id = nosliw.generateId();
	
	var loc_resourceId = resourceId;
	
	var loc_configue = configure;
	
	var loc_runtimeContext;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_packageDef;
	
	var loc_mainBundleRuntime;

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
						bundleRuntimeRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(packageDef[node_COMMONATRIBUTECONSTANT.PACKAGEEXECUTABLE_DEPENDENCY], {
							success : function(requestInfo, resourceTree){
								loc_mainBundleRuntime = nosliw.runtime.getPackageService().createBundleRuntime(loc_packageDef[node_COMMONATRIBUTECONSTANT.PACKAGEEXECUTABLE_MAINENTITYID], loc_configue, requestInfo);
								return loc_mainBundleRuntime.getPreInitRequest();
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
		getId : function(){  return loc_id;   },

		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},
			
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextCorePackage", {}), handlers, request);
			loc_runtimeContext = runtimeContext;
			loc_parentView = runtimeContext.view;
			out.addRequest(loc_mainBundleRuntime.getUpdateRuntimeContextRequest(loc_runtimeContext));
			return out;
		},

		
		
		
		
		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){
			loc_runtimeEnv = runtimeEnv;
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
			}
			else{
				if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
					out.addRequest(loc_mainBundleRuntime.getLifeCycleRequest(transitName));
				}
				else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				}
			}
			return out;
		},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE);
	loc_out.id = loc_id;
	
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
nosliw.registerSetNodeDataEvent("package.createVariableDomain", function(){nod_createVariableDomain = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageCore", node_createPackageCore); 

})(packageObj);