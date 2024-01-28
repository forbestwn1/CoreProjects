//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createDataAssociation;
	var node_createProcess;
	var node_taskUtility;
	var node_IOTaskResult;
	var node_IOTaskInfo;
	var node_ProcessResult;
	var node_resourceUtility;

//*******************************************   Start Node Definition  **************************************
var node_createProcessRuntime = function(envObj){

	var loc_envObj = envObj; 
	
	var loc_out = {

		//execute process by resource id
		//return ProcessResult with resultName and mapped output value by result
		getExecuteProcessResourceRequest : function(id, inputValue, outputMappingsByResult, handlers, requester_parent){
//			id = node_resourceUtility.buildReourceCoreIdLiterate(id);

			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteProcessResource", {"id":id, "input":inputValue}), handlers, requestInfo);
			//get process resource first
			out.addRequest(nosliw.runtime.getResourceService().getGetResourceDataByTypeRequest([id], node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_PROCESS, {
				success : function(requestInfo, processes){
					var process = processes[id];
					//execute process
					return node_createProcess(process, loc_envObj).getExecuteProcessRequest(inputValue, {
						success : function(request, processResult){
							if(outputMappingsByResult==undefined || outputMappingsByResult[processResult.resultName]==undefined){
								//no mapping needed, calculate value
								return processResult;
							}
							else{
								//otherwise, do mapping according to result
								var dataAssociation = node_createDataAssociation(
										processResult.resultValue,
										outputMappingsByResult[processResult.resultName], 
										undefined, 
										node_dataAssociationUtility.buildDataAssociationName("PROCESSRESULT", id, "RESULT", "MAPPEDRESULT")
								);
								return dataAssociation.getExecuteRequest(						
									{
										success : function(requestInfo, processOutputIODataSet){
											//calculate mapping output value
											return processOutputIODataSet.getGetDataValueRequest(undefined, {
												success : function(request, value){
													return new node_ProcessResult(processResult.resultName, value);
												}
											});
										}
									}
								);
							}
						}
					}); 
				}
			}));
			return out;
		},
			
		executeProcessResourceRequest : function(id, inputValue, outputMappingsByResult, handlers, requester_parent){
			var requestInfo = this.getExecuteProcessResourceRequest(id, inputValue, outputMappingsByResult, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

		getExecuteProcessRequest : function(processDef, inputValue, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("executeProcess", {}), handlers, request);
			out.addRequest(node_createProcess(processDef, loc_envObj).getExecuteProcessRequest(inputValue, {
				success : function(request, processResult){
					return new node_IOTaskResult(processResult.resultName, processResult.resultValue);
				}
			}));
			return out;
		},
		
		executeProcessRequest : function(processDef, inputValue, handlers, request){
			var requestInfo = this.getExecuteProcessRequest(processDef, inputValue, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		

		getExecuteEmbededProcessRequest : function(processDef, inputIODataSet, outputIODataSet, extraInputDataSet, handlers, requester_parent){
			var envObj = {
				//add method for sync data from internal process context to external process context
				getSyncOutRequest : function(internalValue, handlers, request){
					var taskOutputDataAssociation = node_createDataAssociation(internalValue, processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEGROUPDATAASSOCIATIONFORTASK_OUT][node_COMMONCONSTANT.NAME_DEFAULT], outputIODataSet, loc_buildTaskOutputDataAssociationName(processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK][node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ID]));
					return taskOutputDataAssociation.getExecuteRequest(handlers, request);
				}
			};
			envObj = _.extend(envObj, loc_envObj);
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteEmbededProcessRequest", {}), handlers, requester_parent);
			out.addRequest(node_taskUtility.getExecuteEmbededTaskRequest(
				inputIODataSet, 
				outputIODataSet,
				extraInputDataSet, 
				processDef, 
				new node_IOTaskInfo(function(inputValue, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("executeEmbededProcess", {}), handlers, request);
					out.addRequest(node_createProcess(processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK], loc_envObj).getExecuteProcessRequest(inputValue, {
						success : function(request, processResult){
							return new node_IOTaskResult(processResult.resultName, processResult.resultValue);
						}
					}));
					return out;
				}, processDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_TASK][node_COMMONATRIBUTECONSTANT.EXECUTABLEPROCESS_ID]), 
				{
					success : function(requestInfo, processResult){
						//calculate mapping output value
						return processResult.resultValue.getGetDataValueRequest(undefined, {
							success : function(request, value){
								return new node_ProcessResult(processResult.resultName, value);
							}
						});
					}
				}
			));
			return out;
		},

		executeEmbededProcessRequest : function(processDef, inputIODataSet, outputIODataSet, extraInputDataSet, handlers, requester_parent){
			var requestInfo = this.getExecuteEmbededProcessRequest(processDef, inputIODataSet, outputIODataSet, extraInputDataSet, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		//process defined within other component
		//extraInputDataSet is other component's contextIo
		//return : IOTaskResult with resultName and outputIODataSet (which is externalIODataSet)
		getExecuteWrappedProcessRequest : function(processDef, externalIODataSet, extraInputDataSet, handlers, requester_parent){
			return loc_out.getExecuteEmbededProcessRequest(processDef, externalIODataSet, externalIODataSet, extraInputDataSet, handlers, requester_parent);
		},

		executeWrappedProcessRequest : function(processDef, externalIODataSet, extraInputDataSet, handlers, requester_parent){
			var requestInfo = this.getExecuteWrappedProcessRequest(processDef, externalIODataSet, extraInputDataSet, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//process runtime factory
//when create process runtime, the process environment is needed
//process environment is object connect the process to external 
//example include process in module, app, or others
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("process.createProcess", function(){node_createProcess = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createProcessRuntimeFactory", node_createProcessRuntimeFactory); 

})(packageObj);
