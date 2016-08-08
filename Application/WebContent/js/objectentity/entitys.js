
/**
 * entity store all the information related with an context variable event
 */
var NosliwContextVariableEventInfo = function(event, context, contextVariable, eventData, requestInfo){
	//event name
	this.event = event;
	//context object
	this.context = context;
	//context variable
	this.contextVariable = contextVariable;
	//event data
	this.eventData = eventData;
	//original request information
	this.requestInfo = requestInfo;
};

