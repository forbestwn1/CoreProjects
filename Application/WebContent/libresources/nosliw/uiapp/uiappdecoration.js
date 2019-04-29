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
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createAppDecoration = function(gate){

	var ROLE_APPLICATION = "application";
	var ROLE_SETTING = "setting";
	
	var loc_gate = gate;
	var loc_uiApp = loc_gate.getComponent();
	var loc_uiAppDef = loc_uiApp.prv_app.appDef;
	var loc_configureData = loc_gate.getConfigureData();
	
	var loc_createModuleOutputMapping = function(moduleRuntime, moduleDef){
		var outputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
		var out = {};
		_.each(outputMappings, function(mapping, name){
			out[name] = node_createDataAssociation(moduleRuntime.getModule().getIOContext(), mapping, loc_ioContext);
		});
		return out;
	};

	var loc_createModuleInputMapping = function(moduleDef){
		var inputMappings = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
		var out = {};
		_.each(inputMappings, function(mapping, name){
			out[name] = node_createDataAssociation(loc_uiApp.getIOContext(), mapping);
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
	
	var loc_createApplicationModuleRequest = function(module, configureData, handlers, request){
		var moduleInfo = new node_ModuleInfo(ROLE_APPLICATION);
		moduleInfo.inputMapping = loc_createModuleInputMapping(module);
		moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
		
		var moduleId = loc_uiApp.getId()+"."+ROLE_APPLICATION;
		return nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleId, module[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], loc_getModuleConfigureData(ROLE_APPLICATION), loc_buildMoudleInputIO(moduleInfo), {
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
//					out.addRequest(loc_createSettingsModuleRequest(module, appStatelessData.nodes[role], appStatelessData, decorations, moduleConfigure.moduleEnvFactoryId));
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

//Register Node by Name
packageObj.createChildNode("createAppDecoration", node_createAppDecoration); 

})(packageObj);
