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

var node_createComponentDecoration = function(id, child, coreGenerator, processEnv, configure, state){
	
	var loc_id = id;
	var loc_configure = configure;
	var loc_state = state;
	var loc_processEnv = processEnv;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_child = child;
	loc_child.registerEventListener(loc_eventListener, function(eventName, eventData, request){
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
	
	var loc_component = loc_child.prv_getComponent==undefined? loc_child : loc_child.prv_getComponent();
	
	var loc_core = coreGenerator({
		
		getComponent : function(){   return loc_component;		},
		
		getConfigure : function(){  return loc_configure.getConfigure(loc_id);	},
		
		getStateValue : function(name){  return loc_state.getStateValue(loc_id, name);	},
		
		getState : function(){  return loc_state.getState(loc_id);   },
		
		setStateValue : function(name, value){  loc_state.setStateValue(loc_id, name, value);	},
		
		processRequest : function(request){   	node_requestServiceProcessor.processRequest(request);  },
		
		getExecuteProcessRequest : function(process, extraInput, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessRequest(process, this.getComponent().getIOContext(), extraInput, handlers, request);
		},

	});
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		prv_getComponent : function(){
			var childType = node_getObjectType(loc_child);
			if(childType==node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION)  return loc_child.prv_getComponent();
			else return loc_child;
		},	
			
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		getExecuteCommandRequest : function(command, parms, handlers, request){
			if(loc_core.getExecuteCommandRequest!=undefined){
				var commandResult = loc_core.getExecuteCommandRequest(command, parms, handlers, request);
				if(commandResult==undefined){
					return loc_child.getExecuteCommandRequest(command, parms, handlers, request);
				}
				else{
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
					out.addRequest(commandResult.requestResult);
					if(commandResult.commandInfo!=undefined){
						out.addRequest(loc_child.getExecuteCommandRequest(command, parms));
					}
					return out;
				}
			}
			else{
				return loc_child.getExecuteCommandRequest(command, parms, handlers, request);
			}
		},

		getInterface : function(){   return loc_core.getInterface();	},
		
		getInitRequest : function(handlers, request){  return loc_core.getInitRequest==undefined?undefined:loc_core.getInitRequest(handlers, request);	}
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