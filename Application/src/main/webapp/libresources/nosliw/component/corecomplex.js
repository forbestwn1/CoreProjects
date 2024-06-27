//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createConfigure;
	var node_createComponentCoreDecoration;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_createEventObject;
	var node_buildComponentInterface;
	var node_createDecoration;
	var node_componentUtility;
	var node_getComponentInterface;
	var node_getObjectId;
	var node_getEmbededEntityInterface;
	var node_getObjectType;
	var node_makeObjectWithComponentManagementInterface;
	var node_makeObjectEntityTreeNodeInterface;
	var node_makeObjectWithEmbededEntityInterface;
	var node_makeObjectBasicEntityObjectInterface;
	var node_makeObjectWithComponentInterface;

//*******************************************   Start Node Definition  ************************************** 	
//ComponentCore complex is a structure that composed of a ComponentCore at the bottom and a list of decoration on top of it
//decoration may change the behavior of ComponentCore by event processing, command request, view appearance, exposed env interface
var node_createComponentCoreComplex = function(componentCore, decorationInfos){

	var loc_id = node_getObjectId(componentCore);

	var loc_runtimeContext;
	
	//component core and decoration layers
	var loc_layers = [];

	var loc_backupState;
	var loc_componentStates = [];

	var loc_lifecycleEntity;
	
	var loc_environmentInterface = {};
	var loc_applyEnvInterface = false;
	
	//current lifecycle status
	var loc_lifecycleStatus;

	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();


	var loc_init = function(componentCore, decorationInfos){
		var coreLayer = componentCore;
		loc_addLayer(coreLayer);	

		for(var i in decorationInfos){  
			loc_addDecoration(decorationInfos[i]);	
		}	
	};
	
	var loc_applyEnvInterfaceToCore = function(){
		if(loc_applyEnvInterface==true){
			var coreEntity = loc_getCore();
			if(loc_environmentInterface!=undefined&&coreEntity!=undefined){
				var coreEntityType = node_getObjectType(coreEntity);
				var appliedCoreEntity = coreEntity;
				if(coreEntityType==node_CONSTANT.TYPEDOBJECT_TYPE_APPLICATION||coreEntityType==node_CONSTANT.TYPEDOBJECT_TYPE_PACKAGE||coreEntityType==node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE){
					appliedCoreEntity = coreEntity.getMainEntityRuntime(); 
				}
				else{
					appliedCoreEntity = node_getEmbededEntityInterface(coreEntity); 
				}
				
				_.each(loc_environmentInterface, function(envInt, name){
					appliedCoreEntity.setEnvironmentInterface(name, envInt);
				});
			}
		}
	};

	var loc_getCurrentFacad = function(){   return loc_layers[loc_layers.length-1];  };
	
	var loc_getCore = function(){  return  loc_layers[0]; };

	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("getPreInitRequestCoreComplex", {}), handlers, request);
		_.each(loc_layers, function(layer, i){
			out.addRequest(loc_getLayerInterfaceObj(i).getPreInitRequest());
		});
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			loc_applyEnvInterface = true;
			loc_applyEnvInterfaceToCore();
		}));
		return out;
	};
	
	var loc_updateBackupStateObject = function(backupStateObj){
		_.each(loc_layers, function(layer, i){
			var layerBackupStateObj = backupStateObj.createChildState(i+"");
			if(i==0){
				//for core
				node_getComponentInterface(layer).updateBackupStateObject(layerBackupStateObj);
			}
			else{
				layer.updateBackupStateObject(layerBackupStateObj);				
			}
		});
	};

	var loc_updateLifecycleEntityObject = function(lifecycleEntityObj){
		_.each(loc_layers, function(layer, i){
			var layerLifecycleEntityObj = lifecycleEntityObj.createChild(i+""); 
			if(i==0){
				//for core
				node_getComponentInterface(layer).updateLifecycleEntityObject(layerLifecycleEntityObj);
			}
			else{
				layer.updateLifecycleEntityObject(layerLifecycleEntityObj);				
			}
		});
	};


	var loc_updateView = function(view){
		var newView = view;
		for(var i=loc_layers.length-1; i>=0; i--){
			var layer = loc_layers[i];
			var returnView;
			if(i==0){
				returnView = node_getComponentInterface(layer).updateView(newView);
			} 
			else{
				returnView = layer.updateView(newView);
			}
			if(returnView!=undefined)  newView = returnView;
		}
		return newView;
	};

	
	var loc_getPostInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PostInit", {}), handlers, request);
		_.each(loc_layers, function(layer, i){
			out.addRequest(loc_getLayerInterfaceObj(i).getPostInitRequest());
		});
		return out;
	};
	
	
	
	
	
	
	
	
	
	
	
	
	var loc_initBackState = function(backupState){
		loc_backupState = backupState;
		loc_componentStates.push(loc_createComplexLayerState(loc_componentCoreComplex.getCore(), "core"));

		_.each(loc_componentCoreComplex.getDecorations(), function(decoration, i){
			loc_componentStates.push(loc_createComplexLayerState(decoration, "dec_"+decoration.getId()));
		});
	};
	
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

	var loc_getLayerInterfaceObj = function(layerNum){
		if(layerNum==0){
			return node_getComponentInterface(loc_layers[layerNum]);
		}
		else{
			return  node_getComponentInterface(loc_layers[layerNum]);
		}
	};
	
	var loc_registerLayerEvent = function(layerNum){
		loc_getLayerInterfaceObj(layerNum).registerEventListener(loc_eventListener, function(event, eventData, requestInfo){
			var processedEvent = event;
			var processedEventData = eventData;
			var processedRequest = requestInfo;
			//when one layer trigue a event, the event will be processed by all upper layer
			//every layer may have three choice: propagate the same event, propagate different event, not propagate
			for(var i=layerNum+1; i<loc_layers.length; i++){
				var eventResult = loc_getLayerInterfaceObj(i).processComponentCoreEvent(processedEvent, processedEventData, processedRequest);
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

		loc_getLayerInterfaceObj(layerNum).registerValueChangeEventListener(loc_valueChangeEventListener, function(event, eventData, requestInfo){
			//for value change event, the event will be processed by all upper layer
			//but upper layer won't modify the event
			for(var i=layerNum+1; i<loc_layers.length; i++){
				loc_getLayerInterfaceObj(i).processComponentCoreValueChangeEvent(event, eventData, requestInfo);
			}
			loc_valueChangeEventSource.triggerEvent(event, eventData, requestInfo);
		});
	},
	
	loc_addDecoration = function(decorationInfo){
		var decoration = node_createDecoration(decorationInfo);

		decoration = node_makeObjectWithEmbededEntityInterface(decoration);

		decoration = node_makeObjectBasicEntityObjectInterface(decoration);
		
		decoration = node_makeObjectEntityTreeNodeInterface(decoration);

		decoration = node_makeObjectWithComponentInterface(node_getObjectType(decoration), decoration, false);

		node_makeObjectWithComponentManagementInterface(decoration, decoration);
		
		var commands = {};
		commands[node_CONSTANT.INTERFACE_ENV_DECORATION_COMMAND_GETCORE] = function(){
			return loc_getCore();
		};
		node_getEmbededEntityInterface(decoration).setEnvironmentInterface(node_CONSTANT.INTERFACE_ENV_DECORATION, commands);
		
		loc_addLayer(decoration);
	};
	
	loc_addLayer = function(layer){
		loc_layers.push(layer);
		loc_registerLayerEvent(loc_layers.length-1);
	};
	
	var loc_out = {
		
		getId : function(){  return loc_id;  },
			
		getLifecycleEntity : function(){   return loc_lifecycleEntity;    },
		
		getBackupState : function(){   return loc_backupState;   },
			
		getCore : function(){   return loc_getCore();    },
			
		getDecorations : function(){  
			var out = [];
			_.each(loc_layers, function(layer, i){
				if(i!=0)  out.push(layer);
			});
			return out;
		},
		
		addDecoration : function(decorationInfo){		loc_addDecoration(decorationInfo);		},

		getPreInitRequest : function(handlers, request){	return loc_getPreInitRequest(handlers, request);	},
		
		getLifeCycleRequest : function(transitName, handlers, request){	 return loc_getLifeCycleRequest(transitName, handlers, request);	},

		getPostInitRequest : function(handlers, request){	return loc_getPostInitRequest(handlers, request);	},
		
		setEnvironmentInterface : function(name, envInterface){
			loc_environmentInterface[name] = envInterface;
			loc_applyEnvInterfaceToCore();
		},

		updateBackupStateObject : function(backupStateObj){		loc_updateBackupStateObject(backupStateObj);		},
		
		updateLifecycleEntityObject : function(lifecycleEntityObj){		loc_updateLifecycleEntityObject(lifecycleEntityObj);		},

		updateView : function(view){		return loc_updateView(view);		},
		
		
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
	
	loc_init(componentCore, decorationInfos);

	loc_out.id = nosliw.generateId();
	loc_out.dataType = node_getComponentInterface(loc_out.getCore()).getDataType();
	
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreDecoration", function(){node_createComponentCoreDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createDecoration", function(){node_createDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.getObjectId", function(){node_getObjectId = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.makeObjectEntityTreeNodeInterface", function(){node_makeObjectEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.makeObjectWithEmbededEntityInterface", function(){node_makeObjectWithEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.makeObjectBasicEntityObjectInterface", function(){node_makeObjectBasicEntityObjectInterface = this.getData();}); 
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentInterface", function(){node_makeObjectWithComponentInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentCoreComplex", node_createComponentCoreComplex); 


})(packageObj);
