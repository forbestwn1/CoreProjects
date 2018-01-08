//get/create package
var packageObj = library.getChildPackage("context");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithType;
var node_getObjectType;
var node_makeObjectWithLifecycle;
var node_getLifecycleInterface;
var node_createRequestEventGroupHandler;
//*******************************************   Start Node Definition  ************************************** 	
/**
 * this is a factory to create variables group
 * this group contains context variables: context name + path
 * this object help to build event response for context variable
 * 		variables : a group of context variables
 * 		handler : function(contextVariableEventInfo) handle all context variables events
 * 		thisContext : the this context for event handler
 */

var node_createContextVariablesGroup = function(context, contextVariables, handler, thisContext){

	//context
	var loc_context = context;
	//event handler
	var loc_handler = handler;
	//variables
	var loc_variables = {};
	
	var loc_thisContext = thisContext;
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(contextVariable){
		var variable = loc_context.createVariable(contextVariable);
		loc_variables[contextVariable.key] = variable;
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), contextVariable.key);
		loc_requestEventGroupHandler.addElement(variable.getLifecycleEventObject(), contextVariable.key);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(context, contextVariablesArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in contextVariablesArray){
			loc_addElement(contextVariablesArray[i]);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		loc_thisContext = undefined;
		loc_context = undefined;
		loc_handler = undefined;
	};

	var loc_out = {
		/*
		 * add 
		 */
		addVariable : function(contextVariable){	loc_addElement(contextVariable);		},
		
//		getVariable : function(key){	return loc_requestEventGroupHandler.getElement(key);		},
		getVariable : function(key){	return loc_variables[key];		},
		
		getVariables : function(){  return loc_variables;  },
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  }
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(context, contextVariables, handler, thisContext);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("request.event.createRequestEventGroupHandler", function(){node_createRequestEventGroupHandler = this.getData();});

//Register Node by Name
packageObj.createChildNode("createContextVariablesGroup", node_createContextVariablesGroup); 

})(packageObj);
