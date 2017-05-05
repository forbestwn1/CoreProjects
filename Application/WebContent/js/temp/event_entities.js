/**
 * 
 */
nosliwCreateRequestEventSource = function(){
	
	var loc_eventSource = {};
	var loc_eventListeners = [];
	
	var loc_out = {
		registerEventHandler : function(handler, thisContext, event){
			var e = event;
			if(e==undefined){
				e = NOSLIWCONSTANT.EVENT_EVENTNAME_ALL;
			}
			var listener = nosliwRequestUtility.registerEventWithRequest(loc_eventSource, e, handler, thisContext);
			loc_eventListeners.push(listener);
			return listener;
		},
		
		triggerEvent : function(event, data, requestInfo){
			nosliwRequestUtility.triggerEventWithRequest(loc_eventSource, event, data, requestInfo);
		},
		
		clearup : function(){
			nosliwEventUtility.unregisterAllListeners(loc_eventListeners);
			loc_eventListeners = [];
		},
	};
	
	return loc_out;
};


nosliwCreateWrapperOperationEventSource = function(){
	
	var loc_eventSource = {};
	var loc_eventListeners = [];
	
	var loc_out = {
		registerEventHandler : function(handler, thisContext, event){
			var e = event;
			if(e==undefined){
				e = NOSLIWCONSTANT.EVENT_EVENTNAME_ALL;
			}
			var listener = nosliwWrapperEventUtility.registerDataOperationEvent(loc_eventSource, handler, thisContext);
			loc_eventListeners.push(listener);
			return listener;
		},
		
		triggerEvent : function(event, path, data, requestInfo){
			nosliwWrapperEventUtility.trigueDataOperationEvent(loc_eventSource, event, path, data, requestInfo);
		},
		
		clearup : function(){
			nosliwEventUtility.unregisterAllListeners(loc_eventListeners);
			loc_eventListeners = [];
		},
	};
	
	return loc_out;
};


