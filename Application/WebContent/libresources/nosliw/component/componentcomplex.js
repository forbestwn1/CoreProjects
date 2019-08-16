//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createState;
	var node_createConfigure;
	var node_createComponentDecoration;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

//component complex is composed of a component at the bottom and a list of decoration on top of it
//decoration may change the behavior of component by event, command, appearance, exposed interface
var node_createComponentComplex = function(configure, envInterface){

	var loc_configure = node_createConfigure(configure);
	
	var loc_state = node_createState();
	var loc_layers = [];
	var loc_interface = _.extend({}, envInterface);

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_getCurrentFacad = function(){   return loc_layers[loc_layers.length-1];  };
	
	var loc_getComponent = function(){  return  loc_layers[0]; };

	//for particular lifecycle request, every layer got invoked 
	var loc_getLifeCycleRequest = function(requestFunName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ComponentComplexLifycycle", {}), handlers, request);
		//start module
		_.each(loc_layers, function(layer, i){
			if(layer[requestFunName]!=undefined){
				out.addRequest(layer[requestFunName]());
			}
		});
		return out;
	};

	//process view from top layer to bottom layer
	//any layer can modify view and return new view and pass the new view to next layer
	var loc_updateView = function(view, request){
		for(var i=loc_layers.length-1; i>0; i--){
			var updated = loc_layers[i].updateView(view, request);
			if(updated!=undefined)  view = updated;  
			else break;
		}
	};
	
	var loc_unregisterFacadeListener = function(){	
		loc_getCurrentFacad().unregisterEventListener(loc_eventListener);	
		loc_getCurrentFacad().unregisterValueChangeEventListener(loc_valueChangeEventListener);	
	};

	var loc_registerFacadeListener = function(){
		loc_getCurrentFacad().registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			loc_eventSource.triggerEvent(event, eventData, requestInfo);
		});

		loc_getCurrentFacad().registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	};

	loc_registerLayerEvent = function(layerNum){
		loc_layers[layerNum].registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			var processedEvent = event;
			var processedEventData = eventData;
			var processedRequest = requestInfo;
			for(var i=layerNum; i<loc_layers.length; i++){
				var eventResult = loc_layers[i].processComponentEvent(processedEvent, processedEventData, processedRequest);
				if(eventResult==true || eventResult==undefined){
					//propagate the same event
				}
				else if(eventResult==false){
					//not propagate
					processedEvent = undefined;
					processedEventData = undefined;
					break;
				}
				else{
					//new event 
					processedEvent = eventResult.eventName;
					processedEventData = eventResult.eventData;
					processedRequest = eventResult.request;
				}
			}
			if(processedEvent!=undefined)	loc_eventSource.triggerEvent(processedEvent, processedEventData, processedRequest);

			loc_layers[layerNum].registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
				for(var i=layerNum; i<loc_layers.length; i++){
					loc_layers[i].processComponentValueChangeEvent(event, eventData, requestInfo);
				}
				loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
			});
		});

	},
	
	loc_addDecoration = function(componentDecorationInfo){
		var decName = componentDecorationInfo.name;
		var decoration = node_createComponentDecoration(decName, loc_getComponent(), componentDecorationInfo.coreFun, loc_interface, loc_configure.getConfigureData(decName), loc_state);
		loc_layers.push(decoration);
		if(decoration.getInterface!=undefined)	_.extend(loc_interface, decoration.getInterface());

		loc_registerLayerEvent(loc_layers.length-1);
	};
	
	var loc_out = {
		
		setComponent : function(component){
			loc_layers.push(component);
			loc_registerLayerEvent(0);
		},
		
		addDecorations : function(componentDecorationInfos){
			for(var i in componentDecorationInfos){  loc_out.addDecoration(componentDecorationInfos[i]);	}
		},

		addDecoration : function(componentDecorationInfo){		loc_addDecoration(componentDecorationInfo);		},

		getInterface : function(){  return loc_interface;   },
		
		getComponent : function(){   return loc_getComponent();    },
		
		registerEventListener : function(listener, handler, thisContext){  return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){  return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("Execute component command", {}), handlers, request);
			
			var layerCommand = command;
			var layerParms = parms;
			for(var i in loc_layers){
				var commandResult = loc_layers[i].getExecuteCommandRequest(layerCommand, layerParms);
				if(commandResult!=undefined){
					out.addRequest(commandResult.requestResult);
					if(commandResult.commandInfo!=undefined){
						layerCommand = commandResult.commandInfo.commandName;
						layerParms = commandResult.commandInfo.commandParms;
					}
				}
			}
			return out;
		},
		
		getAllStateData : function(){   return loc_state.getAllState();   },
		clearState : function(){   loc_state.clear();   },	
		setAllStateData : function(stateData){  loc_state.setAllState(stateData)  },
		
		updateView : function(view, request){  loc_updateView(view, request);  },
		
		getPreDisplayInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getPreDisplayInitRequest", handlers, request);  },
		getInitRequest : function(handlers, request){  return loc_getLifeCycleRequest("getInitRequest", handlers, request);  },
		getStartRequest : function(handlers, request){  return loc_getLifeCycleRequest("getStartRequest", handlers, request);  },
		getResumeRequest : function(handlers, request){  return loc_getLifeCycleRequest("getResumeRequest", handlers, request);  },
		getDeactiveRequest : function(handlers, request){  return loc_getLifeCycleRequest("getDeactiveRequest", handlers, request);  },
		getSuspendRequest : function(handlers, request){  return loc_getLifeCycleRequest("getSuspendRequest", handlers, request);  },
		getDestroyRequest : function(handlers, request){  return loc_getLifeCycleRequest("getDestroyRequest", handlers, request);  },

	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.createState", function(){node_createState = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentDecoration", function(){node_createComponentDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentComplex", node_createComponentComplex); 


})(packageObj);
