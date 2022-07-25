//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_requestServiceProcessor;
	var node_buildDecorationPlugInObject;
	var node_basicUtility;
	var node_ResourceId;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createDecoration = function(decorationInfo){
	
	var loc_decorationInfo = decorationInfo;
	
	var loc_runtimeObject;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_out = {

		getPreInitRequest : function(handlers, request){
			loc_runtimeObject = nosliw.runtime.getPackageService().createPackageRuntime(new node_ResourceId(loc_decorationInfo.type, loc_decorationInfo.id), loc_decorationInfo.configure);
			return loc_runtimeObject.getPreInitRequest(handlers, request);
		},
			
		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){
			
		},
		
		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			return loc_runtimeObject.getUpdateRuntimeContextRequest(runtimeContext, handlers, request);
		},
		
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },
		
	};
	
	return loc_out;
	
};
	
//component decoration
// id : decoration id
// baseLayer : layer underneath this decoration, it maybe component or another decoration
// decorationResource: function to generate plugin object, plugin object defined the logic
// componentEnv : component environment that decoration communicate with component env
// configureData : configuration data for this decoration
var node_createComponentCoreDecoration = function(id, componentCore, decorationResource, componentEnv, configure){
	
	var loc_id = id;
	var loc_configure = configure;
	var loc_componentEnv = componentEnv;
	
	var loc_lifecycleStatus;
	
	//temp value, not use as state
	var loc_tempValueByName = {};
	//store value that would be used as state, simple object
	var loc_stateValueByName = {};
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_valueChangeEventSource = node_createEventObject();
	var loc_valueChangeEventListener = node_createEventObject();

	var loc_componentCore = componentCore;

	//gate interface for decoration to communicate with external world
	var loc_gateForDecoration = {
		
		getComponentCore : function(){   return loc_componentCore;		},
		
		//get configure data for this decoration
		getConfigure : function(){  return loc_configure;	},

		//value by name
		getTempValue(name){  return  loc_tempValueByName[name];  },
		setTempValue(name, value){   loc_tempValueByName[name] = value;   },
		
		//state value by name
		getStateValue : function(name){  return loc_stateValueByName[name];	},
		getState : function(){  return loc_stateValueByName;   },
		setStateValue : function(name, value){  loc_stateValueByName[name] = value;	},
		setState : function(state){   loc_stateValueByName = state==undefined? {} : state;	},

		getValueFromCore : function(name){  return loc_componentCore.getValue(name); },

		getLifecycleStatus : function(){   return componentEnv.getLifecycleStatus();    },
		
		trigueEvent : function(eventName, eventData, requestInfo){  loc_trigueEvent(eventName, eventData, requestInfo);	},
		
		processRequest : function(request){   	loc_componentEnv.processRequest(request);  },
		
		getExecuteTaskRequest : function(task, extraInput, handlers, request){    return loc_componentEnv.getExecuteTaskRequest(task, extraInput, handlers, request);   },

		getExecuteProcessResourceRequest : function(processId, input, handlers, request){   return loc_componentEnv.getExecuteProcessResourceRequest(processId, input, handlers, request);    },
	};
	
	//generate plug in object 
	var loc_plugin = typeof decorationResource=='function' ?  node_buildDecorationPlugInObject(decorationResource(loc_gateForDecoration)) : decorationResource;
	
	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };
	var loc_trigueValueChangeEvent = function(eventName, eventData, requestInfo){loc_valueChangeEventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_out = {
		
		getId : function(){   return loc_id;    },
			
		registerEventListener : function(listener, handler, thisContext){	return loc_eventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterEventListener : function(listener){	return loc_eventSource.unregister(listener); },

		registerValueChangeEventListener : function(listener, handler, thisContext){	return loc_valueChangeEventSource.registerListener(undefined, listener, handler, thisContext); },
		unregisterValueChangeEventListener : function(listener){	return loc_valueChangeEventSource.unregister(listener); },

		getGetStateDataRequest : function(handlers, request){  return node_createServiceRequestInfoSimple(undefined, function(){return loc_stateValueByName;}, handlers, request);},
		getRestoreStateDataRequest : function(stateData, handlers, request){ 
			loc_stateValueByName = stateData;
			return loc_plugin.getRestoreStateDataRequest(handlers, request);
		},
		
		processComponentCoreEvent : function(eventName, eventData, request){		return loc_plugin.processComponentCoreEvent(eventName, eventData, request);		},
		processComponentCoreValueChangeEvent : function(eventName, eventData, request){		return loc_plugin.processComponentCoreValueChangeEvent(eventName, eventData, request);	},
		
		getProcessCommandRequest : function(command, parms, handlers, request){  return loc_plugin.getProcessCommandRequest(command, parms, handlers, request);  },
		
		getInterface : function(){   return loc_plugin.getInterface();	},

		getUpdateViewRequest : function(view, handlers, request){  return loc_plugin.getUpdateViewRequest(view, handlers, request);  },		

		//component decoration lifecycle call back
		getLifeCycleRequest : function(transitName, handlers, request){  return loc_plugin.getLifeCycleRequest(transitName, handlers, request);	},
		setLifeCycleStatus : function(status){   loc_lifecycleStatus = status;   },
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_COMPONENTDECORATION);
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildDecorationPlugInObject", function(){node_buildDecorationPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();	});

//Register Node by Name
//packageObj.createChildNode("createComponentCoreDecoration", node_createComponentCoreDecoration); 
packageObj.createChildNode("createDecoration", node_createDecoration); 

})(packageObj);
