//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_eventUtility;
	var node_makeObjectWithLifecycle;
//*******************************************   Start Node Definition  ************************************** 	


/**
 * this is object that handler multiple event related with request
 * it wait until request is finished, then it emitt event 
 */
var node_createRequestEventGroupHandler = function(eventHandler, registerElementEventFunction, thisContext){
	
	//sync task name for remote call 
	var loc_moduleName = "requestEventGroup";
	
	//how to handle 
	var loc_eventHandler = eventHandler;
	//function provided that used to register element event, this function should return listener object or array
	var loc_registerElementEventFunction = registerElementEventFunction;
	var loc_thisContext = thisContext;
	
	//all listeners for events from elements
	var loc_listeners = [];
	
	//all elements
	var loc_elements = {};
	
	//all active requesters 
	var loc_requesters = {};

	var loc_size = 0;
	
	/*
	 * handle function for all element
	 * process the event only when 
	 * 		requestInfo is done status
	 * 		or requestInfo trigger done event
	 */
	var loc_processEvent = function(){
		request = arguments[arguments.length-1];
		if(request==undefined){
			//no request
			loc_eventHandler.call(loc_thisContext);
			return;
		}
		
		//all processing is based on root request
		var requestInfo = request.getRootRequest();
		var requestId = requestInfo.getId();
		var requestStatus = requestInfo.getStatus();
		if(requestStatus==node_CONSTANT.REQUEST_STATUS_DONE){
			loc_eventHandler.call(loc_thisContext, requestInfo);
			delete loc_requesters[requestId];
		}
		else{
			var request = loc_requesters[requestId];
			if(request==undefined){
				loc_requesters[requestId] = requestInfo;
				requestInfo.registerEventListener(function(e, data, req){
					if(e==node_CONSTANT.REQUEST_EVENT_DONE){
						loc_eventHandler.call(loc_thisContext, requestInfo);
						delete loc_requesters[requestId];
					}
				});
			}
		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){};	
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(requestInfo){
		//unregister all listeners
		node_eventUtilty.unregisterAllListeners(loc_listeners);
		
		_.each(loc_elements, function(element, key){
			element.destroy(requestInfo);
		});
		
		loc_eventHandler = undefined; 
		loc_registerElementEventFunction = undefined;
		loc_thisContext = undefined;
		loc_listeners = [];
		loc_requesters = {};
	};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
			
		/*
		 * add an element to group
		 */
		addElement : function(element, name){
			if(name==undefined)  name = loc_size+"";
			loc_elements[name] = element;
			//register element to event
			var listener = loc_registerElementEventFunction.call(this, element, loc_processEvent);
			//save listener
			if(_.isFunction(listener)){
				for(var i in listener)  loc_listeners.push(listener[i]);
			}
			else  loc_listeners.push(listener);
			loc_size++;
		},
		
		size : function(){return loc_size;},
		getElement : function(name){ return loc_elements[name+""]; },
		
		triggerEvent : function(requestInfo){
			loc_processEvent(undefined, undefined, requestInfo);
		},
		
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_moduleName);
	
	return loc_out;
		
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("createRequestEventGroupHandler", node_createRequestEventGroupHandler); 

	var module = {
		start : function(packageObj){
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
			node_eventUtility = packageObj.getNodeData("common.event.utility");
			node_makeObjectWithLifecycle = packageObj.getNodeData("common.lifecycle.makeObjectWithLifecycle");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
