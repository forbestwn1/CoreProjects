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
	var node_createActivity;
	var node_getObjectType;

//*******************************************   Start Node Definition  **************************************
var node_createTaskRuntime = function(envObj){

	var loc_envObj = envObj; 

	var loc_getExecuteTaskInSuiteRequest = function(taskSuite, taskId, input, handlers, request){
		var task = taskSuite[node_COMMONATRIBUTECONSTANT.EXECUTABLETASKSUITE_TASK][taskId];
		return loc_getExecuteTaskRequest(task, input, handlers, request);
	};
	
	var loc_getExecuteTaskRequest = function(task, input, handlers, request){
		if(task==undefined)   return;
		var out;
		if(task[node_COMMONATRIBUTECONSTANT.EXECUTABLETASK_TASKTYPE]==node_COMMONCONSTANT.TASK_TYPE_ACTIVITY){
			out = node_createActivity(task, input, loc_envObj).getExecuteRequest(handlers, request);
		}
		else if(task[node_COMMONATRIBUTECONSTANT.EXECUTABLETASK_TASKTYPE]==node_COMMONCONSTANT.TASK_TYPE_SEQUENCE){
			out = node_createSequence(task, input, loc_envObj).getExecuteRequest(handlers, request);
		}
		return out;
	};

	
	var loc_out = {

		getExecuteEmbededTaskRequest : function(task, inputValueIo, handlers, request){
			return loc_getExecuteTaskRequest(task, inputValueIo, handlers, request);
		},
		
		executeExecuteEmbededTasRequest : function(task, inputValueIo, handlers, request){
			var requestInfo = getExecuteEmbededTaskRequest(task, inputValueIo, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
			
		getExecuteTaskInSuiteRequest : function(taskSuite, taskId, inputValue, handlers, request){
			var updatedInputData = taskSuite[node_COMMONATRIBUTECONSTANT.EXECUTABLETASKSUITE_INITSCRIPT](inputValue);
			return loc_getExecuteTaskInSuiteRequest(taskSuite, taskId, updatedInputData, handlers, request);
		},
		
		executeExecuteTaskInSuiteRequest : function(taskSuite, taskId, inputValue, handlers, request){
			var requestInfo = this.getExecuteTaskInSuiteRequest(taskSuite, taskId, inputValue, handlers, request)
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getExecuteEmbededTaskInSuiteRequest : function(taskSuite, taskId, inputValueIo, handlers, request){
			return loc_getExecuteTaskInSuiteRequest(taskSuite, taskId, inputValueIo, handlers, request);
		},
		
		executeExecuteEmbededTaskInSuiteRequest : function(taskSuite, taskId, inputValueIo, handlers, request){
			var requestInfo = this.getExecuteEmbededTaskInSuiteRequest(taskSuite, taskId, inputValueIo, handlers, request);
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
var node_createTaskRuntimeFactory = function(){
	var loc_out = {
		createTaskRuntime : function(envObj){
			return node_createTaskRuntime(node_createEnv(envObj));
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
nosliw.registerSetNodeDataEvent("activity.createActivity", function(){node_createActivity = this.getData();});
nosliw.registerSetNodeDataEvent("sequence.createSequence", function(){node_createSequence = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});


//Register Node by Name
packageObj.createChildNode("createTaskRuntimeFactory", node_createTaskRuntimeFactory); 

})(packageObj);
