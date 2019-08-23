//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	

var node_buildPlugInObject = function(rawPluginObj){
	var loc_rawPluginObj = rawPluginObj;
	
	var loc_out = {

		getInterface : function(){   return loc_rawPluginObj.getInterface==undefined?undefined:loc_rawPluginObj.getInterface();  },
		
		processComponentEvent : function(eventName, eventData, request){
			return loc_rawPluginObj.processComponentEvent==undefined? undefined:loc_rawPluginObj.processComponentEvent(eventName, eventData, request);
		},
		
		processComponentValueChangeEvent : function(eventName, eventData, request){
			return loc_rawPluginObj.processComponentValueChangeEvent==undefined? undefined:loc_rawPluginObj.processComponentValueChangeEvent(eventName, eventData, request);
		},
			
		getExecuteCommandRequest : function(command, parms, handlers, request){
			return loc_rawPluginObj.getExecuteCommandRequest==undefined? undefined:loc_rawPluginObj.getExecuteCommandRequest(command, parms, handlers, request);
		},
		
		getUpdateViewRequest : function(view, handlers, request){
			if(loc_rawPluginObj.getUpdateViewRequest!=undefined){
				return loc_rawPluginObj.getUpdateViewRequest(view, handlers, request);
			}
			else{
				//callback not defined, then return view 
				return node_createServiceRequestInfoSimple(undefined, function(request){
					return view;
				}, handlers, request);
			}
		},
		
		getLifeCycleRequest : function(transitName, handlers, request){	return loc_rawPluginObj.getLifeCycleRequest==undefined?undefined:loc_rawPluginObj.getLifeCycleRequest(transitName, handlers, request);},
	};
	
	return loc_out;
};
	
//component decoration
// id : decoration id
// baseLayer : layer underneath this decoration, it maybe component or another decoration
// pluginGenerator: function to generate plugin object, plugin object defined the logic
// porcessEnv : process environment object, decoration may contribute to it
// configureData : configuration data for this decoration
// state : state data
var node_createComponentDecoration = function(id, component, pluginGenerator, processEnv, configureData, state){
	
	var loc_id = id;
	var loc_configureData = configureData;
	var loc_state = state;
	var loc_processEnv = processEnv;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_component = component;
	
	//generate plug in object 
	var loc_plugin = node_buildPlugInObject(pluginGenerator({
		
		getComponent : function(){   return loc_component;		},
		
		getConfigureData : function(){  return loc_configureData;	},
		
		getStateValue : function(name){  return loc_state.getStateValue(loc_id, name);	},
		
		getState : function(){  return loc_state.getState(loc_id);   },
		
		setStateValue : function(name, value){  loc_state.setStateValue(loc_id, name, value);	},
		
		processRequest : function(request){   	node_requestServiceProcessor.processRequest(request);  },
		
		getExecuteProcessRequest : function(process, extraInput, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteEmbededProcessRequest(process, this.getComponent().getIOContext(), extraInput, handlers, request);
		},

		getExecuteProcessResourceRequest : function(processId, input, handlers, request){
			return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_processEnv).getExecuteProcessResourceRequest(processId, input, handlers, request);
		},
		
		trigueEvent : function(eventName, eventData, requestInfo){  loc_trigueEvent(eventName, eventData, requestInfo);	}
	}));
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		processComponentEvent : function(eventName, eventData, request){		return loc_plugin.processComponentEvent(eventName, eventData, request);		},
		processComponentValueChangeEvent : function(eventName, eventData, request){		return loc_plugin.processComponentValueChangeEvent(eventName, eventData, request);	},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){  return loc_plugin.getExecuteCommandRequest(command, parms, handlers, request);  },
		
		getInterface : function(){   return loc_plugin.getInterface();	},

		getUpdateViewRequest : function(view, handlers, request){  return loc_plugin.getUpdateViewRequest(view, handlers, request);  },		

		//component decoration lifecycle call back
		getLifeCycleRequest : function(transitName, handlers, request){  return loc_plugin.getLifeCycleRequest(transitName, handlers, request);	},
		
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComponentDecoration", node_createComponentDecoration); 

})(packageObj);
