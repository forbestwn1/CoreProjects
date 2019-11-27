//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createIODataSet;
	var node_createDynamicIOData;
	var node_ModuleInfo;
	var node_ServiceInfo;
	var node_createConfigure;
	var node_getComponentLifecycleInterface;
	var node_createEventObject;
	var node_requestServiceProcessor;
	var node_appUtility;
	var node_createAppDataInfo;
	var node_createServiceRequestInfoSimple;
	var node_createEventSource;
	var node_createEventInfo;
	var node_eventUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_createAppDecoration = function(gate){

	var ROLE_APPLICATION = "application";
	var ROLE_SETTING = "setting";
	
	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponentCore();
	var loc_uiAppDef = loc_uiApp.prv_componentData.componentDef;
	var loc_configure = loc_gate.getConfigure();
	var loc_configureData = loc_configure.getConfigureValue();
	var loc_appDataService = loc_configureData.__appDataService;
	
	var loc_getOwnerInfo = function(appDataName){	return loc_uiApp.prv_componentData.appDataOwnerInfo[appDataName]; }; 
	
	var loc_getModuleConfigure = function(role){	return loc_configure.getChildConfigure("modules", role);	};
	
	var loc_settingParentView = loc_getModuleConfigure(ROLE_SETTING).getConfigureValue().root;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_updateSettingModuleStatusRequest = function(settingModule, status, handlers, request){
		return settingModule.getUpdateSystemDataRequest("module_setting", status, handlers, request);
	};
	
	//for module with application role, only one data segment for each data name
	var loc_buildApplicationModuleInfoRequest = function(moduleName, moduleDef, configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var appDataNames = node_appUtility.discoverApplicationDataDependency(moduleDef);   //app names that this module depend on
		if(appDataNames.length==0){
			//no application data dependency
			out.addRequest(loc_uiApp.buildModuleInfoRequest(moduleName, [], configure));
		}
		else{
			//get from database first by data name
			var appDataInfos = [];
			_.each(appDataNames, function(dataName, i){
				appDataInfos.push(node_appUtility.getAppDataInfoByDataName(dataName, loc_uiApp));
			});

			out.addRequest(loc_appDataService.getGetAppDataRequest(appDataInfos, {
				success : function(request, appDataInfoContainer){
					//app data infos
					var appDataSegs = [];
					_.each(appDataNames, function(dataName, i){
						var segs = appDataInfoContainer.getAppDataSegmentInfoArray(node_appUtility.getOwnerInfoByDataName(dataName, loc_uiApp), dataName);
						if(segs==undefined || segs.length==0){
							//no data in database, then generate one 
							appDataSegs.push(node_appUtility.buildAppDataSegmentInfoTemp(dataName, "", loc_uiApp));
						}
						else{
							appDataSegs.push(segs[0]);
						}
					});
					return loc_uiApp.buildModuleInfoRequest(moduleName, appDataSegs, configure);
				}
			}));
		}
		return out;
	};

	var loc_createSettingModuleRequest = function(moduleName, moduleDef, dataSegmentInfo, configure, handlers, request){
		var configureData = configure.getConfigureValue(); 
		configureData.root = $('<div></div>').get(0);
		$(configureData.root).appendTo(loc_settingParentView);
		var moduleInfoRequest = node_createServiceRequestInfoSequence();
		moduleInfoRequest.addRequest(loc_uiApp.buildModuleInfoRequest(moduleName, dataSegmentInfo==undefined?undefined:[dataSegmentInfo], node_createConfigure(configureData), {
			success : function(request, moduleInfo){
				return loc_updateSettingModuleStatusRequest(moduleInfo.module,
						{
							persist : dataSegmentInfo==undefined?false:dataSegmentInfo.persist,
							modified : false,
							name : moduleInfo.name
						}, {
							success : function(request){
								return moduleInfo;
							}
						});
			}
		}));
		return moduleInfoRequest;
	};
	
	var loc_createSettingRoleRequest = function(moduleName, moduleDef, configure, handlers, request){
		var settingsRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var appDataName = node_appUtility.discoverApplicationDataDependency(moduleDef)[0];
		
		settingsRequest.addRequest(loc_appDataService.getGetAppDataRequest(node_appUtility.getAppDataInfoByDataName(appDataName), {
			success : function(request, appDataContainer){
				var settingRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);

				//first one is not persistent
				settingRequest.addRequest(loc_createSettingModuleRequest(moduleName, moduleDef, node_appUtility.buildAppDataSegmentInfoTemp(appDataName, "New Setting", loc_uiApp), configure));

				var appDataSegmentInfos = appDataContainer.getAppDataSegmentInfoArray(node_appUtility.getOwnerInfoByDataName(appDataName), appDataName);
				_.each(appDataSegmentInfos, function(dataSegmentInfo, index){
					settingRequest.addRequest(loc_createSettingModuleRequest(moduleName, moduleDef, dataSegmentInfo, configure));
				});
				return settingRequest;
			}
		}));
		return settingsRequest;
	};
	
	var loc_getModuleInfoByEventInfo = function(eventInfo){
		var moduleSource = eventInfo.getSourceByType(node_CONSTANT.TYPEDOBJECT_TYPE_APPMODULE);
		return loc_uiApp.getModuleInfoById(moduleSource.getId());
	};
	
	var loc_out = {
		
		processComponentCoreValueChangeEvent : function(eventName, eventData, request){
			var out = loc_updateSettingModuleStatusRequest(eventData.moduleInfo.module, {
				modified : true
			}, request);
			
			if(out!=undefined)		node_requestServiceProcessor.processRequest(out);
		},	
			
		processComponentCoreEvent : function(eventName, eventData, request){
			if(eventName=="nosliw_module_setting_submitSetting"){
				var moduleInfo = loc_getModuleInfoByEventInfo(eventData);
				loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, moduleInfo.id);
				var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
				node_requestServiceProcessor.processRequest(processRequest);
			}
			else if(eventName=="nosliw_module_setting_deleteSetting"){
				var moduleInfo = loc_getModuleInfoByEventInfo(eventData);
				var applicationDataInfo = moduleInfo.applicationDataInfo[0];
				
				node_requestServiceProcessor.processRequest(loc_appDataService.getDeleteAppDataSegmentRequest(loc_getOwnerInfo(applicationDataInfo.dataName), applicationDataInfo.dataName, applicationDataInfo.id, {
					success : function(request){
						loc_uiApp.removeModuleInfo(ROLE_SETTING, moduleInfo.id);
						moduleInfo.root.remove();
					}
				}, request));
				
			}
			else if(eventName=="nosliw_module_setting_saveSetting"){
				var moduleInfo = loc_getModuleInfoByEventInfo(eventData);
				var dataInfo = moduleInfo.applicationDataInfo[0];
				var outRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
				var saveRequest = moduleInfo.outputMapping["persistance"].getExecuteCommandRequest("execute", undefined, {
					success : function(request){
						return loc_updateSettingModuleStatusRequest(moduleInfo.module, 
							{
								persist : dataInfo.persist,
								modified : false,
							});
					}
				});
				outRequest.addRequest(saveRequest);
				node_requestServiceProcessor.processRequest(outRequest);
			}
			else{
				var eventHandler = loc_gate.getComponentCore().getEventHandler(loc_getModuleInfoByEventInfo(eventData).name, eventName);
				//if within module, defined the process for this event
				if(eventHandler!=undefined){
					var extraInput = {
						public : {
							EVENT : {
								event : eventName,
								data : eventData.getEventData()
							} 
						}
					};
					loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler[node_COMMONATRIBUTECONSTANT.EXECUTABLEEVENTHANDLER_PROCESS], extraInput, undefined, request));
				}
			}
		},
			
		processComponentCoreEvent1 : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.APP_EVENT_MODULEEVENT){
				if(eventData.eventName=="nosliw_module_setting_submitSetting"){
					loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, eventData.moduleInfo.id);
					var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(processRequest);
				}
				else if(eventData.eventName=="nosliw_module_setting_deleteSetting"){
					var moduleInfo = eventData.moduleInfo;
					var applicationDataInfo = moduleInfo.applicationDataInfo[0];
					
					node_requestServiceProcessor.processRequest(loc_appDataService.getDeleteAppDataSegmentRequest(node_appUtility.getCurrentOwnerInfo(), applicationDataInfo.dataName, applicationDataInfo.id, {
						success : function(request){
							loc_uiApp.removeModuleInfo(ROLE_SETTING, moduleInfo.id);
							moduleInfo.root.remove();
						}
					}, request));
					
				}
				else if(eventData.eventName=="nosliw_module_setting_saveSetting"){
					var moduleInfo = eventData.moduleInfo;
					var dataInfo = moduleInfo.applicationDataInfo[0];
					var outRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
					var saveRequest = moduleInfo.outputMapping["persistance"].getExecuteCommandRequest("execute", undefined, {
						success : function(request){
							return loc_updateSettingModuleStatusRequest(moduleInfo.module, 
								{
									persist : dataInfo.persist,
									modified : false,
								});
						}
					});
					outRequest.addRequest(saveRequest);
					node_requestServiceProcessor.processRequest(outRequest);
				}
				else{
					var eventHandler = loc_gate.getComponentCore().getEventHandler(eventData.moduleInfo.name, eventData.eventData.eventName);
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
		
		getLifeCycleRequest : function(transitName, handlers, request){
			var out;
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_ACTIVE){
				out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				var modulesRequest = node_createServiceRequestInfoSequence(undefined, {
					success : function(request){
						var modulesStartRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
						var allModules = loc_uiApp.getAllModuleInfo();
						_.each(allModules, function(moduleInfo){
							modulesStartRequest.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_ACTIVATE));
						});
						return modulesStartRequest;
					}
				});
				var modules = loc_uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE];
				_.each(modules, function(moduleDef, name){
					var role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
					if(role==ROLE_APPLICATION){
						modulesRequest.addRequest(loc_buildApplicationModuleInfoRequest(name, moduleDef, loc_getModuleConfigure(role)));
					}
					else if(role==ROLE_SETTING){
						modulesRequest.addRequest(loc_createSettingRoleRequest(name, moduleDef, loc_getModuleConfigure(role)));
					}
				});
				
				out.addRequest(modulesRequest);
			}
			else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				var moduleInfos = loc_uiApp.getAllModuleInfo();
				_.each(moduleInfos, function(moduleInfo, index){
					out.addRequest(node_getComponentLifecycleInterface(moduleInfo.module).getTransitRequest(node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_COMMAND_DESTROY));
				});

				out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
					loc_uiApp.clearModuleInfo();
				}));
			}
			return out;
		},
	};
	
	return loc_out;
};
	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.utility", function(){node_appUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDataInfo", function(){node_createAppDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.event.createEventSource", function(){node_createEventSource = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventInfo", function(){node_createEventInfo = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.utility", function(){node_eventUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
