//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createConfigure;
	var node_createComponentCoreDecoration;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createEventObject;
	var node_buildComponentCore;
	var node_createDecoration;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	
//ComponentCore complex is a structure that composed of a ComponentCore at the bottom and a list of decoration on top of it
//decoration may change the behavior of ComponentCore by event processing, command request, view appearance, exposed env interface
var node_createComponentCoreComplex = function(interfaceEnv){
	
	var loc_interfaceEnv = interfaceEnv;
	
	//component core and decoration layers
	var loc_layers = [];
	//exposed interface
//	var loc_interface = {};

	var loc_backupState;
	var loc_componentStates = [];

	//current lifecycle status
	var loc_lifecycleStatus;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_getCurrentFacad = function(){   return loc_layers[loc_layers.length-1];  };
	
	var loc_getCore = function(){  return  loc_layers[0]; };

	var loc_initBackState = function(backupState){
		loc_backupState = backupState;
		loc_componentStates.push(loc_createComplexLayerState(loc_componentCoreComplex.getCore(), "core"));

		_.each(loc_componentCoreComplex.getDecorations(), function(decoration, i){
			loc_componentStates.push(loc_createComplexLayerState(decoration, "dec_"+decoration.getId()));
		});
	}
	
	var loc_createComplexLayerState = function(layer, id){
		return node_createComponentState(loc_backupState.createChildState(id), 
			function(handlers, request){  
				return layer.getGetStateDataRequest(handlers, request);
			}, 
			function(stateData, handlers, request){
				return layer.getRestoreStateDataRequest(stateData, handlers, request);
			});
	};
	
	
	var loc_getLifeCycleRequest = function(transitName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ComponentCoreComplexLifycycle", {}), handlers, request);
		//start module
		_.each(loc_layers, function(layer, i){
			if(layer.getLifeCycleRequest!=undefined) out.addRequest(layer.getLifeCycleRequest(transitName));
		});
		return out;
	};

	//process runtime context from top layer to bottom layer
	//any layer can modify runtime context (view and backup store) and return new runtime context and pass the new runtime context to next layer
	var loc_getUpdateRuntimeContextRequest = function(runtimeContext, handlers, request){
		loc_backupState = runtimeContext.backupState;
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContext", {}), handlers, request);
		var index = loc_layers.length-1;
		out.addRequest(loc_getUpdateLayerRuntimeContextRequest(index, loc_processRuntimeContext(index, runtimeContext)));
		return out;
	};
	
	var loc_processRuntimeContext = function(index, runtimeContext){
		var override = {};
		override.backupState = loc_backupState.createChildState(index+"");
		return _.extend({}, runtimeContext, override);
	};
	
	var loc_getUpdateLayerRuntimeContextRequest = function(index, runtimeContext, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateLayerRuntimeContext", {}), handlers, request);
		out.addRequest(loc_layers[index].getUpdateRuntimeContextRequest(runtimeContext, {
			success : function(request, newRuntimeContext){
				if(newRuntimeContext==undefined)   return node_createServiceRequestInfoSimple(undefined, function(request){return loc_processRuntimeContext(index, runtimeContext);});
				else if(index==0)  return node_createServiceRequestInfoSimple(undefined, function(request){return newRuntimeContext;});
				else return loc_getUpdateLayerRuntimeRequest(index-1, loc_processRuntimeContext(index, newRuntimeContext));
			}
		}));
		return out;
	};

	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInit", {}), handlers, request);
		_.each(loc_layers, function(layer){
			out.addRequest(layer.getPreInitRequest());
		});
		return out;
	};
	
	var loc_getUpdateRuntimeEnvRequest = function(runtimeEnv, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeEnv", {}), handlers, request);
		_.each(loc_layers, function(layer){
			out.addRequest(layer.getUpdateRuntimeEnvRequest(runtimeEnv));
		});
		return out;
	};

	var loc_registerLayerEvent = function(layerNum){
		loc_layers[layerNum].registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			var processedEvent = event;
			var processedEventData = eventData;
			var processedRequest = requestInfo;
			//when one layer trigue a event, the event will be processed by all upper layer
			//every layer may have three choice: propagate the same event, propagate different event, not propagate
			for(var i=layerNum+1; i<loc_layers.length; i++){
				var eventResult = loc_layers[i].processComponentCoreEvent(processedEvent, processedEventData, processedRequest);
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
		});

		loc_layers[layerNum].registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			//for value change event, the event will be processed by all upper layer
			//but upper layer won't modify the event
			for(var i=layerNum+1; i<loc_layers.length; i++){
				loc_layers[i].processComponentCoreValueChangeEvent(event, eventData, requestInfo);
			}
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	},
	
	loc_addDecoration = function(decorationInfo){
		var decoration = node_createDecoration(decorationInfo);
//		var decName = decorationInfo.name;
//		var decoration = node_createComponentCoreDecoration(decName, loc_getCore(), decorationInfo.resource, loc_runtimeEnv, decorationInfo.configure);
		loc_addLayer(decoration);
	};
	
	loc_addLayer = function(layer){
		loc_layers.push(layer);
		loc_registerLayerEvent(loc_layers.length-1);
	};
	
	var loc_out = {
		
		getCore : function(){   return loc_getCore();    },
			
		setCore : function(core){
			var coreLayer = node_buildComponentCore(core);
//			coreLayer.setRuntimeEnv(loc_runtimeEnv);
			loc_addLayer(coreLayer);	
		},
		
		getDecorations : function(){  
			var out = [];
			_.each(loc_layers, function(layer, i){
				if(i!=0)  out.push(layer);
			});
			return out;
		},
		
		addDecorations : function(decorationInfos){	
			for(var i in decorationInfos){  
				loc_out.addDecoration(decorationInfos[i]);	
			}	
		},

		addDecoration : function(decorationInfo){		loc_addDecoration(decorationInfo);		},

//		getInterface : function(){  return loc_interface;   },
		
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
				var commandResult;
				if(i>0)  commandResult = loc_layers[i].getProcessCommandRequest(layerCommand, layerParms);   //for decoration
				else commandResult = loc_layers[i].getExecuteCommandRequest(layerCommand, layerParms);   //for component core
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
		
		getPreInitRequest : function(handlers, request){	return loc_getPreInitRequest(handlers, request);	},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){		return loc_getUpdateRuntimeContextRequest(runtimeContext, handlers, request);	},

		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){   return loc_getUpdateRuntimeEnvRequest(runtimeEnv);    },

		getLifeCycleRequest : function(transitName, handlers, request){	 return loc_getLifeCycleRequest(transitName, handlers, request);	},
		setLifeCycleStatus : function(status){
			loc_lifecycleStatus = status;   
			_.each(loc_layers, function(layer, i){  
				layer.setLifeCycleStatus(status);	
			});
		},
		startLifecycleTask : function(){
			_.each(loc_layers, function(layer, i){
				if(layer.startLifecycleTask!=undefined) layer.startLifecycleTask();
			});
		},
		endLifecycleTask : function(){
			_.each(loc_layers, function(layer, i){
				if(layer.endLifecycleTask!=undefined) layer.endLifecycleTask();
			});
		},
	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreDecoration", function(){node_createComponentCoreDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentCore = this.getData();});
nosliw.registerSetNodeDataEvent("component.createDecoration", function(){node_createDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentCoreComplex", node_createComponentCoreComplex); 


})(packageObj);
