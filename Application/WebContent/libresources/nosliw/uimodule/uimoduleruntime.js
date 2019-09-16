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
	
	var loc_componentCoreComplex;
	var loc_state = state;
	var loc_rootView = rootView;
	
	var loc_eventListener = node_createEventObject();

	var loc_init = function(uiModuleCore, configure, componentDecorationInfos, rootView, state, request){
		loc_componentCoreComplex = node_createComponentCoreComplex(configure, loc_componentEnv, state);
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
	
	var loc_getNormalLiefCycleCallBackRequestRequest = function(lifecycleName, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getNormalLiefCycleCallBackRequestRequest", {}), undefined, request);
		//start module 
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(lifecycleName));
		//execute process defined in module by handler name 
		out.addRequest(loc_getExecuteModuleProcessByNameRequest(lifecycleName));
		return out;
	};
	
	//component lifecycle call back methods
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ActiveUIModuleRuntime", {}), undefined, request);
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var stateData = loc_state.getStateValue(request);
			if(stateData!=undefined)  loc_getModuleCore().setValue(node_CONSTANT.COMPONENT_VALUE_BACKUP, true);    //use backup mode
			return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE, request);
		}));
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			//clear backup flag
			loc_getModuleCore().setValue(node_CONSTANT.COMPONENT_VALUE_BACKUP, undefined);
			//clear backup data
			loc_state.clear(); 
		}));
		return out;
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]= function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE, request);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND, request);
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){
		//from active to suspended
		loc_state.clear();   //remove backup state saved when into suspended
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME, request);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY, request);
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE, request);
		loc_componentCoreComplex.clearState();
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
	
	//environment for component complex
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
			out.addRequest(loc_componentCoreComplex.getUpdateViewRequest(loc_rootView, {
				success : function(request, view){
					loc_getModuleCore().setRootView(view);
				}
			}));
			out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT));
			return out;
		},

		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);	},
		
	};
	
	loc_init(uiModuleCore, configure, componentDecorationInfos, rootView, state, request);
	
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
