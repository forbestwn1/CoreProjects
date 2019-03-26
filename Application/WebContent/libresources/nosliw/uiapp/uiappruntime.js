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
	var node_ioTaskUtility;
	var node_createIODataSet;
	var node_appDataService;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createApplicationModuleRequest = function(module, root, appStatelessData, decorations, envFactoryId){
	var statelessData = {
		app : appStatelessData.app,
		root : root
	};

	return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], undefined, statelessData, decorations, envFactoryId, {
		success : function(requestInfo, uiModuleRuntime){
			uiModuleRuntime.executeStartRequest(undefined, requestInfo);			
		}
	});
};	

var loc_createSettingModuleRequest = function(data, module, settingRoots, settingPanelRoot, appStatelessData, decorations, envFactoryId, handlers, request){
	var settingRequest = node_createServiceRequestInfoSequence(undefined, handlers, request);
	var root = $('<div></div>');
	root.appendTo(settingPanelRoot);
	settingRoots.push(root);
	
	var inputSet = node_createIODataSet();
	inputSet.setData("appdata_setting", data.data);
	settingRequest.addRequest(node_ioTaskUtility.getExecuteDataAssociationRequest(inputSet, module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element.default, {
		success : function(requestInfo, outputSet){
			var moduleStatelessData ={
				root:root.get(),
				eventProcessor : function(eventName, uiName, eventData, request){
					if(eventName=="saveSetting"){
						var moduleData = moduleStatelessData.uiModule.getContext();
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
							 
						node_ioTaskUtility.executeDataAssociationToTargetRequest(moduleData, module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element.persistance, ioTarget, {
							success : function(request, result){
								var data = result.getData("appdata_setting");
								var kkkk = ioTarget.getDataSet();
								kkkk = kkkk;
							}
						});
						
					}
					else if(eventName=="submitSetting"){
						
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
	var modules = [];
	var settingsRequest = node_createServiceRequestInfoSequence(undefined);
	settingsRequest.addRequest(node_appDataService.getGetAppDataRequest("setting", {
		success : function(request, appData){
			var settingRequest = node_createServiceRequestInfoSequence(undefined);
			_.each(appData, function(data, index){
				settingRequest.addRequest(loc_createSettingModuleRequest(data, module, settingRoots, settingPanelRoot, appStatelessData, decorations, envFactoryId, {
					success : function(request, uiModuleRuntime){
						modules.push(uiModuleRuntime);
					}
				}));
			});
			return settingRequest;
		}
	}));
	return settingsRequest;
};

var node_createAppRuntimeRequest = function(uiAppDef, appConfigure, appStatelessData, handlers, request){
	var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createAppRuntime", {}), handlers, request);
	
	var modules = uiAppDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_UIMODULE];

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
nosliw.registerSetNodeDataEvent("iotask.ioTaskUtility", function(){node_ioTaskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.appDataService", function(){node_appDataService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createAppRuntimeRequest", node_createAppRuntimeRequest); 

})(packageObj);
