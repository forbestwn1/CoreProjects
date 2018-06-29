//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_eventUtility;
	var node_makeObjectWithLifecycle;
	var node_createEventObject;
	var node_requestUtility;
	var node_getLifecycleInterface;
//*******************************************   Start Node Definition  ************************************** 	


/**
 * this is object that handler multiple event related with request
 * it wait until request is finished, then it emitt event 
 */
var node_createRequestEventGroupHandler = function(eventHandler, thisContext){
	
	//sync task name for remote call 
	var loc_moduleName = "requestEventGroup";
	
	var loc_eventObject = node_createEventObject();

	//how to handle 
	var loc_eventHandler = eventHandler;
	var loc_thisContext = thisContext;
	
	//all elements
	var loc_elements = {};
	
	//all active root requesters 
	var loc_requesters = {};

	var loc_size = 0;
	
	/*
	 * handle function for all element
	 * process the event only when 
	 * 		requestInfo is done status
	 * 		or requestInfo trigger done event
	 */
	var loc_processEvent = function(){
		request = node_requestUtility.getRequestInfoFromFunctionArguments(arguments);
		if(request==undefined){
			//no request
			loc_eventHandler.call(loc_thisContext);
			return;
		}
		
		//all processing is based on root request
		var rootRequest = request.getRootRequest();
		var requestId = rootRequest.getId();
		var request = loc_requesters[requestId];
		if(request==undefined){
			loc_requesters[requestId] = rootRequest;
			rootRequest.registerEventListener(loc_eventObject, function(e, data, req){
				if(e==node_CONSTANT.REQUEST_EVENT_ALMOSTDONE){
					if(loc_requesters[requestId]!=undefined){
						delete loc_requesters[requestId];
						loc_eventHandler.call(loc_thisContext, rootRequest);
					}
				}
				else if(e==node_CONSTANT.REQUEST_EVENT_DONE){
				}
			});
		}
		
		
//		var requestInfo = request.getRootRequest();
//		var requestId = requestInfo.getId();
//		var requestStatus = requestInfo.getStatus();
//		if(requestStatus==node_CONSTANT.REQUEST_STATUS_DONE){
//			loc_eventHandler.call(loc_thisContext, requestInfo);
//			delete loc_requesters[requestId];
//		}
//		else{
//			var request = loc_requesters[requestId];
//			if(request==undefined){
//				loc_requesters[requestId] = requestInfo;
//				requestInfo.registerEventListener(loc_eventObject, function(e, data, req){
//					if(e==node_CONSTANT.REQUEST_EVENT_DONE){
//						loc_eventHandler.call(loc_thisContext, requestInfo);
//						delete loc_requesters[requestId];
//					}
//				});
//			}
//		}
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){};	
	loc_resourceLifecycleObj[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY] = function(requestInfo){
		//unregister all listeners
		loc_eventObject.clearup();
		
		loc_eventHandler = undefined; 
		loc_thisContext = undefined;
		loc_requesters = {};
	};
	
	var loc_out = {
		/*
		 * add an element to group
		 */
		addElement : function(element, name){
			if(name==undefined)  name = loc_size+"";
			loc_elements[name] = element;
			//register element to event
			node_eventUtility.registerListener(loc_eventObject, element, undefined, loc_processEvent, this);
			loc_size++;
		},
		
		size : function(){return loc_size;},
		getElement : function(name){ return loc_elements[name+""]; },
		
		triggerEvent : function(requestInfo){
			loc_processEvent(undefined, undefined, requestInfo);
		},
		
		destroy : function(requestInfo){  node_getLifecycleInterface(loc_out).destroy(requestInfo);  },
	};
	
	//append resource life cycle method to out obj
	loc_out = node_makeObjectWithLifecycle(loc_out, loc_moduleName);
	
	return loc_out;
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.getLifecycleInterface", function(){node_getLifecycleInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("createRequestEventGroupHandler", node_createRequestEventGroupHandler); 

})(packageObj);
