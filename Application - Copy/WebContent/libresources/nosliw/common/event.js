//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "EVENT";

	var loc_createEventObject = function(){
		var loc_backboneEventObj = _.extend({}, Backbone.Events);
		var loc_listeners = [];
	
		var out = {
				pri_getBackboneEventObj : function(){
					return loc_backboneEventObj;
				},
				
				/*
				 * trigger event on source
				 */
				triggerEvent : function(eventName, data){
					loc_backboneEventObj.trigger(eventName, data);
				},

				/*
				 * register listener to source
				 */
				registerEvent : function(eventName, listener, handler, thisContext){
					var listenerEventObj = node_getEventObject(listener);
					var sourceEventObj = loc_backboneEventObj;
					var that = thisContext;
					if(that==undefined)  that = this.getBaseObject();
					if(eventName===undefined)  eventName = node_CONSTANT.EVENT_EVENTNAME_ALL; 
					
					//for event in backbone.js, the parms are different depending on the event type
					//for "all" event, the first parm is event name
					//for other event, the first parm is the beginning of data
					var isAllEvent = false;
					if(node_CONSTANT.EVENT_EVENTNAME_ALL===eventName)  isAllEvent = true;
					
					listenerEventObj.pri_getBackboneEventObj().listenTo(sourceEventObj, eventName, function(event, data){
						//within this method, "this" refer to listenerEventObj
						//we need to set "this" as source
						if(isAllEvent===true)	handler.call(that, event, data);
						else		handler.call(that, eventName, event);
					});
					loc_listeners.push(listener);
				},
				
				/*
				 * stop listener from listenering any events
				 */
				unregister : function(listener){
					var listenerEventObj = node_getEventObject(listener);
					listenerEventObj.stopListening(loc_backboneEventObj);
				},
				
				unregisterAllListeners : function(listeners){
					var that = this;
					//unregister all listeners
					_.each(listeners, function(listener, key){
						that.unregister(listener);
					});
				},
				
				clearup : function(){
					this.unregisterAllListeners(loc_listeners);
					loc_listeners = [];
				}
		};
		
		return out;
	};
	
	/*
	 * build an object with event obj
	 */
	var loc_makeObjectWithEvent = function(obj){
		var eventObj = loc_createEventObject();
		return node_buildInterface(obj, INTERFACENAME, eventObj);
	};
	
	var node_getEventObject = function(object){
		var eventObj = node_getInterface(object, INTERFACENAME);
		if(eventObj==undefined){
			var obj = loc_makeObjectWithEvent(object);
			eventObj = node_getEventObject(obj);
		}
		return eventObj;
	};

/**
 * utility object containing all the methods related with events
 * 		listen to event
 * 		trigger event
 * 		unregisterEvent
 * backbone implements all the event behavior
 * however, backbone pollute original object by adding many new attribute
 * therefore, we add an attribute to original object, and that attribute is the source and listener object for backbone
 */
var node_eventUtility = 
	{
		/*
		 * trigger event on source
		 */
		triggerEvent : function(source, eventName, data){
			node_getEventObject(source).triggerEvent(eventName, data);
		},

		/*
		 * register listener to source
		 */
		registerEvent : function(listener, source, eventName, handler, thisContext){
			node_getEventObject(source).registerEvent(eventName, listener, handler, thisContext);
		},
		
		/*
		 * stop listener from listenering any events
		 */
		unregister : function(source, listener){
			node_getEventObject(source).unregister(listener);
		},
		
		unregisterAllListeners : function(source, listeners){
			var that = this;
			//unregister all listeners
			_.each(listeners, function(listener, key){
				that.unregister(source, listener);
			});
		},
	};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("getEventObject", node_getEventObject); 
packageObj.createNode("utility", node_eventUtility); 

var module = {
	start : function(packageObj){
		node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
		node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
		node_getInterface = packageObj.getNodeData("common.interface.getInterface");
	}
};
nosliw.registerModule(module, packageObj);

})(packageObj);
