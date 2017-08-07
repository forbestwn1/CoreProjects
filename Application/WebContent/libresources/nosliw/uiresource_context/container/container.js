/**
 * 
 */
function registerContainerContextEvent(context, contextPath, action, code){
	var contextValue = context[contextPath.name];
	var contextPathEventInfo = {
		'data' : contextValue,
		'expectPath' : contextPath, 
	};
	
	_.extend(contextPathEventInfo, Backbone.Events);
	contextPathEventInfo.listenTo(contextValue.data, EVENT_DATA_CHANGE+':'+EVENT_ELEMENT_ADD, function(eventData, requestInfo){
		action.call(this, EVENT_ELEMENT_ADD, contextPath, eventData, requestInfo);
	}); 
	contextPathEventInfo.listenTo(contextValue.data, EVENT_DATA_CHANGE+':'+EVENT_ELEMENT_REMOVE, function(eventData, requestInfo){
		action.call(this, EVENT_ELEMENT_REMOVE, contextPath, eventData, requestInfo);
	}); 
	return contextPathEventInfo;
};

var unregisterContainerContextEvent = function(contextPathEventInfo){
	contextPathEventInfo.stopListening();
};

