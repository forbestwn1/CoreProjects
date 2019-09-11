//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIModuleComponentCoreRequest;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_getComponentManagementInterface;
	var node_createComponentCoreComplex;
	var node_createStateBackupService;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	var node_createEventObject;
	
//*******************************************   Start Node Definition  ************************************** 	

//runtime is the one that expose lifecycle and interface inteface
var node_createModuleRuntimeRequest = function(id, uiModuleDef, configure, moduleDecorationInfos, uiDecorationConfigure, rootView, ioInput, state, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef}), handlers, request);
	out.addRequest(node_createUIModuleComponentCoreRequest(id, uiModuleDef, uiDecorationConfigure, ioInput, {
		success : function(request, uiModuleCore){
			var runtime = loc_createModuleRuntime(uiModuleCore, configure, moduleDecorationInfos, rootView, state, request);
			return runtime.prv_getInitRequest({
				success : function(request){
					return request.getData();
				}
			}).withData(runtime);
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModuleCore, configure, componentDecorationInfos, rootView, state, request){
	
	var loc_componentCoreComplex = node_createComponentCoreComplex(configure, loc_componentEnv, state);
	var loc_state = state;

	var loc_eventListener = node_createEventObject();

	var loc_init = function(uiModuleCore, configure, componentDecorationInfos, rootView, request){
		loc_componentCoreComplex.setCore(uiModuleCore);
		loc_componentCoreComplex.addDecorations(componentDecorationInfos);
	};

	var loc_getModuleCore = function(){   return loc_componentCoreComplex.getCore();   };
	var loc_getContextIODataSet = function(){  return loc_getModuleCore().getContextIODataSet();   };
	var loc_getProcessEnv = function(){   return loc_componentCoreComplex.getInterface();    };

	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteEmbededProcessRequest(process, loc_getContextIODataSet(), extraInput, handlers, request);
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_getModuleCore().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	//from init to active
	var loc_getGoActiveRequest = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE));
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("active"));
		return out;
	};
	
	//from paused to active
	var loc_getResumeActiveRequest = function(stateData, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ResumeUIModuleRuntime", {}), undefined, request);

		loc_componentCoreComplex.setAllStateData(stateData.state);
		
		var backupContextData = stateData.context;
		_.each(backupContextData, function(contextData, name){
			out.addRequest(loc_getContextIODataSet().getSetDataValueRequest(name, contextData));
		});
		
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME));
		
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("resume"));
		return out;
	};
	
	//component lifecycle call back methods
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DestroyUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY));
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out;
		var stateData = loc_state.getStateValue(request);
		if(stateData==undefined)	out = loc_getGoActiveRequest(request);
		else	out = loc_getResumeActiveRequest(stateData, request);
		return out;
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]=
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DeactiveUIModuleRuntime", {}), undefined, request);
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE));
		loc_componentCoreComplex.clearState();
		return out;
	};	

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SuspendUIModuleRuntime", {}), undefined, request);
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("suspend"));
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND));
		
		out.addRequest(loc_getContextIODataSet().getGetDataSetValueRequest({
			success : function(request, contextDataSet){
				var backupData = {
						state : loc_componentCoreComplex.getAllStateData(),
						context : contextDataSet,
					};
				loc_state.setStateValue(backupData, request);
			}
		}));
		
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){
		loc_state.clear();
	};

	//component management interface object 
	var loc_interfaceDelegate = {
		getContextIODataSet :  function(){  return loc_getContextIODataSet();  },
		getExecuteCommandRequest : function(command, parms, handlers, request){  return loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);    },
		registerEventListener : function(listener, handler, thisContext){  return loc_componentCoreComplex.registerEventListener(listener, handler, thisContext);   },
		unregisterEventListener : function(listener){   return loc_componentCoreComplex.unregisterEventListener(listener);  },
		registerValueChangeEventListener : function(listener, handler, thisContext){   return loc_componentCoreComplex.registerValueChangeEventListener(listener, handler, thisContext);   },
		unregisterValueChangeEventListener : function(listener){   return loc_componentCoreComplex.unregisterValueChangeEventListener(listener);    }
	};
	
	var loc_componentEnv = {
		//process request
		processRequest : function(request){  node_requestServiceProcessor.processRequest(request); },
		//execute process
		getExecuteProcessRequest : function(process, extraInput, handlers, request){  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);  },
		//execute process
		getExecuteProcessResourceRequest : function(processId, input, handlers, request){  return loc_getExecuteModuleProcessByNameRequest(processId, extraInput, handlers, request);  },
	};

	var loc_out = {
		
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getUpdateViewRequest(rootView));
			out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT));
			return out;
		},

		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);	},
		
	};
	
	loc_init(uiModuleCore, configure, componentDecorationInfos, rootView, request);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback, loc_out);
	//listen to lifecycle event and update lifecycle status
	node_getComponentLifecycleInterface(loc_out).registerEventListener(loc_eventListener, function(eventName, eventData, request){
		if(eventName==node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_FINISHTRANSITION){
			loc_componentCoreComplex.setLifeCycleStatus(eventData.to);
		}
	});
	
	loc_out = node_makeObjectWithComponentManagementInterface(loc_out, loc_interfaceDelegate, loc_out);

	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleComponentCoreRequest", function(){node_createUIModuleComponentCoreRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
