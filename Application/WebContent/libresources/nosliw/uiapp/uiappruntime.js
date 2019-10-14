//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIAppComponentCore;
	var node_createComponentCoreComplex;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_createStateBackupService;
	var node_createEventObject;
	var node_basicUtility;
		
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppRuntimeRequest = function(id, appDef, configure, componentDecorationInfos, ioInput, state, handlers, request){
	var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("createUIModule"), function(request){
		var app = node_createUIAppComponentCore(id, appDef, configure, ioInput);
		var runtime = node_createAppRuntime(app, configure, componentDecorationInfos, state, request);
		return runtime.prv_getInitRequest({
			success : function(request){
				return request.getData();
			}
		}).withData(runtime);
	}, handlers, request);
	return out;
};
	
var node_createAppRuntime = function(uiAppCore, configure, componentDecorationInfos, state, request){
	
	var loc_state = state;
	var loc_componentCoreComplex;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_init = function(uiAppCore, configure, componentDecorationInfos, state){
		loc_componentCoreComplex = node_createComponentCoreComplex(configure, loc_componentEnv, state);
		loc_componentCoreComplex.setCore(uiAppCore);
		loc_componentCoreComplex.addDecorations(componentDecorationInfos);
	};
	
	var loc_getAppCore = function(){   return loc_componentCoreComplex.getCore();   };
	var loc_getContextIODataSet = function(){  return loc_getAppCore().getContextIODataSet();   };
	var loc_getProcessEnv = function(){   return loc_componentCoreComplex.getInterface();    };
	
	var loc_getExecuteAppProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteEmbededProcessRequest(process, loc_getContextIODataSet(), extraInput, handlers, request);
	};
	
	var loc_getExecuteAppProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_getAppCore().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteAppProcessRequest(process, extraInput, handlers, request);
	};

	var loc_getProcessNameByLifecycle = function(lifecycleName){ return node_basicUtility.buildNosliwFullName(lifecycleName);	};
	
	var loc_getNormalLiefCycleCallBackRequestRequest = function(lifecycleName, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("loc_getNormalLiefCycleCallBackRequestRequest", {}), handlers, request);
		//clear backup state
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){  loc_clearBackupState(request)  }));
		//start module 
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(lifecycleName));
		//execute process defined in module by handler name 
		out.addRequest(loc_getExecuteAppProcessByNameRequest(loc_getProcessNameByLifecycle(lifecycleName)));
		return out;
	};
	
	var loc_clearBackupState = function(request){
		//clear backup flag
		loc_getAppCore().setValue(node_CONSTANT.COMPONENT_VALUE_BACKUP, undefined);
		//clear backup data
		loc_state.clear(request); 
	}
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ActiveUIModuleRuntime", {}), undefined, request);
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var stateData = loc_state.getStateValue(request);
			if(stateData!=undefined){
				loc_getAppCore().setValue(node_CONSTANT.COMPONENT_VALUE_BACKUP, true);    //use backup mode
				//only call lifecycle, not process
				return loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE, {
					success : function(request){
						loc_clearBackupState(request);
					}
				});
			}
			else{
				//normal, call both lifecycle and process
				return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE);
			}
		}));
		return out;
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE]= function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE, undefined, request);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND, undefined, request);
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){
		//from active to suspended
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME, undefined, request);
	};

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY, undefined, request);
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE] = function(request){
		return loc_getNormalLiefCycleCallBackRequestRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE_REVERSE, request);
	};	

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
		getExecuteProcessRequest : function(process, extraInput, handlers, request){  return loc_getExecuteAppProcessRequest(process, extraInput, handlers, request);  },
		//execute process
		getExecuteProcessResourceRequest : function(processId, input, handlers, request){  return loc_getExecuteAppProcessByNameRequest(processId, extraInput, handlers, request);  },
	};

	//call back when start a statemachine task
	var loc_lifecycleTaskCallback = {
		startTask : function(){		loc_componentCoreComplex.startLifecycleTask();	},
		endTask : function(){		loc_componentCoreComplex.endLifecycleTask();	}
	};

	var loc_out = {
			
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT));
			return out;
		},
	};
	
	loc_init(uiAppCore, configure, componentDecorationInfos, state);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback, loc_lifecycleTaskCallback, loc_out);
	loc_out = node_makeObjectWithComponentManagementInterface(loc_out, loc_interfaceDelegate, loc_out);

	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.createUIAppComponentCore", function(){node_createUIAppComponentCore = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);
