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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppRuntimeRequest = function(id, appDef, appDecorationFac, configure, ioInput, handlers, request){
	loc_appDefinition(appDef, ioInput);
};
	
var node_createAppRuntime = function(id, uiApp, configure, componentDecorationInfos){
	
	var loc_id = id;
	var loc_version = "1.0.0";
	var loc_componentComplex = node_createComponentComplex(configure);
	var loc_localStore = configure.getConfigureData().__storeService;
	var loc_applicationDataService = configure.getConfigureData().__appDataService;
	var loc_stateBackupService = node_createStateBackupService("app", loc_id, loc_version, loc_localStore);
	
	var loc_init = function(uiApp, configure, componentDecorationInfos){
		loc_componentComplex.addComponent(uiApp);
		loc_componentComplex.addDecorations(componentDecorationInfos);
	};
	
	var loc_getIOContext = function(){  return loc_getApp().getIOContext();   };
	
	var loc_getApp = function(){  return loc_componentComplex.getComponent();   };
	
	var loc_getProcessEnv = function(){   return loc_componentComplex.getInterface();    };
	
	var loc_getExecuteAppProcessRequest = function(process, extraInput, handlers, request){
		return nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_getProcessEnv()).getExecuteProcessRequest(process, loc_getApp().getIOContext(), extraInput, handlers, request);
	};
	
	var loc_getExecuteAppProcessByNameRequest = function(processName, extraInput, handlers, request){
		var process = loc_getApp().getProcess(processName);
		if(process!=undefined)  return loc_getExecuteAppProcessRequest(process, extraInput, handlers, request);
	};

	
	var loc_out = {
		
	};
	
	loc_init(uiApp, configure, componentDecorationInfos);
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);
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

//Register Node by Name
//packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);
