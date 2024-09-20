//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_makeObjectWithLifecycle;
	var node_getLifecycleInterface;
	var node_createRequestEventGroupHandler;
	var node_createValuePortElementInfo;
	var node_valuePortUtilityResolvedVariable;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * this is a factory to create variables group
 * this group contains context variables: context name + path
 * this object help to build event response for context variable
 * 		variables : a group of context variables
 * 		handler : function(contextVariableEventInfo) handle all context variables events
 * 		thisContext : the this context for event handler
 */

var node_createVariablesGroup = function(valuePortEnv, variableIdsArray, handler, thisContext){

	//value port env
	var loc_valuePortEnv;
	//event handler
	var loc_handler;
	//variables
	var loc_variables = [];
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(variableId){
		var variable = node_valuePortUtilityResolvedVariable.createValuePortVariable(loc_valuePortEnv, variableId);
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject());
		loc_variables.push(variable);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(valuePortEnv, variableIdsArray, handler, thisContext){
		loc_valuePortEnv = valuePortEnv;
		loc_handler = handler;

		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in variableIdsArray){
			loc_addElement(variableIdsArray[i]);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		
		_.each(loc_variables, function(variable, i){
			variable.release(requestInfo);
		});
		
		loc_valueContext = undefined;
		loc_handler = undefined;
	};

	var loc_out = {
		/*
		 * add 
		 */
		addVariable : function(contextVariable){	loc_addElement(contextVariable);		},
		
		getVariables : function(){  return loc_variables;  },
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(valuePortEnv, variableIdsArray, handler, thisContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortElementInfo", function(){node_createValuePortElementInfo = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.valuePortUtilityResolvedVariable", function(){node_valuePortUtilityResolvedVariable = this.getData();});

//Register Node by Name
packageObj.createChildNode("createVariablesGroup", node_createVariablesGroup); 

})(packageObj);
