//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIModuleRequest;
	var node_makeObjectWithComponentLifecycle;
	var node_makeObjectWithComponentManagementInterface;
	var node_getComponentManagementInterface;
	var node_createComponentCoreComplex;
	var node_createStateBackupService;
	var node_getComponentLifecycleInterface;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

//runtime is the one that expose lifecycle and interface inteface
var node_createModuleRuntimeRequest = function(id, uiModuleDef, configure, componentDecorationInfos, rootView, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef}), handlers, request);
	out.addRequest(node_createUIModuleRequest(id, uiModuleDef, undefined, ioInput, {
		success : function(request, uiModule){
			var runtime = loc_createModuleRuntime(uiModule, configure, componentDecorationInfos, rootView, request);
			return runtime.prv_getInitRequest({
				success : function(request){
					return request.getData();
				}
			}).withData(runtime);
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(uiModule, configure, componentDecorationInfos, rootView, request){
	
	var loc_componentCoreComplex = node_createComponentCoreComplex(configure, loc_componentEnv);
	var loc_localStore = configure.getConfigureData().__storeService;
	var loc_stateBackupService = node_createStateBackupService("module", uiModule.getId(), uiModule.getVersion(), loc_localStore);

	var loc_init = function(uiModule, configure, componentDecorationInfos, rootView, request){
		loc_componentCoreComplex.setCore(uiModule);
		loc_componentCoreComplex.addDecorations(componentDecorationInfos);
	};

	var loc_getContextIODataSet = function(){  return loc_out.prv_getComponent().getIOContext();   };
	
	var loc_getProcessEnv = function(){   return loc_componentCoreComplex.getInterface();    };
	
	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteEmbededProcessRequest(process, loc_out.prv_getComponent().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_out.prv_getComponent().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	var loc_getGoActiveRequest = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE));
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("active"));
		return out;
	};
	
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
	
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("DestroyUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY));
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE] = function(request){
		var out;
		var stateData = loc_stateBackupService.getBackupData();
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
				loc_stateBackupService.saveBackupData(backupData);
			}
		}));
		
		return out;
	};
	
	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_RESUME] = function(request){
		loc_stateBackupService.clearBackupData();
	};

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
		processRequest : function(request){   },
		//execute process
		getExecuteProcessRequest : function(process, extraInput, handlers, request){    },
		//execute process
		getExecuteProcessResourceRequest : function(processId, input, handlers, request){    },
	};

	var loc_out = {
		
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentCoreComplex.getUpdateViewRequest(rootView));
			out.addRequest(loc_componentCoreComplex.getLifeCycleRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT));
			return out;
		},

		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_componentCoreComplex.getExecuteCommandRequest(command, parms, handlers, request);	},
		
		getInterface : function(){   return node_getComponentManagementInterface(loc_out);  },
		
	};
	
	loc_init(uiModule, configure, componentDecorationInfos, rootView, request);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback, loc_out);
	
	loc_out = node_makeObjectWithComponentManagementInterface(loc_out, loc_interfaceDelegate, loc_out);

	loc_componentCoreComplex.getCore().setLifecycle(node_getComponentLifecycleInterface(loc_out));
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentCoreComplex", function(){node_createComponentCoreComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
