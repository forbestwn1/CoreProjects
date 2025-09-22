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
	var node_getEntityObjectInterface;
	var node_createVariablesGroup;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_basicUtility;
	var node_expressionUtility;
//*******************************************   Start Node Definition  ************************************** 	


var node_createExpressionTaskWatch = function(taskDef, valuePortEnv){
	
	var loc_taskWrapper;
	
	var loc_dataEventObject;
	
	var loc_contextVarGroup;
	
	//store result from last time calculation
	var loc_result;

	var loc_getExecuteRequest = function(handlers, request){
		return loc_taskWrapper.getExecuteRequest(handlers, request);
	};

	var loc_executeExecuteRequest = function(handlers, request){
		var requestInfo = loc_getExecuteRequest(handlers, request);
		node_requestServiceProcessor.processRequest(requestInfo);
	};
	
	var loc_varsGroupHandler = function(requestInfo){
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
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(taskDef, valuePortEnv, runtimeEnv){
		loc_taskWrapper = loc_createTaskWrapper(taskDef, valuePortEnv);
		loc_dataEventObject = node_createEventObject();
		loc_contextVarGroup = node_createVariablesGroup(valuePortEnv, loc_taskWrapper.getVariableIds(), loc_varsGroupHandler, this);
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
	node_getLifecycleInterface(loc_out).init(taskDef, valuePortEnv);
	return loc_out;
};


var loc_createTaskWrapper = function(taskDef, valuePortEnv){
	
	var loc_taskDef = taskDef;
	var loc_valuePortEnv = valuePortEnv;
	
	var loc_out = {
		
		getExecuteRequest : function(handlers, request){
			var out;
			var taskType = loc_taskDef[node_COMMONATRIBUTECONSTANT.WITHVARIABLE_ENTITYTYPE];
			if(taskType==node_COMMONCONSTANT.WITHVARIABLE_ENTITYTYPE_DATAEXPRESSION){
				out = node_expressionUtility.getExecuteDataExpressionRequest(loc_taskDef, loc_valuePortEnv, undefined, handlers, request);
			}
			else if(taskType==node_COMMONCONSTANT.WITHVARIABLE_ENTITYTYPE_SCRIPTEXPRESSION){
				out = node_expressionUtility.getExecuteScriptExpressionRequest(loc_taskDef, loc_valuePortEnv, undefined, handlers, request);
			}
			return out;
		},
		
		getVariableInfo : function(){
			return loc_taskDef[node_COMMONATRIBUTECONSTANT.WITHVARIABLE_VARIABLEINFOS];
		},
		
		getVariableIds : function(){
			var variableIds = [];
			_.each(this.getVariableInfo(), function(varInfo, i){
				variableIds.push(varInfo[node_COMMONATRIBUTECONSTANT.VARIABLEINFO_VARIABLEID]);
			});
			return variableIds;
		}
		
	};
	
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
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("variable.createVariablesGroup", function(){  node_createVariablesGroup = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});

packageObj.createChildNode("createExpressionTaskWatch", node_createExpressionTaskWatch); 

})(packageObj);
