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
	var node_ApplicationDataSegmentInfo;
	var node_createServiceRequestInfoSimple;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppDecoration = function(gate){

	var ROLE_APPLICATION = "application";
	var ROLE_SETTING = "setting";
	
	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponent();
	var loc_uiAppDef = loc_uiApp.prv_app.appDef;
	var loc_configureData = loc_gate.getConfigureData();
	var loc_appDataService = loc_configureData.__appDataService;
	
	var loc_getModuleConfigureData = function(role){
		return node_createConfigure(loc_configureData).getConfigureData(role);
	};
	
	var loc_settingParentView = loc_getModuleConfigureData(ROLE_SETTING).global.root;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_createSettingModuleRequest = function(moduleDef, dataInfo, handlers, request){
		var configureData = loc_getModuleConfigureData(ROLE_SETTING); 
		configureData.root = $('<div></div>').get(0);
		$(configureData.root).appendTo(loc_settingParentView);
		var moduleInfoRequest = node_createServiceRequestInfoSequence();
		moduleInfoRequest.addRequest(node_appUtility.buildModuleInfoRequest(moduleDef, loc_uiApp, dataInfo==undefined?undefined:[dataInfo], configureData, loc_appDataService, {
			success : function(request, moduleInfo){
				return moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
					persist : dataInfo==undefined?false:dataInfo.persist,
					modified : false,
					name : moduleInfo.name
				}, {
					success : function(request){
						return moduleInfo;
					}
				}, request);
			}
		}));
		return moduleInfoRequest;
	};
	
	var loc_createSettingRoleRequest = function(moduleDef, handlers, request){
		var settingRoots = [];
		var settingsRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var appDataName = node_appUtility.getApplicationDataName(moduleDef);
		settingsRequest.addRequest(loc_appDataService.getGetAppDataSegmentInfoRequest(node_appUtility.getCurrentOwnerInfo(), appDataName, {
			success : function(request, settingDataInfos){
				var settingRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
				_.each(settingDataInfos, function(dataInfo, index){
					settingRequest.addRequest(loc_createSettingModuleRequest(moduleDef, dataInfo));
				});
				settingRequest.addRequest(loc_createSettingModuleRequest(moduleDef, new node_ApplicationDataSegmentInfo(node_appUtility.getCurrentOwnerInfo(), appDataName, node_appUtility.createAppDataSegmentId(), "New Setting", false)));
				return settingRequest;
			}
		}));
		return settingsRequest;
	};
	
	

	var loc_out = {
		
		processComponentValueChangeEvent : function(eventName, eventData, request){
			var out = eventData.moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
				modified : true
			}, request);
			if(out!=undefined)		node_requestServiceProcessor.processRequest(out);
		},	
			
		processComponentEvent : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.APP_EVENT_MODULEEVENT){
				if(eventData.eventName=="submitSetting"){
					loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, eventData.moduleInfo.id);
					var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(processRequest);
				}
				else if(eventData.eventName=="deleteSetting"){
					var moduleInfo = eventData.moduleInfo;
					var applicationDataInfo = moduleInfo.applicationDataInfo[0];
					
					node_requestServiceProcessor.processRequest(loc_appDataService.getDeleteAppDataSegmentRequest({}, applicationDataInfo.dataName, applicationDataInfo.id, {
						success : function(request){
							loc_uiApp.removeModuleInfo(ROLE_SETTING, moduleInfo.id);
							moduleInfo.root.remove();
						}
					}, request));
					
				}
				else if(eventData.eventName=="saveSetting"){
					var moduleInfo = eventData.moduleInfo;
//					var saveRequest = moduleInfo.module.getInterface().getComponent().getPart("outputMapping.persistance").getExecuteCommandRequest("execute", undefined, undefined, request);
					var saveRequest = moduleInfo.outputMapping["persistance"].getExecuteCommandRequest("execute", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(saveRequest);
				}
				else{
					var eventHandler = loc_gate.getComponent().getEventHandler(eventData.moduleInfo.name, eventData.eventName);
					//if within module, defined the process for this event
					if(eventHandler!=undefined){
						var extraInput = {
							public : {
								EVENT : {
									event : eventData.eventName,
									data : eventData.eventData
								} 
							}
						};
						loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEEVENTHANDLER_PROCESS], extraInput, undefined, request));
					}
				}
			}
		},
			
		getInitRequest : function(handlers, request){
			
		},

		getStartRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var modulesRequest = node_createServiceRequestInfoSequence(undefined, {
				success : function(request){
					var modulesStartRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
					var allModules = loc_uiApp.getAllModuleInfo();
					_.each(allModules, function(moduleInfo){
						modulesStartRequest.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest("activate"));
					});
					return modulesStartRequest;
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
			
		getDeactiveRequest :function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

			var moduleInfos = loc_uiApp.getAllModuleInfo();
			_.each(moduleInfos, function(moduleInfo, index){
				out.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest("destroy"));
			});

			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				loc_uiApp.clearModuleInfo();
			}));
			
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
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
