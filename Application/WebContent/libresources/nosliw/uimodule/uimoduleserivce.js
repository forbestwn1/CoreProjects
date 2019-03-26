//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_resourceUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIModuleService = function(){
	
	var loc_out = {

		getGetUIModuleRuntimeRequest : function(module, input, statelessData, decorations, envFactoryId, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUIModuleResource", {"module":module, "input":input}), handlers, requestInfo);

			var resourceIds = [];
			var moduleId;
			var uiModuleDef;
			if(typeof module === 'string'){
				moduleId = {};
				moduleId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = module; 
				moduleId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULE; 
				resourceIds.push(moduleId);
			}
			else{
				uiModuleDef = module;
			}

			var moduleEnvId = {};
			moduleEnvId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = envFactoryId; 
			moduleEnvId[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIMODULEENV; 
			resourceIds.push(moduleEnvId);

			//load ui module resource and env factory resource
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceIds, {
				success : function(requestInfo, resourceTree){
					var uiModuleDef = requestInfo.getData("uiModuleDef");
					var envFactory = node_resourceUtility.getResourceFromTree(resourceTree, moduleEnvId).resourceData;
					if(moduleId!=undefined)  uiModuleDef = node_resourceUtility.getResourceFromTree(resourceTree, moduleId).resourceData;
					
					//create ui module runtime
					return node_createModuleRuntimeRequest(uiModuleDef, input, statelessData, decorations, envFactory, {
						success : function(request, uiModuleRuntime){
							return uiModuleRuntime.getInitRequest({
								success :function(request, data){
									return uiModuleRuntime;
								}
							});
						}
					});
				}
			}).withData("uiModuleDef", uiModuleDef));
			
			return out;
		},
			
		executeGetUIModuleRuntimeRequest : function(id, input, decorations, envFactoryId, handlers, requester_parent){
			var requestInfo = this.getGetUIModuleRuntimeRequest(id, input, decorations, envFactoryId, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createModuleRuntimeRequest", function(){node_createModuleRuntimeRequest = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUIModuleService", node_createUIModuleService); 

})(packageObj);
