//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_createUIModuleRequest;
	var node_createState;
	var node_createComponentDecoration;
	var node_makeObjectWithComponentLifecycle;
	var node_createComponentComplex;
	var node_createStateBackupService;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createModuleRuntimeRequest = function(id, uiModuleDef, configure, componentDecorationInfos, ioInput, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createModuleRuntime", {"moduleDef":uiModuleDef}), handlers, request);
	out.addRequest(node_createUIModuleRequest(uiModuleDef, undefined, ioInput, {
		success : function(request, uiModule){
			var runtime = loc_createModuleRuntime(id, uiModule, configure, componentDecorationInfos);
			return runtime.prv_getInitRequest({
				success : function(request){
					return request.getData();
				}
			}).withData(runtime);
		}
	}));
	return out;
};

var loc_createModuleRuntime = function(id, uiModule, configure, componentDecorationInfos){
	
	var loc_id = id;
	var loc_version = "1.0.0";
	var loc_moduleComplex = [];
	var loc_componentComplex = node_createComponentComplex(configure);
	var loc_localStore = configure.getConfigureData().__store;
	var loc_stateBackupService = node_createStateBackupService("module", loc_id, loc_version, loc_localStore);

	var loc_init = function(uiModule, configure, componentDecorationInfos){
		loc_componentComplex.addComponent(uiModule);
		
		loc_componentComplex.addDecorations(componentDecorationInfos);

//		loc_getCurrentModuleFacad().registerEventListener(undefined, function(eventName, eventData, request){});
	};
/*
	var loc_getStateData = function(){  
		var storeData = loc_localStore.retrieveData("module", loc_id);
		if(storeData==undefined)   return;
		if(storeData.version!=loc_version){
			loc_clearStateData();
			return;
		}
		return storeData.data;
	};
	var loc_saveStateData = function(stateData){
		var storeData = {
			version : loc_version,
			data : stateData
		};
		loc_localStore.saveData("module", loc_id, storeData);  
	};
	var loc_clearStateData = function(){  
		loc_localStore.clearData("module", loc_id);  
	};
	*/
	var loc_getIOContext = function(){  return loc_getModule().getIOContext();   };
	
	var loc_getModule = function(){  return loc_componentComplex.getComponent();   };
	
	var loc_getProcessEnv = function(){   return loc_componentComplex.getInterface();    };
	
	var loc_getExecuteModuleProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteProcessRequest(process, loc_getModule().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteModuleProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_getModule().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteModuleProcessRequest(process, extraInput, handlers, request);
	};
	
	var loc_getGoActiveRequest = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("StartUIModuleRuntime", {}), undefined, request);
		//start module
		out.addRequest(loc_componentComplex.getStartRequest());
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("active"));
		return out;
	};
	
	var loc_getResumeActiveRequest = function(stateData, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ResumeUIModuleRuntime", {}), undefined, request);

		loc_componentComplex.setAllStateData(stateData.state);
		
		var backupContextData = stateData.context;
		_.each(backupContextData, function(contextData, name){
			out.addRequest(loc_getIOContext().getSetDataValueRequest(name, contextData, true));
		});
		
		out.addRequest(loc_componentComplex.getResumeRequest());
		
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("resume"));
		return out;
	};
	
	
	var lifecycleCallback = {};
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
		out.addRequest(loc_componentComplex.getDeactiveRequest());
		loc_componentComplex.clearState();
		return out;
	};	

	lifecycleCallback[node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_SUSPEND] = function(request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("SuspendUIModuleRuntime", {}), undefined, request);
		out.addRequest(loc_getExecuteModuleProcessByNameRequest("suspend"));
		out.addRequest(loc_componentComplex.getSuspendRequest());
		
		out.addRequest(loc_getIOContext().getGetDataSetValueRequest({
			success : function(request, contextDataSet){
				var backupData = {
						state : loc_componentComplex.getAllStateData(),
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

	var loc_out = {
		
		prv_getInitRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitUIModuleRuntime", {}), handlers, request);
			out.addRequest(loc_componentComplex.getInitRequest());
			return out;
		},
			
		getModule : function(){  return loc_getModule();  },

		registerEventListener : function(listener, handler, thisContext){	return loc_getCurrentModuleFacad().registerEventListener(listener, handler, thisContext);	},
		
		getExecuteCommandRequest : function(command, parms, handlers, request){	return loc_getCurrentModuleFacad().getExecuteCommandRequest(command, parms, handlers, request);	}
		
	};
	
	loc_init(uiModule, configure, componentDecorationInfos);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("component.createState", function(){node_createState = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentDecoration", function(){node_createComponentDecoration = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentLifecycle", function(){node_makeObjectWithComponentLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentComplex", function(){node_createComponentComplex = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createModuleRuntimeRequest", node_createModuleRuntimeRequest); 

})(packageObj);
