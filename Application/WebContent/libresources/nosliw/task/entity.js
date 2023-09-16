//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

//process output
var node_ExecutableResult = function(resultName, value){
	this.resultName = resultName;
	this.resultValue = value;
}


var node_createTaskGroupItemWatch = function(taskGroupEntityCore, taskItemId, requestInfo){
	
	var loc_taskGroupEntityCore;
	var loc_taskGroupInterface;
	
	var loc_taskItemId;
	
	var loc_dataEventObject;
	
	var loc_contextVarGroup;
	
	//store result from last time calculation
	var loc_result;

	var loc_getExecuteRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteTaskItem", {}), handlers, request);
		out.addRequest(loc_taskGroupInterface.getExecuteItemRequest(loc_taskItemId));
		return out;
	};

	var loc_executeExecuteRequest = function(handlers, request){
		var requestInfo = loc_getExecuteRequest(handlers, request);
		node_requestServiceProcessor.processRequest(requestInfo);
	};
	
	var loc_contextVarsGroupHandler = function(requestInfo){
		loc_executeExecuteRequest({
			success : function(requestInfo, data){
				loc_result = data;
				loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_SUCCESS, data);
			},
			error : function(requestInfo, serviceData){
				loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_ERROR, serviceData);
			},
			exception : function(requestInfo, serviceData){
				loc_dataEventObject.triggerEvent(node_CONSTANT.REQUESTRESULT_EVENT_EXCEPTION, serviceData);
			}
		}, requestInfo);
	}
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(taskGroupEntityCore, taskItemId, requestInfo){

		loc_taskGroupEntityCore = taskGroupEntityCore;
	
		loc_taskItemId = taskItemId;
	
		loc_dataEventObject = node_createEventObject();
	
		loc_taskGroupInterface = node_getApplicationInterface(loc_taskGroupEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASK);

		var valueContext = node_getComplexEntityObjectInterface(loc_taskGroupEntityCore).getValueContext()
		loc_contextVarGroup = node_createContextVariablesGroup(valueContext, loc_taskGroupInterface.getItemVariableInfos(loc_taskItemId), loc_contextVarsGroupHandler, this);
	};
		
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(){
		loc_contextVarGroup.destroy();
	};

	var loc_out = {
			
		getExecuteRequest : function(handlers, requester_parent){
			return loc_getExecuteRequest(handlers, request);
		},

		executeExecuteRequest : function(handlers, requester_parent){
			var requestInfo = this.getExecuteRequest(handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		registerListener : function(listenerEventObj, handler){
			loc_dataEventObject.registerListener(undefined, listenerEventObj, handler);
		},
		
		refresh : function(requestInfo){
			loc_contextVarGroup.triggerEvent(requestInfo);
		},
		
		getResult : function(){
			return loc_result;
		},

		destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
	};

	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_getLifecycleInterface(loc_out).init(taskGroupEntityCore, taskItemId, requestInfo);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("ExecutableResult", node_ExecutableResult); 
packageObj.createChildNode("createTaskGroupItemWatch", node_createTaskGroupItemWatch); 

})(packageObj);
