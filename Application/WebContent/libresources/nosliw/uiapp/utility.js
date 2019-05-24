//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_createDynamicData;
	var node_createServiceRequestInfoSequence;
	var node_getComponentInterface;
	var node_ModuleInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){

	var loc_appDataPrefex = "applicationData_";

	var loc_out = {
		
		//find which application data this module depend on
		getApplicationDataName : function(moduleDef){
			var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
			for(var i in dataDependency){
				var dataName = dataDependency[i];
				if(dataName.startsWith(loc_appDataPrefex)){
					return dataName.substring(loc_appDataPrefex.length);
				}
			}
		},
			
		getApplicationDataIOName : function(appDataName){
			return loc_appDataPrefex+appDataName;
		},

		buildModuleInfoRequest : function(moduleDef, uiApp, applicationDataInfo, configureData, appDataService, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var moduleInfo = new node_ModuleInfo(moduleDef);
			moduleInfo.root = configureData.root;
			moduleInfo.id = uiApp.getId()+"."+nosliw.generateId();
			if(applicationDataInfo!=undefined) moduleInfo.applicationDataInfo = applicationDataInfo;
			
			loc_out.buildModuleExternalIO(uiApp.getIOContext(), moduleInfo, appDataService);
			loc_out.buildModuleInputMapping(moduleInfo);
			moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
			loc_out.buildMoudleInputIO(moduleInfo);
			
			if(applicationDataInfo!=undefined && applicationDataInfo.length==1){
				moduleInfo.name = applicationDataInfo[0].version;
			}
			
			out.addRequest(nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleInfo.id, moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], configureData, moduleInfo.inputIO, {
				success : function(requestInfo, uiModuleRuntime){
					moduleInfo.module = uiModuleRuntime;
					loc_out.buildModuleOutputMapping(moduleInfo);
					moduleInfo = uiApp.addModuleInfo(moduleInfo);
					return moduleInfo;
				}
			}));
			return out;
		},
		
		buildModuleExternalIO : function(appIOContext, moduleInfo, appDataService){
			var externalDataIO = node_createIODataSet();
			externalDataIO.setData(undefined, appIOContext.generateDataEle());
			
			_.each(moduleInfo.applicationDataInfo, function(appDataInfo, index){
				var dataIOName = loc_out.getApplicationDataIOName(appDataInfo.dataName);
				externalDataIO.setData(dataIOName, node_createDynamicData(
					function(handlers, request){
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getGetAppDataByIdRequest(appDataInfo.dataName, appDataInfo.id, {
							success : function(request, dataInfo){
								return dataInfo.data;
							}
						}));
						return out;
					},
					function(value, handlers, request){
						return appDataService.getUpdateAppDataRequest(appDataInfo.dataName, appDataInfo.id, value, handlers, request);
					}
				));
			});
			moduleInfo.externalIO = externalDataIO;
		},

		buildMoudleInputIO : function(moduleInfo){
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
			moduleInfo.inputIO = out;
		},
		
		buildModuleInputMapping : function(moduleInfo){
			var inputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
			var out = {};
			_.each(inputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(moduleInfo.externalIO, mapping);
			});
			moduleInfo.inputMapping = out;
		},
		
		buildModuleOutputMapping : function(moduleInfo){
			var outputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
			var out = {};
			var comInterface = node_getComponentInterface(moduleInfo.module);
			_.each(outputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(comInterface.getIOContext(), mapping, moduleInfo.externalIO);
			});
			moduleInfo.outputMapping = out;
			return moduleInfo;
		},
	};
	
	return loc_out;
		
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.createDataAssociation", function(){node_createDataAssociation = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createIODataSet", function(){node_createIODataSet = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicData = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
