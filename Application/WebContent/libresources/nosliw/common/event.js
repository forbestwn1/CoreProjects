//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "EVENT";

	var node_createEventObject = function(){
		var loc_backboneEventObj = _.extend({}, Backbone.Events);
		var loc_listeners = [];
	
		var out = {
				pri_getBackboneEventObj : function(){
					return loc_backboneEventObj;
				},
				
				/*
				 * trigger event on source
				 * eventName : event name
				 * parms : can be any multiple value to transfer to handler 
				 */
				triggerEvent : function(eventName){
					loc_backboneEventObj.trigger(eventName, arguments);
				},

				/*
				 * register listener to source
				 */
				registerListener : function(eventName, listener, handler, thisContext){
					var listenerEventObj = node_getEventInterface(listener);
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
						var parms;
						if(isAllEvent===true){
							parms = data;
							parms[0] = event;
						}
						else{
							parms = event;
							parms[0] = eventName;
						}
						handler.apply(that, parms);
					});
					loc_listeners.push(listener);
				},
				
				/*
				 * stop listener from listenering any events
				 */
				unregister : function(listener){
					var listenerEventObj = node_getEventInterface(listener).pri_getBackboneEventObj();
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
					//stop listening to other 
					this.pri_getBackboneEventObj().stopListening();
					//unregister all listeners
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
		var eventObj = node_createEventObject();
		return node_buildInterface(obj, INTERFACENAME, eventObj);
	};
	
	var node_getEventInterface = function(object){
		var eventObj = node_getInterface(object, INTERFACENAME);
		if(eventObj==undefined){
			var obj = loc_makeObjectWithEvent(object);
			eventObj = node_getEventInterface(obj);
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
var node_utility = 
	{
		/*
		 * trigger event on source
		 */
		triggerEvent : function(source, eventName, data){
			node_getEventInterface(source).triggerEvent(eventName, data);
		},

		/*
		 * register listener to source
		 */
		registerListener : function(listener, source, eventName, handler, thisContext){
			node_getEventInterface(source).registerListener(eventName, listener, handler, thisContext);
		},
		
		/*
		 * stop listener from listenering any events
		 */
		unregister : function(source, listener){
			node_getEventInterface(source).unregister(listener);
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
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createEventObject", node_createEventObject); 
packageObj.createChildNode("getEventInterface", node_getEventInterface); 
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
