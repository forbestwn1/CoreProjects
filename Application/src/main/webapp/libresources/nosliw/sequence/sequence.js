//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_CONSTANT;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_taskUtility;
	var node_createIODataSet;
	var node_getLifecycleInterface;
	var node_makeObjectWithLifecycle;
	var node_makeObjectWithType;
	var node_requestServiceProcessor;
	var node_IOTaskInfo;
	var node_ExecutableResult;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  **************************************
var loc_moduleName = "sequence";	

//process has contextIO as its status
var node_createSequence = function(sequenceDef, inputData, envObj){
	var loc_sequenceDef = sequenceDef;
	//environment obj for activity to interact with external world
	var loc_envObj = envObj;
	//io data for activity status
	var loc_sequenceStatusIO;
	
	//input data type, value or data set io, different data type have different output 
	var loc_inputDataType;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT]  = function(sequenceDef, inputData, envObj){
		loc_inputDataType = node_getObjectType(inputData);
		
		loc_sequenceStatusIO = node_createIODataSet(inputData);

		if(loc_envObj.getSyncOutRequest==undefined){
			var envObj = {
					//add method for sync data from internal process context to external process context
					getSyncOutRequest : function(internalValue, handlers, request){
						return node_createServiceRequestInfoSimple(undefined, function(request){}, handlers, request);
					}
				};
			loc_envObj = _.extend(envObj, loc_envObj);
		}
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
	};	
	
	var loc_getExecuteSequenceRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
		
		var steps = loc_sequenceDef[node_COMMONATRIBUTECONSTANT.EXECUTABLESEQUENCE_STEP];
		out.addRequest(loc_getExecuteStepRequest(steps, 0, {
			success : function(request, taskResult){
				if(taskResult.resultName==node_COMMONCONSTANT.TASK_RESULT_SUCCESS){
					if(loc_inputDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
						//if input data is io data set, out put is io data set as well
						return new node_ExecutableResult(taskResult.resultName, loc_sequenceStatusIO);
					}
					else{
						//otherwise, input data is value, then output is value
						return loc_sequenceStatusIO.getGetDataValueRequest(undefined, {
							success : function(request, value){
								return new node_ExecutableResult(taskResult.resultName, value);
							}
						});
					}
				}
				else{
					//if error, stop sequence
					return taskResult;
				}
			}
		}));
		
		return out;
	};
	
	var loc_getExecuteStepRequest = function(steps, current, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
		
		out.addRequest(nosliw.runtime.getTaskRuntimeFactory().createTaskRuntime(loc_envObj).getExecuteEmbededTaskRequest(steps[current], loc_sequenceStatusIO, {
			success : function(request, taskResult){
				if(taskResult.resultName==node_COMMONCONSTANT.TASK_RESULT_SUCCESS){
					current++;
					if(current>=steps.length){
						//last step
						return taskResult;
					}
					else{
						return loc_getExecuteStepRequest(steps, current);
					}
				}
				else{
					//if error, stop sequence
					return taskResult;
				}
			}
		}));
		
		return out;
	};
	
	
	var loc_out = {
		
		getExecuteRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_getExecuteSequenceRequest({
				success : function(request, sequenceTaskResult){
					if(loc_inputDataType==node_CONSTANT.TYPEDOBJECT_TYPE_DATAASSOCIATION_IODATASET){
						//if input data is io data set, out put is io data set as well
						return new node_ExecutableResult(sequenceTaskResult.resultName, loc_sequenceStatusIO);
					}
					else{
						//otherwise, input data is value, then output is value
						return loc_sequenceStatusIO.getGetDataValueRequest(undefined, {
							success : function(request, value){
								return new node_ExecutableResult(sequenceTaskResult.resultName, value);
							}
						});
						
					}
				}
			}));
			return out;
		},

		executeRequest : function(handlers, request){
			var requestInfo = this.getExecuteRequest(handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		}
			
	};
	
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_SEQUENCE);

	node_getLifecycleInterface(loc_out).init(sequenceDef, inputData, envObj);

	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});
nosliw.registerSetNodeDataEvent("task.entity.ExecutableResult", function(){node_ExecutableResult = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createSequence", node_createSequence); 

})(packageObj);
