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

	var loc_resourceId = resourceId;
	
	var loc_configue = configure;
	
	var loc_runtimeContext;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_packageDef;
	
	var loc_mainBundle;

	var loc_getPreInitPackageRequest = function(handlers, request){
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
								var kkkk = 5555;
								kkkk++;
							}
						}));
						
						return bundleRuntimeRequest;
					}
				}
		));
		return out;
	};
	
	var loc_getInitPackageRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitCorePackage", {}), handlers, request);
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
								var kkkk = 5555;
								kkkk++;
							}
						}));
						
						bundleRuntimeRequest.addRequest(nosliw.runtime.getPackageService().getCreateBundleRuntimeRequest(packageDef[node_COMMONATRIBUTECONSTANT.PACKAGEEXECUTABLE_MAINENTITYID], configure, loc_runtimeContext, {
							success : function(request, bundleRuntime){
								loc_mainBundle = bundleRuntime;
							}
						}));
						return bundleRuntimeRequest;
					}
				}
		));
		return out;
	};
	
	var loc_out = {

		getPreInitRequest : function(handlers, request){   return loc_getPreInitPackageRequest(handlers, request);	},
			
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			loc_runtimeContext = runtimeContext;
			loc_parentView = runtimeContext.view;
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
					out.addRequest(loc_getInitPackageRequest());
				}
				else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				}
			}
			return out;
		},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE);
	loc_out.id = nosliw.generateId();
	
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
