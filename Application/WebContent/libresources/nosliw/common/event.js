//get/create package
var packageObj = library.getChildPackage("event");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_makeObjectWithType;
	var node_getObjectType;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "EVENT";

	var node_EventInfo = function(eventName, eventData){
		this.eventName = eventName;
		this.eventData = eventData;
	};
	
	var node_createEventObject = function(){
		var loc_backboneEventObj = _.extend({}, Backbone.Events);
		var loc_listeners = [];
	
		var out = {
				pri_getBackboneEventObj : function(){		return loc_backboneEventObj;		},
				
				/*
				 * trigger event on source
				 * eventName : event name
				 * parms : can be any multiple value to transfer to handler 
				 */
				triggerEvent : function(eventName){		loc_backboneEventObj.trigger(eventName, arguments);		},

				/*
				 * register listener to source
				 * 		listener : event object
				 */
				registerListener : function(eventName, listener, handler, thisContext){
					
					if (typeof handler != "function") {
						var kkkk = 5555;
						kkkk++;
					}
					
					var that = thisContext;
					if(that==undefined){
						if(this.getBaseObject!=null)   that = this.getBaseObject();
					}
					if(eventName===undefined)  eventName = node_CONSTANT.EVENT_EVENTNAME_ALL; 
					
					//for event in backbone.js, the parms are different depending on the event type
					//for "all" event, the first parm is event name
					//for other event, the first parm is the beginning of data
					var isAllEvent = false;
					if(node_CONSTANT.EVENT_EVENTNAME_ALL===eventName)  isAllEvent = true;
					
					if(listener==undefined){
						//if listener is not provided, then create one
						listener = node_createEventObject();
					}
					
					listener.pri_getBackboneEventObj().listenTo(loc_backboneEventObj, eventName, function(parm1, parm2){
						//within this method, "this" refer to listenerEventObj
						//we need to set "this" as source
						var parms;
						if(isAllEvent===true)		handler.apply(that, parm2);
						else		handler.apply(that, parm1);
					});
					loc_listeners.push(listener);

					//return listener object
					return listener;
				},
				
				/*
				 * stop listener from listenering any events
				 */
				unregister : function(listener){	listener.pri_getBackboneEventObj().stopListening(loc_backboneEventObj);	},
				
				unregisterAllListeners : function(){
					var that = this;
					//unregister all listeners
					_.each(loc_listeners, function(listener, key){
						that.unregister(listener);
					});
				},
				
				clearup : function(){
					//stop listening to other 
					this.pri_getBackboneEventObj().stopListening();
					//unregister all listeners
					this.unregisterAllListeners();
					loc_listeners = [];
				}
		};
		
		out = node_makeObjectWithType(out, node_CONSTANT.TYPEDOBJECT_TYPE_EVENTOBJECT);
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
		 * get event object from value
		 * if value is event object, then return value
		 * if value contains event object interface, then get interface object and return it
		 * if value not contains event object interface, then build event object interface, and return it 
		 */
		getEventObject : function(value){
			var out;
			if(node_getObjectType(value)==node_CONSTANT.TYPEDOBJECT_TYPE_EVENTOBJECT){
				out = value;
			}
			else{
				out = node_getEventInterface(value);
			}
			return out;
		},
	
		/*
		 * trigger event on source
		 */
		triggerEvent : function(source, eventName, data){
			var parms = [];
			for(var i=1; i<arguments.length; i++){
				parms.push(arguments[i]);
			}
			var eventObject = this.getEventObject(source);
			eventObject.triggerEvent.apply(eventObject, parms);
		},

		/*
		 * register listener to source
		 */
		registerListener : function(listener, source, eventName, handler, thisContext){
			var listenerEventObject = this.getEventObject(listener);
			var sourceEventObject = this.getEventObject(source);
			sourceEventObject.registerListener(eventName, listenerEventObject, handler, thisContext);
		},
		
		/*
		 * stop listener from listenering any events
		 */
		unregister : function(source, listener){
			this.getEventObject(source).unregister(this.getEventObject(listener));
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
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createEventObject", node_createEventObject); 
packageObj.createChildNode("utility", node_utility); 
packageObj.createChildNode("EventInfo", node_EventInfo); 

})(packageObj);
