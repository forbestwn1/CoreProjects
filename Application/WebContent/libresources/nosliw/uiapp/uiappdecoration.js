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
	var node_ApplicationDataSegmentInfo;
	var node_createServiceRequestInfoSimple;
	
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
			out.addRequest(loc_appDataService.getGetAppDataSegmentInfoRequest(node_appUtility.getCurrentOwnerInfo(), appDataNames, {
				success : function(request, appDataInfosByName){
					//app data infos
					var appDatas = [];
					_.each(appDataNames, function(appDataName, index){
						var appDataInfos = appDataInfosByName[appDataName];
						if(appDataInfos==undefined || appDataInfos.length==0){
							//no data in database, then generate one 
							appDatas.push(node_appUtility.buildAppDataInfoTemp(appDataName, ""));
						}
						else{
							appDatas.push(appDataInfos[0]);
						}
					});
					return loc_uiApp.buildModuleInfoRequest(moduleName, appDatas, configure);
				}
			}));
		}
		return out;
	};

	var loc_createSettingModuleRequest = function(moduleName, moduleDef, dataInfo, configure, handlers, request){
		var configureData = configure.getConfigureValue(); 
		configureData.root = $('<div></div>').get(0);
		$(configureData.root).appendTo(loc_settingParentView);
		var moduleInfoRequest = node_createServiceRequestInfoSequence();
		moduleInfoRequest.addRequest(loc_uiApp.buildModuleInfoRequest(moduleName, dataInfo==undefined?undefined:[dataInfo], node_createConfigure(configureData), {
			success : function(request, moduleInfo){
				return loc_updateSettingModuleStatusRequest(moduleInfo.module,
						{
							persist : dataInfo==undefined?false:dataInfo.persist,
							modified : false,
							name : moduleInfo.name
						}, {
							success : function(request){
								return moduleInfo;
							}
						});
				
//				return moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
//					persist : dataInfo==undefined?false:dataInfo.persist,
//					modified : false,
//					name : moduleInfo.name
//				}, {
//					success : function(request){
//						return moduleInfo;
//					}
//				}, request);
			}
		}));
		return moduleInfoRequest;
	};
	
	var loc_createSettingRoleRequest = function(moduleName, moduleDef, configure, handlers, request){
		var settingsRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var appDataName = node_appUtility.discoverApplicationDataDependency(moduleDef)[0];
		settingsRequest.addRequest(loc_appDataService.getGetAppDataSegmentInfoRequest(node_appUtility.getCurrentOwnerInfo(), appDataName, {
			success : function(request, settingDataInfos){
				var settingRequest = node_createServiceRequestInfoSequence(undefined, undefined, request);
				//first one is not persistent
				settingRequest.addRequest(loc_createSettingModuleRequest(moduleName, moduleDef, node_appUtility.buildAppDataInfoTemp(appDataName, "New Setting"), configure));

				_.each(settingDataInfos[appDataName], function(dataInfo, index){
					settingRequest.addRequest(loc_createSettingModuleRequest(moduleName, moduleDef, dataInfo, configure));
				});
				return settingRequest;
			}
		}));
		return settingsRequest;
	};
	
	var loc_out = {
		
		processComponentCoreValueChangeEvent : function(eventName, eventData, request){
			var out = loc_updateSettingModuleStatusRequest(eventData.moduleInfo.module, {
				modified : true
			}, request);
			
//			var out = eventData.moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
//				modified : true
//			}, request);
			if(out!=undefined)		node_requestServiceProcessor.processRequest(out);
		},	
			
		processComponentCoreEvent : function(eventName, eventData, request){
			if(eventName==node_CONSTANT.APP_EVENT_MODULEEVENT){
				if(eventData.eventName=="submitSetting"){
					loc_uiApp.setCurrentModuleInfo(ROLE_SETTING, eventData.moduleInfo.id);
					var processRequest = loc_gate.getExecuteProcessResourceRequest("applicationsetting;submitsetting", undefined, undefined, request);
					node_requestServiceProcessor.processRequest(processRequest);
				}
				else if(eventData.eventName=="deleteSetting"){
					var moduleInfo = eventData.moduleInfo;
					var applicationDataInfo = moduleInfo.applicationDataInfo[0];
					
					node_requestServiceProcessor.processRequest(loc_appDataService.getDeleteAppDataSegmentRequest(node_appUtility.getCurrentOwnerInfo(), applicationDataInfo.dataName, applicationDataInfo.id, {
						success : function(request){
							loc_uiApp.removeModuleInfo(ROLE_SETTING, moduleInfo.id);
							moduleInfo.root.remove();
						}
					}, request));
					
				}
				else if(eventData.eventName=="saveSetting"){
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
							
//							return moduleInfo.module.getExecuteCommandRequest("updateModuleInfo", {
//								persist : dataInfo.persist,
//								modified : false,
//							}, undefined, request);
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
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
