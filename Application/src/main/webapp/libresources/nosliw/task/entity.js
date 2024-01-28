//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_getLifecycleInterface;
	var node_getApplicationInterface;
	var node_getComplexEntityObjectInterface;
	var node_createVariablesGroup;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskInfo = function(entityPath, adapterInfo){
	
	var loc_entityPath;
	
	var loc_adapterInfo;

	var loc_init = function(entityPath, adapterInfo){
		if(node_basicUtility.isStringValue(entityPath)){
			loc_entityPath = entityPath;
			loc_adapterInfo = adapterInfo;
		}
		else{
			loc_entityPath = entityPath[node_COMMONATRIBUTECONSTANT.INFOTASK_ENTITYPATH];
			loc_adapterInfo = entityPath[node_COMMONATRIBUTECONSTANT.INFOTASK_ADAPTERINFO];
		}
	};
	
	var loc_out = {
		
		getEntityPath : function(){   return loc_entityPath;   },
		
		getAdapterInfo : function(){   return loc_adapterInfo;     }	
		
	};
	
	loc_init(entityPath, adapterInfo);
	return loc_out;
};


//process output
var node_ExecutableResult = function(resultName, value){
	this.resultName = resultName;
	this.resultValue = value;
};

//info
//requirement: resource to support the execute the task, for instance, interface, 
var node_createTaskInput1 = function(info, adapterInfo, requirement){
	
	var loc_requirement = requirement;
	
	var loc_info = info;
	
	var loc_adapterInfo = adapterInfo;
	
	var loc_out = {
		
		getRequirement : function(){    return loc_requirement;    },
			
		getInfo : function(){   return loc_info;   },
		
		getAdapterInfo : function(){   return loc_adapterInfo;     }	
		
	};
	return loc_out;
};


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
	
		loc_taskGroupInterface = node_getApplicationInterface(loc_taskGroupEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_TASKCONTAINER);

		var valueContext = node_getComplexEntityObjectInterface(loc_taskGroupEntityCore).getValueContext()
		loc_contextVarGroup = node_createVariablesGroup(valueContext, loc_taskGroupInterface.getItemVariableInfos(loc_taskItemId), loc_contextVarsGroupHandler, this);
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
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getComplexEntityObjectInterface", function(){node_getComplexEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.createVariablesGroup", function(){  node_createVariablesGroup = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("ExecutableResult", node_ExecutableResult); 
packageObj.createChildNode("createTaskGroupItemWatch", node_createTaskGroupItemWatch); 
packageObj.createChildNode("createTaskInput1", node_createTaskInput1); 
packageObj.createChildNode("createTaskInfo", node_createTaskInfo); 

})(packageObj);
