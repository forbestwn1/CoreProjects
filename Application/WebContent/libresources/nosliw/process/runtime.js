//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_objectOperationUtility;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_createProcess;

//*******************************************   Start Node Definition  **************************************
var node_createProcessRuntime = function(envObj){

	var loc_envObj = envObj; 
	
	var loc_out = {

		getExecuteProcessResourceRequest : function(id, input, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcessResource", {"id":id, "input":input}), handlers, requestInfo);
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([id], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_PROCESS, {
				success : function(requestInfo, processes){
					var process = processes[id];
					return node_createProcess(process, loc_envObj).getExecuteProcessRequest(input, outputMappingsByResult); 
//					loc_out.getExecuteProcessRequest(process, input, outputMappingsByResult);
				}
			}));
			
			return out;
		},
			
		executeProcessResourceRequest : function(id, input, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessResourceRequest(id, input, outputMappingsByResult, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteProcessRequest : function(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requester_parent);

			var outputMappingsByResult;
			var outputMappingDef = processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_OUTPUTMAPPING];
			if(outputMappingDef!=undefined){
				outputMappingsByResult = {};
				_.each(outputMappingDef, function(dataAssociation, resultName){
					outputMappingsByResult[resultName] = new node_ExternalMapping(loc_uiModule.getIOContext(), dataAssociation);
				});
			}

			var output = {};
			out.addRequest(node_createDataAssociation(input, processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_INPUTMAPPING], output).getExecuteDataAssociationRequest(extraInputDataSet, {
				success : function(request, input){
					return node_createProcess(processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK], loc_envObj).getExecuteProcessRequest(input.getData(), outputMappingsByResult);
				}
			}));
			return out;
		},
		
		executeProcessRequest : function(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessRequest(processDef, input, extraInputDataSet, outputMappingsByResult, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

var node_createProcessRuntimeFactory = function(){
	var loc_out = {
		createProcessRuntime : function(envObj){
			return node_createProcessRuntime(node_createEnv(envObj));
		}
	};
	return loc_out;
};

var node_createEnv = function(envObj){
	
	if(envObj==undefined)  envObj = {};
	
	var extended = {
			
		buildOutputVarialbeName : function(varName){
			return "nosliw_"+varName;
		},
			
	};
	
	return _.extend(extended, envObj);
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
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("process.createProcess", function(){node_createProcess = this.getData();});

//Register Node by Name
packageObj.createChildNode("createProcessRuntimeFactory", node_createProcessRuntimeFactory); 

})(packageObj);
