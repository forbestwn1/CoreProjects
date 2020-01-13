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
	
	var loc_moduleDefInfoByRole = node_appUtility.buildModuleDefInfoByRole(loc_uiAppDef, loc_configure);
	
	var loc_getOwnerInfo = function(appDataName){	return loc_uiApp.prv_componentData.appDataOwnerInfo[appDataName]; }; 
	
	var loc_settingParentView;
	
	var loc_eventSource = node_createEventObject();
	var loc_eventListener = node_createEventObject();

	var loc_trigueEvent = function(eventName, eventData, requestInfo){loc_eventSource.triggerEvent(eventName, eventData, requestInfo); };

	var loc_updateSettingModuleStatusRequest = function(settingModule, status, handlers, request){
		return settingModule.getUpdateSystemDataRequest("module_setting", status, handlers, request);
	};
	
	//for module with application role, only one data segment for each data name
	var loc_buildApplicationModuleInfoRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var moduleDefInfo = loc_moduleDefInfoByRole[ROLE_APPLICATION];
		if(moduleDefInfo!=undefined){
			var moduleName = moduleDefInfo.name;
			var moduleDef = moduleDefInfo.moduleDef;
			var configure = moduleDefInfo.configure;
			
			var appDataNames = node_appUtility.discoverApplicationDataDependency(moduleDef);   //app names that this module depend on
			if(appDataNames.length==0){
				//no application data dependency
				out.addRequest(loc_uiApp.buildModuleInfoRequest(moduleName, [], configure, {
					success : function(request, moduleInfo){
						loc_uiApp.addModuleInfo(moduleInfo);
					}
				}));
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
						return loc_uiApp.buildModuleInfoRequest(moduleName, appDataSegs, configure, {
							success : function(request, moduleInfo){
								loc_uiApp.addModuleInfo(moduleInfo);
							}
						});
					}
				}));
			}
		}
		return out;
	};

	var loc_createSettingModuleRequest = function(dataSegmentInfo, handlers, request){
		var moduleDefInfo = loc_moduleDefInfoByRole[ROLE_SETTING];
		var moduleName = moduleDefInfo.name;
		var moduleDef = moduleDefInfo.moduleDef;
		var configure = moduleDefInfo.configure;

		var configureData = configure.getConfigureValue(); 
		configureData.root = $('<div></div>').get(0);
		var moduleInfoRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		moduleInfoRequest.addRequest(loc_uiApp.buildModuleInfoRequest(moduleName, dataSegmentInfo==undefined?undefined:[dataSegmentInfo], node_createConfigure(configureData), {
			success : function(request, moduleInfo){
				loc_uiApp.addModuleInfo(moduleInfo);
				return loc_updateSettingModuleStatusRequest(moduleInfo.module,
						{
							persist : dataSegmentInfo==undefined?false:dataSegmentInfo.persist,
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

	var loc_getSettingModuleAppName = function(){
		var moduleDefInfo = loc_moduleDefInfoByRole[ROLE_SETTING];
		var moduleDef = moduleDefInfo.moduleDef;
		var appDataDeps = node_appUtility.discoverApplicationDataDependency(moduleDef);
		if(appDataDeps==undefined || appDataDeps.length==0)  return;
		else return appDataDeps[0];
	};
	
	var loc_createTempSettingModuleRequest = function(handlers, request){
		return loc_createSettingModuleRequest(node_appUtility.buildAppDataSegmentInfoTemp(loc_getSettingModuleAppName(), "New Setting", loc_uiApp), handlers, request);
	};

	var loc_addNewSettingModule = function(handlers, request){
		if(loc_uiApp.getModuleInfoByRole(ROLE_SETTING).length==0){
			return loc_getNewSettingModuleRequest(handlers, request);
		}
	};
	
	var loc_getNewSettingModuleRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_createTempSettingModuleRequest({
			success : function(request, moduleInfo){
				$(moduleInfo.root).prependTo(loc_settingParentView);
				return moduleInfo;
			}
		}));
		return out;
	};
	
	var loc_createSettingRoleRequest = function(handlers, request){
		var settingsRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var moduleDefInfo = loc_moduleDefInfoByRole[ROLE_SETTING];
		if(moduleDefInfo!=undefined){
			
			loc_settingParentView = loc_moduleDefInfoByRole[ROLE_SETTING].configure.getConfigureValue().root;
			
			var moduleDef = moduleDefInfo.moduleDef;
			var appDataName = loc_getSettingModuleAppName(); 
			
			settingsRequest.addRequest(loc_appDataService.getGetAppDataRequest(node_appUtility.getAppDataInfoByDataName(appDataName, loc_uiApp), {
				success : function(request, appDataContainer){
					var settingRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);

					//first one is not persistent
					settingRequest.addRequest(loc_createTempSettingModuleRequest({
						success : function(request, moduleInfo){
							$(moduleInfo.root).appendTo(loc_settingParentView);
							return moduleInfo;
						}
					}));

					var appDataSegmentInfos = appDataContainer.getAppDataSegmentInfoArray(node_appUtility.getOwnerInfoByDataName(appDataName, loc_uiApp), appDataName);
					_.each(appDataSegmentInfos, function(dataSegmentInfo, index){
						settingRequest.addRequest(loc_createSettingModuleRequest(dataSegmentInfo, {
							success : function(request, moduleInfo){
								$(moduleInfo.root).appendTo(loc_settingParentView);
								return moduleInfo;
							}
						}));
					});
					return settingRequest;
				}
			}));
		}
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
			}, undefined, request);
			
			if(out!=undefined)		node_requestServiceProcessor.processRequest(out);
		},	
			
		processComponentCoreEvent : function(eventName, eventData, request){
			if(eventName=="nosliw_module_setting_submitSetting"){
				var moduleInfo = loc_getModuleInfoByEventInfo(eventData);
				loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, moduleInfo.id);
				var processRequest = loc_gate.getExecuteProcessResourceRequest("@submitsetting;applicationsetting", undefined, undefined, request);
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
					loc_gate.processRequest(loc_gate.getExecuteProcessRequest(eventHandler, extraInput, undefined, request));
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
				
				modulesRequest.addRequest(loc_buildApplicationModuleInfoRequest());
				modulesRequest.addRequest(loc_createSettingRoleRequest());
				
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
