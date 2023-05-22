//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_IOTaskResult;
	var node_createIODataSet;
	var node_createDataAssociation;
//*******************************************   Start Node Definition  ************************************** 	
//do task process with input data association and output io 
var node_taskUtility = function(){

	loc_buildTaskInputDataAssociationName = function(taskName){		return "TASKINPUT_" + taskName;		};

	loc_buildTaskOutputDataAssociationName = function(taskName){		return "TASKOUTPUT_" + taskName;	};

	var loc_out = {
			
		//input and output is same
		getExecuteWrappedTaskRequest : function(externalIODataSet, ioMapping, ioTaskInfo, handlers, request){
			return loc_out.getExecuteEmbededTaskRequest(externalIODataSet, externalIODataSet, ioMapping, ioTaskInfo, handlers, request);
		},

		//input and output may not same
		getExecuteEmbededTaskRequest : function(inputIODataSet, outputIODataSet, ioMapping, ioTaskInfo, handlers, request){
			return loc_out.getExecuteTaskRequest(
					inputIODataSet, 
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEGROUPDATAASSOCIATIONFORTASK_IN], 
					ioTaskInfo, 
					ioMapping[node_COMMONATRIBUTECONSTANT.EXECUTABLEGROUPDATAASSOCIATIONFORTASK_OUT], 
					outputIODataSet, 
					handlers, 
					request);
		},

		getExecuteTaskRequest : function(inputIODataSet, inputDataAssociationDef, ioTaskInfo, outputDataAssociationByResult, outputIODataSet, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			var taskInputIODataSet = ioTaskInfo.inIO;
			var taskInputDataAssociation = node_createDataAssociation(inputIODataSet, inputDataAssociationDef, taskInputIODataSet, loc_buildTaskInputDataAssociationName(ioTaskInfo.taskName));   //data association for input for task
			out.addRequest(taskInputDataAssociation.getExecuteRequest({
				success : function(requestInfo){
					return ioTaskInfo.taskRequestFun({
						success : function(request, resultName){
							//process output association according to result name
							var outputDataAssociationDef;
							if(outputDataAssociationByResult!=undefined){
								if(typeof outputDataAssociationByResult === "function")		outputDataAssociationDef = outputDataAssociationByResult(resultName);
								else{
									outputDataAssociationDef = outputDataAssociationByResult[resultName];
									if(outputDataAssociationDef==undefined)   outputDataAssociationDef = outputDataAssociationByResult[node_COMMONCONSTANT.NAME_DEFAULT];
								}
							}

							var taskOutputDataAssociation = node_createDataAssociation(outputIODataSet, outputDataAssociationDef, ioTaskInfo.outIO[resultName], loc_buildTaskOutputDataAssociationName(ioTaskInfo.taskName));
							return taskOutputDataAssociation.getExecuteRequest({
								success :function(request){
									return resultName;
								}
							});
						}
					});
				}
			}));
			return out;
		},

		getExecuteTaskRequest1 : function(inputIODataSet, extraInputdata, inputDataAssociationDef, ioTaskInfo, outputDataAssociationByResult, outputIO, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteIOTask", {}), handlers, request);
			//process input association
			var taskInputDataAssociation = node_createDataAssociation(inputIODataSet, inputDataAssociationDef, ioTaskInfo.inIO!=undefined?ioTaskInfo.inIO:node_createIODataSet(), loc_buildTaskInputDataAssociationName(ioTaskInfo.taskName));   //data association for input for task
			out.addRequest(taskInputDataAssociation.getExecuteWithExtraDataRequest(extraInputdata, {
				success : function(requestInfo, taskInputDataSet){
					
					return taskInputDataSet.getGetDataValueRequest(undefined, {
						success : function(request, taskInput){
							//execute task
							var executeIOTaskRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTask", {}));
							executeIOTaskRequest.addRequest(ioTaskInfo.taskRequestFun(taskInput, {
								success : function(request, taskResult){
									//process output association according to result name
									var outputDataAssociationDef;
									if(outputDataAssociationByResult!=undefined){
										if(typeof outputDataAssociationByResult === "function")		outputDataAssociationDef = outputDataAssociationByResult(taskResult.resultName);
										else{
											outputDataAssociationDef = outputDataAssociationByResult[taskResult.resultName];
											if(outputDataAssociationDef==undefined)   outputDataAssociationDef = outputDataAssociationByResult[node_COMMONCONSTANT.NAME_DEFAULT];
										}
									}

									var taskOutputDataAssociation = node_createDataAssociation(ioTaskInfo.outIO!=undefined?ioTaskInfo.outIO:taskResult.resultValue, outputDataAssociationDef, outputIO, loc_buildTaskOutputDataAssociationName(ioTaskInfo.taskName));
									return taskOutputDataAssociation.getExecuteRequest({
										success :function(request, taskOutputDataSetIO){
											return new node_IOTaskResult(taskResult.resultName, taskOutputDataSetIO);
										}
									});
								}
							}));
							return executeIOTaskRequest;
						}
					});
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociation", function(){node_createDataAssociation = this.getData();});

//Register Node by Name
packageObj.createChildNode("taskUtility", node_taskUtility); 

})(packageObj);
