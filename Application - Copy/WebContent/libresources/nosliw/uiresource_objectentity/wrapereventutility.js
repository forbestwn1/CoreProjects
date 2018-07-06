/**
 * 
 */
nosliwWrapperEventUtility = function(){
	
	return {

		registerDataOperationEvent : function(source, handler, thisContext){
			var listener = {};
			nosliwEventUtility.registerEvent(listener, source, NOSLIWCONSTANT.EVENT_EVENTNAME_ALL, function(event, data){
				handler.call(this, event, data.path, data.data, data.requestInfo);
			}, thisContext);
			return listener;
		},
		
		trigueDataOperationEvent : function(source, event, path, eventData, requestInfo){
			nosliwEventUtility.triggerEvent(source, event, {
				path : nosliwCommonUtility.emptyStringIfUndefined(path),
				data : eventData,
				requestInfo : requestInfo,
			});		
		},
	};
}();

/*
 * a full name of data operation event has two parts:
 * 		event name
 * 		path from root data
 */
var nosliwCreateDataOperationEvent = function(event, path){
	//real event
	var loc_event = event;
	//path from root data
	var loc_path = path;
	//full event name
	var loc_fullEvent = undefined;
	
	if(path==undefined){
		//if path is undefined, event is full name, the try to parse it to get event name and path
		var index = event.indexof(':');
		if(index!=-1){
			loc_event = event.substring(0, index);
			loc_path = event.substring(index+1);
		}
	}
	
	loc_fullEvent = loc_event + ":" + loc_path;
	
	var loc_out = {
		getEvent : function(){ return loc_event; },
		getPath : function(){ return loc_path; },
		getFullEvent : function(){ return loc_fullEvent; },
	};
	
	return loc_out;
};

