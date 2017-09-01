/**
 * this is a factory to create variables group
 * this group contains context variables: context name + path
 * this object help to build event response for context variable
 * 		variables : a group of context variables
 * 		handler : function(contextVariableEventInfo) handle all context variables events
 * 		thisContext : the this context for event handler
 */

var nosliwCreateContextVariablesGroup = function(context, contextVariables, handler, thisContext){

	//context
	var loc_context = context;
	//event handler
	var loc_handler = handler;
	
	var loc_requestEventGroupHandler = undefined;
	
	var loc_addElement = function(contextVariable){
		var variable = loc_context.createVariable(contextVariable);
		loc_requestEventGroupHandler.addElement(variable, contextVariable.key);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(context, contextVariablesArray, handler, thisContext){
		loc_requestEventGroupHandler = nosliwCreateRequestEventGroupHandler(loc_handler, function(variable, requestEventHandler){
			var listeners = []; 
			//listen to data operation event
			listeners.push(variable.registerDataChangeEvent(requestEventHandler));

			//listen to context event
			listeners.push(variable.registerLifecycleEvent(requestEventHandler));		
			return listeners;
		}, thisContext);
		
		for(var i in contextVariablesArray){
			loc_addElement(contextVariablesArray[i]);
		}
	};	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(requestInfo){
		loc_requestEventGroupHandler.destroy(requestInfo);
		loc_thisContext = undefined;
		loc_handler = undefined;
	};

	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		/*
		 * add 
		 */
		addVariable : function(contextVariable){	loc_addElement(contextVariable);		},
		
		getVariable : function(key){	return loc_requestEventGroupHandler.getElement(key);		},
	};
	
	//append resource and object life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out);
	loc_out.init(context, contextVariables, handler, thisContext);
	return loc_out;
};
