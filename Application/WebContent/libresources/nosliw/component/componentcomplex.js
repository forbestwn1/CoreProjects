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
//decoration may change the behavior of component by event processing, command request, view appearance, exposed env interface
var node_createComponentComplex = function(configure, envInterface){

	//configuration for component
	var loc_configure = node_createConfigure(configure);
	
	//component state
	var loc_state = node_createState();
	//component and decoration layers
	var loc_layers = [];
	//env interface
	var loc_interface = _.extend({}, envInterface);

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_getCurrentFacad = function(){   return loc_layers[loc_layers.length-1];  };
	
	var loc_getComponent = function(){  return  loc_layers[0]; };

	var loc_getLifeCycleRequest = function(transitName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ComponentComplexLifycycle", {}), handlers, request);
		//start module
		_.each(loc_layers, function(layer, i){
			if(layer.getLifeCycleRequest!=undefined) out.addRequest(layer.getLifeCycleRequest(transitName));
		});
		return out;
	};

	//process view from top layer to bottom layer
	//any layer can modify view and return new view and pass the new view to next layer
	var loc_getUpdateViewRequest = function(view, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateView", {}), handlers, request);
		out.addRequest(loc_getUpdateLayerViewRequest(loc_layers.length-1, view));
		return out;
	};
	
	var loc_getUpdateLayerViewRequest = function(index, view, handlers, request){
		if(index<0 || view==undefined)  return;
		if(loc_layers[index].getUpdateViewRequest==undefined)  return;
		
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateComponentLayer", {}), handlers, request);
		out.addRequest(loc_layers[index].getUpdateViewRequest(view, {
			success : function(request, newView){
				return loc_getUpdateLayerViewRequest(index-1, newView);
			}
		}));
		return out;
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
			for(var i=layerNum+1; i<loc_layers.length; i++){
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
			//from top to bottom
			for(var i=loc_layers.length-1; i>=0; i--){
				var commandResult = loc_layers[i].getExecuteCommandRequest(layerCommand, layerParms);
				if(commandResult!=undefined){
					out.addRequest(commandResult.requestResult);
					if(commandResult.commandInfo!=undefined){
						layerCommand = commandResult.commandInfo.commandName;
						layerParms = commandResult.commandInfo.commandParms;
					}
				}
				else  break;   //if layer return nothing, not go to next layer
			}
			return out;
		},
		
		getAllStateData : function(){   return loc_state.getAllState();   },
		clearState : function(){   loc_state.clear();   },	
		setAllStateData : function(stateData){  loc_state.setAllState(stateData)  },
		
		getUpdateViewRequest : function(view, handlers, request){  return loc_getUpdateViewRequest(view, handlers, request); 	},
		
		getLifeCycleRequest : function(transitName, handlers, request){	 return loc_getLifeCycleRequest(transitName, handlers, request);	},
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
