//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	

var node_createComponentDecoration = function(id, baseLayer, coreGenerator, processEnv, configureData, state){
	
	var loc_id = id;
	var loc_configureData = configureData;
	var loc_state = state;
	var loc_processEnv = processEnv;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_baseLayer = baseLayer;
	//process event from baseLayer
	loc_baseLayer.registerEventListener(loc_eventListener, function(eventName, eventData, request){
		if(loc_core.processComponentEvent!=undefined){
			var eventResult = loc_core.processComponentEvent(eventName, eventData, request);
			if(eventResult==true || eventResult==undefined){
				//propagate the same event
				loc_trigueEvent(eventName, eventData, request);
			}
			else if(eventResult==false){
				//not propagate
			}
			else{
				//new event 
				loc_trigueEvent(eventResult.eventName, eventResult.eventData, eventResult.request);
			}
		}
		else{
			//propagate the same event
			loc_trigueEvent(eventName, eventData, request);
		}
	});

	//process valueChangeEvent from baseLayer
	loc_baseLayer.registerValueChangeEventListener(loc_valueChangeEventListener, function(eventName, eventData, request){
		if(loc_core.processComponentValueChangeEvent!=undefined){
			loc_core.processComponentValueChangeEvent(eventName, eventData, request);
		}
		//propagate the same event
		loc_trigueValueChangeEvent(eventName, eventData, request);
	});

	var loc_component = loc_baseLayer.prv_getComponent==undefined? loc_baseLayer : loc_baseLayer.prv_getComponent();
	
	var loc_core = coreGenerator({
		
		getComponent : function(){   return loc_component;		},
		
		getConfigureData : function(){  return loc_configureData;	},
		
		getStateValue : function(name){  return loc_state.getStateValue(loc_id, name);	},
		
		getState : function(){  return loc_state.getState(loc_id);   },
		
		setStateValue : function(name, value){  loc_state.setStateValue(loc_id, name, value);	},
		
		processRequest : function(request){   	node_requestServiceProcessor.processRequest(request);  },
		
		getExecuteProcessRequest : function(process, extraInput, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessRequest(process, this.getComponent().getIOContext(), extraInput, handlers, request);
		},

		getExecuteProcessResourceRequest : function(processId, input, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessResourceRequest(processId, input, handlers, request);
		},
		
		trigueEvent : function(eventName, eventData, requestInfo){  loc_trigueEvent(eventName, eventData, requestInfo);	}
	});
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		prv_getComponent : function(){
			var childType = node_getObjectType(loc_baseLayer);
			if(childType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION)  return loc_baseLayer.prv_getComponent();
			else return loc_baseLayer;
		},	
			
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){
			if(loc_core.getExecuteCommandRequest!=undefined){
				var commandResult = loc_core.getExecuteCommandRequest(command, parms, undefined, undefined);
				if(commandResult==undefined){
					return loc_baseLayer.getExecuteCommandRequest(command, parms, handlers, request);
				}
				else{
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					out.addRequest(commandResult.requestResult);
					if(commandResult.commandInfo!=undefined){
						out.addRequest(loc_baseLayer.getExecuteCommandRequest(command, parms));
					}
					return out;
				}
			}
			else{
				return loc_baseLayer.getExecuteCommandRequest(command, parms, handlers, request);
			}
		},

		getInterface : function(){   return loc_core.getInterface();	},
		
		updateView : function(view){   
			if(loc_core.updateView==undefined)  return view;
			return loc_core.updateView(view);
		},
		
		getPreDisplayInitRequest : function(handlers, request){  return loc_core.getPreDisplayInitRequest==undefined?undefined:loc_core.getPreDisplayInitRequest(handlers, request);	},
		getInitRequest : function(handlers, request){  return loc_core.getInitRequest==undefined?undefined:loc_core.getInitRequest(handlers, request);	},
		getDeactiveRequest : function(handlers, request){  return loc_core.getDeactiveRequest==undefined?undefined:loc_core.getDeactiveRequest(handlers, request);	},
		getSuspendRequest : function(handlers, request){  return loc_core.getSuspendRequest==undefined?undefined:loc_core.getSuspendRequest(handlers, request);	},
		getResumeRequest : function(handlers, request){  return loc_core.getResumeRequest==undefined?undefined:loc_core.getResumeRequest(handlers, request);	},
		getStartRequest : function(handlers, request){  return loc_core.getStartRequest==undefined?undefined:loc_core.getStartRequest(handlers, request);	},
		getDestroyRequest : function(handlers, request){  return loc_core.getDestroyRequest==undefined?undefined:loc_core.getDestroyRequest(handlers, request);	},
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION);
	return loc_out;
	
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentDecoration", node_createComponentDecoration); 

})(packageObj);
