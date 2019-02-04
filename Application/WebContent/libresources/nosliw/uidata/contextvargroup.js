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

var node_createContextVariableInfosGroup = function(context, contextVariableInfosArray, handler, thisContext){

	//context
	var loc_context = context;
	//event handler
	var loc_handler = handler;
	//variables
	var loc_variables = {};
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(contextVariableInfo){
		var variable = loc_context.createVariable(contextVariableInfo);
		loc_variables[contextVariableInfo.getFullPath()] = variable;
		loc_requestEventGroupHandler.addElement(variable.getDataChangeEventObject(), contextVariableInfo.key);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(context, contextVariableInfosArray, handler, thisContext){
		loc_requestEventGroupHandler = node_createRequestEventGroupHandler(loc_handler, thisContext);
		
		for(var i in contextVariableInfosArray){
			loc_addElement(contextVariableInfosArray[i]);
		}
	};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		
		_.each(loc_variables, function(variable, key){
			variable.release(requestInfo);
		});
		
		loc_context = undefined;
		loc_handler = undefined;
	};

	var loc_out = {
		/*
		 * add 
		 */
		addVariable : function(contextVariable){	loc_addElement(contextVariable);		},
		
		getVariable : function(fullPath){	return loc_variables[fullPath];		},
		
		getVariables : function(){  return loc_variables;  },
		
		triggerEvent : function(requestInfo){   loc_requestEventGroupHandler.triggerEvent(requestInfo);  },
		
		destroy : function(requestInfo){	node_getLifecycleInterface(loc_out).destroy(requestInfo);	},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_resourceLifecycleObj, loc_out);
	node_getLifecycleInterface(loc_out).init(context, contextVariableInfosArray, handler, thisContext);
	
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
packageObj.createChildNode("createContextVariablesGroup", node_createContextVariableInfosGroup); 

})(packageObj);
