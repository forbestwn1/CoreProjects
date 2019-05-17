//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_createDataAssociation;
	var node_ModuleInfo;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_getComponentLifecycleInterface;
	var node_createEventObject;
	var node_requestServiceProcessor;
	var node_appUtility;
	var node_ApplicationDataInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppDecoration = function(gate){

	var ROLE_APPLICATION = "application";
	var ROLE_SETTING = "setting";
	
	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponent();
	var loc_uiAppDef = loc_uiApp.prv_app.appDef;
	var loc_configureData = loc_gate.getConfigureData();
	var loc_appDataService = loc_configureData.__appDataService;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_createSettingRoleRequest = function(moduleDef, handlers, request){
		var settingRoots = [];
		var settingsRequest = node_createServiceRequestInfoSequence(handlers, request);
		var appDataName = node_appUtility.getApplicationDataName(moduleDef);
		settingsRequest.addRequest(loc_appDataService.getGetAppDataRequest(appDataName, {
			success : function(request, allSettings){
				var settingRequest = node_createServiceRequestInfoSequence(undefined);
				_.each(allSettings, function(settingData, index){
					var configureData = loc_getModuleConfigureData(ROLE_SETTING); 
					var root = $('<div></div>');
					root.appendTo(configureData.root);
					
					settingData.dataName = appDataName;

					configureData.root = root.get();
					
					settingRequest.addRequest(node_appUtility.buildModuleInfoRequest(moduleDef, loc_uiApp, [new node_ApplicationDataInfo(settingData.dataName, settingData.id, settingData.version)], configureData, loc_appDataService, handlers, request));
				});
				return settingRequest;
			}
		}));
		return settingsRequest;
	};
	
	var loc_getModuleConfigureData = function(role){
		return node_createConfigure(loc_configureData).getConfigureData(role);
	};
	

	var loc_out = {
		
		processComponentEvent : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.APP_EVENT_MODULEEVENT){
				if(eventData.eventData.eventName=="submitSetting"){
					var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(processRequest);
				}
			}
		},
			
		getInitRequest : function(handlers, request){
			
		},

		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var modulesRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){
					var allModules = loc_uiApp.getAllModuleInfo();
					_.each(allModules, function(moduleInfo){
						node_getComponentLifecycleInterface(moduleInfo.module).transit("activate", request);
					});
				}
			});
			var modules = loc_uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE];
			_.each(modules, function(moduleDef, name){
				var role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
				if(role==ROLE_APPLICATION){
					modulesRequest.addRequest(node_appUtility.buildModuleInfoRequest(moduleDef, loc_uiApp, [], loc_getModuleConfigureData(role), loc_appDataService));
				}
				else if(role==ROLE_SETTING){
					modulesRequest.addRequest(loc_createSettingRoleRequest(moduleDef));
				}
			});
			
			out.addRequest(modulesRequest);
			return out;
		},
			
		getInterface : function(){	},
	};
	
	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.utility", function(){node_appUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataInfo", function(){node_ApplicationDataInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
