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
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_IOTaskResult;
	var node_createIODataSet;
	var node_createDataAssociation;
//*******************************************   Start Node Definition  ************************************** 	

var node_ioTaskProcessor = function(){
	
	var loc_out = {
			
		getExecuteIOTaskRequest : function(externalIO, ioMapping, getTaskRequest, handlers, request){
			return loc_out.getExecuteIORequest(
					externalIO, 
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_INPUTMAPPING], 
					getTaskRequest, 
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEWRAPPERTASK_OUTPUTMAPPING], 
					externalIO, 
					handlers, 
					request);
		},
			
		getExecuteIORequest : function(input, inputDataAssociation, getTaskRequest, outputDataAssociationByResult, outputIO, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			var inputIO = node_createIODataSet(input);
			var taskInputIO = node_createIODataSet();
			var inputDataAssociation = node_createDataAssociation(inputIO, inputDataAssociation, taskInputIO);
			out.addRequest(inputDataAssociation.getExecuteRequest({
				success : function(requestInfo, taskInputIO){
					var taskInput = taskInputIO.getData();
					//execute task
					var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTask", {}));
					executeIOTaskRequest.addRequest(getTaskRequest(taskInput, {
						success : function(request, taskResult){
							//process output association according to result name
							var outputDataAssociationDef;
							if(typeof outputDataAssociationByResult === "function")		outputDataAssociationDef = outputDataAssociationByResult(taskResult.resultName);
							else   outputDataAssociationDef = outputDataAssociationByResult[taskResult.resultName];

							var inputDataAssociation = node_createDataAssociation(taskResult.resultValue, outputDataAssociationDef, outputIO);
							return inputDataAssociation.getExecuteRequest({
								success :function(request, taskOutputDataSetIO){
									return new node_IOTaskResult(taskResult.resultName, taskOutputDataSetIO);
								}
							});
						}
					}));
					return executeIOTaskRequest;
				}
			}));
			return out;
		},
	};
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskProcessor", node_ioTaskProcessor); 

})(packageObj);
