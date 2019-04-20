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
	var node_objectOperationUtility;
	var node_IOTaskResult;
	var node_NormalActivityOutput;
	var node_EndActivityOutput;
	var node_ProcessResult;
	var node_createServiceRequestInfoService;
	var node_DependentServiceRequestInfo;
	var node_requestServiceProcessor;
	var node_contextUtility;
	var node_createUIModuleRequest;
	var node_createIODataSet;
	var node_appDataService;
	var node_createDataAssociation;
	
//*******************************************   Start Node Definition  ************************************** 	
var loc_app;


var loc_ioContext;


var loc_createApplicationModuleRequest = function(module, root, appStatelessData, decorations, envFactoryId){
	var statelessData = {
		app : appStatelessData.app,
		root : root
	};

	return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], undefined, statelessData, decorations, envFactoryId, {
		success : function(requestInfo, uiModuleRuntime){
			var moduleInfo = loc_app.addModule("application", uiModuleRuntime);
			moduleInfo.outputMapping = loc_createModuleOutputMapping(uiModuleRuntime, module);
			moduleInfo.inputMapping = loc_createModuleInputMapping(uiModuleRuntime, module);
			uiModuleRuntime.executeStartRequest(undefined, requestInfo);			
		}
	});
};	

var loc_createModuleOutputMapping = function(moduleRuntime, moduleDef){
	var outputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
	var out = {};
	_.each(outputMappings, function(mapping, name){
		out[name] = node_createDataAssociation(moduleRuntime.getModule().getIOContext(), mapping, loc_ioContext);
	});
	return out;
};

var loc_createModuleInputMapping = function(moduleRuntime, moduleDef){
	var inputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
	var out = {};
	_.each(inputMappings, function(mapping, name){
		out[name] = node_createDataAssociation(loc_ioContext, mapping, moduleRuntime.getModule().getIOContext());
	});
	return out;
};

var loc_createSettingModuleRequest = function(data, module, settingRoots, settingPanelRoot, appStatelessData, decorations, envFactoryId, handlers, request){
	var settingRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
	var root = $('<div></div>');
	root.appendTo(settingPanelRoot);
	settingRoots.push(root);
	
	var inputSet = node_createIODataSet();
	inputSet.setData("appdata_setting", data.data);
	
	
	
	
	settingRequest.addRequest(node_createDataAssociation(inputSet, module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element.default).getExecuteRequest( {
		success : function(requestInfo, outputSet){
			var moduleStatelessData ={
				root:root.get(),
				eventProcessor : function(eventName, uiName, eventData, request){
					if(eventName=="saveSetting"){
						var moduleData = moduleStatelessData.uiModule.getIOContext();
						var ioTarget = node_createIODataSet();
						ioTarget.setData('appdata_setting', {
							getGetValueRequest : function(handlers, request){
								var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
								out.addRequest(node_appDataService.getGetAppDataByVersionRequest("setting", data.version, {
									success : function(request, data){
										return data.data;
									}
								}));
								return out;
							},
							getSetValueRequest : function(value, handlers, request){
								return node_appDataService.getUpdateAppDataRequest("setting", data.version, {
									data : value,
									version : data.version
								});
							}
						});
				
						node_createDataAssociation(moduleData, module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element.persistance).executeRequest({
							success : function(request, result){
								var data = result.getData("appdata_setting");
								var kkkk = ioTarget.getDataSet();
								kkkk = kkkk;
							}
						}, request);
					}
					else if(eventName=="submitSetting"){
						var extraInput = {
							public : {
								EVENT : {
									event : eventName,
									data : eventData
								} 
							}
						};

						var process = loc_app.getEventProcess(eventName);
						nosliw.runtime.getProcessRuntimeFactory().createProcessRuntime(loc_env).executeProcessRequest(process, undefined, extraInput);
						
					}
				}
			}; 
			return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], outputSet.getData(), moduleStatelessData, decorations, envFactoryId, {
				success : function(requestInfo, uiModuleRuntime){
					uiModuleRuntime.executeStartRequest(undefined, requestInfo);
					uiModuleRuntime.getModule().getStatelessData().uiModule = uiModuleRuntime.getModule(); 
					return uiModuleRuntime;
				}
			});
			
		}
	}));
	return settingRequest;
};

var loc_createSettingsModuleRequest = function(module, settingPanelRoot, appStatelessData, decorations, envFactoryId){
	var settingRoots = [];
	var settingsRequest = node_createServiceRequestInfoSequence(undefined);
	settingsRequest.addRequest(node_appDataService.getGetAppDataRequest("setting", {
		success : function(request, appData){
			var settingRequest = node_createServiceRequestInfoSequence(undefined);
			_.each(appData, function(data, index){
				settingRequest.addRequest(loc_createSettingModuleRequest(data, module, settingRoots, settingPanelRoot, appStatelessData, decorations, envFactoryId, {
					success : function(request, uiModuleRuntime){
						var moduleInfo = loc_app.addModule("setting", uiModuleRuntime, request.getData().version);
						moduleInfo.outputMapping = loc_createModuleOutputMapping(uiModuleRuntime, module);
						moduleInfo.inputMapping = loc_createModuleInputMapping(uiModuleRuntime, module);
					}
				}).withData(data));
			});
			return settingRequest;
		}
	}));
	return settingsRequest;
};

var node_createAppRuntimeRequest = function(uiAppDef, appConfigure, appStatelessData, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createAppRuntime", {}), handlers, request);
	
	loc_app = loc_createApp(uiAppDef);
	loc_ioContext = node_createIODataSet();

	var modules = uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_MODULE];

	_.each(modules, function(module, name){
		var role = module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];

		var moduleConfigure = appConfigure.roleConfigure[role];
		var decorations = {};
		decorations[node_COMMONATRIBUTECONSTANT.DEFINITIONDECORATION_GLOBAL] = moduleConfigure.decorations;

		if(role=="application"){
			out.addRequest(loc_createApplicationModuleRequest(module, appStatelessData.nodes[role], appStatelessData, decorations, moduleConfigure.moduleEnvFactoryId));
		}
		else if(role=="setting"){
			out.addRequest(loc_createSettingsModuleRequest(module, appStatelessData.nodes[role], appStatelessData, decorations, moduleConfigure.moduleEnvFactoryId));
		}
	});
	
	loc_out = node_makeObjectWithComponentLifecycle(loc_out, lifecycleCallback);

	return out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityResult", function(){node_IOTaskResult = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.NormalActivityOutput", function(){node_NormalActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.EndActivityOutput", function(){node_EndActivityOutput = this.getData();	});
nosliw.registerSetNodeDataEvent("process.entity.ProcessResult", function(){node_ProcessResult = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.context.utility", function(){node_contextUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.createUIModuleRequest", function(){node_createUIModuleRequest = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.appDataService", function(){node_appDataService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppRuntimeRequest1", node_createAppRuntimeRequest); 

})(packageObj);
