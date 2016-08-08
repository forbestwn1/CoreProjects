/**
 * utility object containing all the methods related with events
 * 		listen to event
 * 		trigger event
 * 		unregisterEvent
 * backbone implements all the event behavior
 * however, backbone pollute original object by adding many new attribute
 * therefore, we add an attribute to original object, and that attribute is the source and listener object for backbone
 */
var nosliwEventUtility = function(){
	var EVENT_ATTRIBUTE = "____eventAttribute";
	
	var loc_getEventObject = function(object){
		var eventObj = object[EVENT_ATTRIBUTE]; 
		if(eventObj==undefined){
			//if not event object, build event object
			eventObj = {};
			_.extend(eventObj, Backbone.Events);
			object[EVENT_ATTRIBUTE] = eventObj;
		}
		return eventObj;
	};
	
	return {
		/*
		 * trigger event on source
		 */
		triggerEvent : function(source, eventName, data){
			var sourceEvent = loc_getEventObject(source);
			sourceEvent.trigger(eventName, data);
		},

		/*
		 * register listener to source
		 */
		registerEvent : function(listener, source, eventName, handler, thisContext){
			var sourceEventObj = loc_getEventObject(source);
			var listenerEventObj = loc_getEventObject(listener);
			var that = thisContext;
			if(that==undefined)  that = source;
			if(eventName===undefined)  eventName = NOSLIWCONSTANT.EVENT_EVENTNAME_ALL; 
			
			//for event in backbone.js, the parms are different depending on the event type
			//for "all" event, the first parm is event name
			//for other event, the first parm is the beginning of data
			var isAllEvent = false;
			if(NOSLIWCONSTANT.EVENT_EVENTNAME_ALL===eventName)  isAllEvent = true;
			
			listenerEventObj.listenTo(sourceEventObj, eventName, function(event, data){
				//within this method, "this" refer to listenerEventObj
				//we need to set "this" as source
				if(isAllEvent===true)	handler.call(that, event, data);
				else		handler.call(that, eventName, event);
			});
		},
		
		/*
		 * stop listener from listenering any events
		 */
		unregister : function(listener){
			if(_.isFunction(listener)){
				for(var i in listener){
					var listenerEventObj = loc_getEventObject(listener[i]);
					listenerEventObj.stopListening();
				}
			}
			else{
				var listenerEventObj = loc_getEventObject(listener);
				listenerEventObj.stopListening();
			}
		},
		
		unregisterAllListeners : function(listeners){
			var that = this;
			//unregister all listeners
			_.each(listeners, function(listener, key){
				that.unregister(listener);
			});
		},
		
	};
}();

