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

	var loc_createModuleOutputMapping = function(moduleRuntime, moduleDef){
		var outputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
		var out = {};
		_.each(outputMappings, function(mapping, name){
			out[name] = node_createDataAssociation(moduleRuntime.getModule().getIOContext(), mapping, loc_ioContext);
		});
		return out;
	};

	var loc_createModuleInputMapping = function(inputIO, moduleDef){
		var inputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
		var out = {};
		_.each(inputMappings, function(mapping, name){
			out[name] = node_createDataAssociation(inputIO, mapping);
		});
		return out;
	};

	var loc_buildMoudleInputIO = function(moduleInfo){
		var out = node_createIODataSet();
		var dynamicData = node_createDynamicData(
			function(handlers, request){
				var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
				out.addRequest(moduleInfo.currentInputMapping.getExecuteRequest({
					success : function(request, dataIo){
						return dataIo.getGetDataValueRequest();
					}
				}));
				return out;
			} 
		);
		out.setData(undefined, dynamicData);
		return out;
	};

	var loc_getApplicationDataName = function(moduleDef){
		var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
		for(var i in dataDependency){
			var dataName = dataDependency[i];
			var appDataPrefex = "applicationData_";
			if(dataName.startsWith(appDataPrefex)){
				return dataName.substring(appDataPrefex.length);
			}
		}
	};

	var loc_createSettingModuleRequest = function(parentNode, settingData, moduleDef, configureData, handlers, request){
		configureData.root = parentNode;
		
		var moduleInfo = new node_ModuleInfo(ROLE_SETTING);

		var inputMappingIO = node_createIODataSet();
		var dynamicData = node_createDynamicData(
			function(handlers, request){
				return loc_uiApp.getIOContext().getGetDataValueRequest(undefined, handlers, request);
			} 
		);
		inputMappingIO.setData(undefined, dynamicData);
		inputMappingIO.setData(settingData.dataName, settingData.data);
		moduleInfo.inputMapping = loc_createModuleInputMapping(inputMappingIO, moduleDef);

		moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
		
		var moduleId = loc_uiApp.getId()+"."+ROLE_SETTING+"."+settingData.id;
		return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleId, moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], configureData, loc_buildMoudleInputIO(moduleInfo), {
			success : function(requestInfo, uiModuleRuntime){
				moduleInfo.module = uiModuleRuntime;
				moduleInfo = loc_uiApp.addModuleInfo(moduleInfo);
				
//				uiModuleRuntime.registerEventListener(undefined, function(eventName, eventData, request){
//					if(eventName=="submitSetting"){
//						loc_trigueEvent("executeProcess", "applicationsetting;submitsetting", request);
//					}
//				});
				
			}
		}, request);
	};
	
	var loc_createSettingRoleRequest = function(moduleDef, configureData, handlers, request){
		var settingRoots = [];
		var settingsRequest = node_createServiceRequestInfoSequence(undefined);
		var appDataName = loc_getApplicationDataName(moduleDef);
		settingsRequest.addRequest(loc_appDataService.getGetAppDataRequest(appDataName, {
			success : function(request, allSettings){
				var settingRequest = node_createServiceRequestInfoSequence(undefined);
				_.each(allSettings, function(settingData, index){
					var root = $('<div></div>');
					root.appendTo(configureData.root);
					
					settingData.dataName = "applicationData_"+appDataName;
					settingRequest.addRequest(loc_createSettingModuleRequest(root.get(), settingData, moduleDef, configureData));
				});
				return settingRequest;
			}
		}));
		return settingsRequest;

	};
	
	var loc_createApplicationModuleRequest = function(moduleDef, configureData, handlers, request){
		var moduleInfo = new node_ModuleInfo(ROLE_APPLICATION);
		moduleInfo.inputMapping = loc_createModuleInputMapping(loc_uiApp.getIOContext(), moduleDef);
		moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
		
		var moduleId = loc_uiApp.getId()+"."+ROLE_APPLICATION;
		return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleId, moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], configureData, loc_buildMoudleInputIO(moduleInfo), {
			success : function(requestInfo, uiModuleRuntime){
				moduleInfo.module = uiModuleRuntime;
				moduleInfo = loc_uiApp.addModuleInfo(moduleInfo);
			}
		}, request);
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
						node_getComponentLifecycleInterface(moduleInfo.module).active();
					});
				}
			});
			var modules = loc_uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE];
			_.each(modules, function(module, name){
				var role = module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
				var moduleConfigureData = loc_getModuleConfigureData(role); 
				if(role==ROLE_APPLICATION){
					modulesRequest.addRequest(loc_createApplicationModuleRequest(module, moduleConfigureData));
				}
				else if(role==ROLE_SETTING){
					modulesRequest.addRequest(loc_createSettingRoleRequest(module, moduleConfigureData));
				}
			});
			
			out.addRequest(modulesRequest);
			return out;
		},
			
		getInterface : function(){
			
		},
		
		registerEventListener(){
			
		}
			
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

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
