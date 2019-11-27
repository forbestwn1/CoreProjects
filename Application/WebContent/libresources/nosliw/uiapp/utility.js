//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createDataAssociation;
	var node_createIODataSet;
	var node_createDynamicIOData;
	var node_createServiceRequestInfoSequence;
	var node_ModuleInfo;
	var node_createAppDataInfo;
	var node_createServiceRequestInfoSimple;
	var node_dataAssociationUtility;
	var node_ApplicationDataSegmentInfo;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){

	var loc_appDataPrefex = "applicationData_";

	var loc_out = {
		
		createAppDataSegmentId : function(){	return new Date().getMilliseconds() + "";	},
			
		getOwnerInfoByDataName : function(dataName, uiApp){    return uiApp.prv_componentData.appDataOwnerInfo[dataName];    },
		getAppDataInfoByDataName : function(dataName, uiApp){    return  node_createAppDataInfo(uiApp.prv_componentData.appDataOwnerInfo[dataName], dataName);     },
		
		//find which application data this module depend on
		discoverApplicationDataDependency : function(moduleDef){
			var out = [];
			var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
			for(var i in dataDependency){
				var dataName = dataDependency[i];
				var coreName = loc_out.getApplicationDataCoreName(dataName);
				if(coreName!=undefined)    out.push(coreName);
			}
			return out;
		},
		
		buildAppDataSegmentInfoTemp : function(appDataName, dataVersion, uiApp){
			return new node_ApplicationDataSegmentInfo(loc_out.getOwnerInfoByDataName(appDataName, uiApp), appDataName, loc_out.createAppDataSegmentId(), dataVersion, false);
		},
		
		getApplicationDataCoreName : function(appDataName){
			if(appDataName.startsWith(loc_appDataPrefex)){
				return appDataName.substring(loc_appDataPrefex.length);
			}
		},
		
		//build application data name
		buildApplicationDataName : function(appDataName){	return loc_appDataPrefex+appDataName;	},

		buildModuleInfoRequest : function(moduleDef, uiApp, applicationDataInfo, configure, appDataService, state, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var configureData = configure.getConfigureValue();
			var moduleInfo = new node_ModuleInfo(moduleDef);
			moduleInfo.root = configureData.root;
			moduleInfo.id = uiApp.getId()+"."+nosliw.generateId();
			if(applicationDataInfo!=undefined) moduleInfo.applicationDataInfo = applicationDataInfo;
			
			moduleInfo.externalIO = node_createIODataSet();
			//build app context part as external io for module
			moduleInfo.externalIO.setData(undefined, uiApp.getContextIODataSet().generateIOData());
			//build application data part as external io for module
			loc_out.buildModuleExternalAppDataIO(moduleInfo, uiApp, uiApp.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_APPLICATIONDATA], appDataService);
			
			loc_out.buildModuleInputMapping(moduleInfo);
			moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
			loc_out.buildMoudleInputIO(moduleInfo);
			
//			if(applicationDataInfo!=undefined && applicationDataInfo.length==1){
//				moduleInfo.name = applicationDataInfo[0].version;
//			}
			
			out.addRequest(nosliw.runtime.getUIModuleService().getGetUIModuleRuntimeRequest(moduleInfo.id, moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_MODULE], configure, moduleInfo.inputIO, state.createChildState(moduleInfo.id), {
				success : function(requestInfo, uiModuleRuntime){
					moduleInfo.module = uiModuleRuntime;
					loc_out.buildModuleOutputMapping(moduleInfo);
					moduleInfo = uiApp.addModuleInfo(moduleInfo);
					return moduleInfo;
				}
			}));
			return out;
		},

		buildModuleExternalAppDataIO : function(moduleInfo, uiApp, appDataDefs, appDataService){
			_.each(moduleInfo.applicationDataInfo, function(appDataInfo, index){
				loc_out.buildExternalDataIOForAppDataInfo(moduleInfo.externalIO, appDataInfo, uiApp, appDataDefs[appDataInfo.dataName], appDataService);
			});
		},
		
		//build externalIO data set for external application data
		buildExternalDataIOForAppDataInfo : function(externalIO, appDataSegInfo, uiApp, appDataDef, appDataService){
			var dataIOName = loc_out.buildApplicationDataName(appDataSegInfo.dataName);
			externalIO.setData(dataIOName, node_createDynamicIOData(
				function(handlers, request){    //get value method
					if(appDataSegInfo.persist==true){
						//if persist, then got from database
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getGetAppDataSegmentRequest(loc_out.getOwnerInfoByDataName(appDataSegInfo.dataName, uiApp), appDataSegInfo.dataName, appDataSegInfo.id, {
							success : function(request, data){
								return data;
							}
						}));
						return out;
					}
					else{
						//otherwise, use default value
						return node_createServiceRequestInfoSimple(undefined, function(request){
							var out = {};
							_.each(appDataDef[node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], function(ele, name){
								out[name] = ele[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
							});
							return out;
						}, handlers, request);
					}
				},
				function(value, handlers, request){    //set value method
					if(appDataSegInfo.persist==true){
						//modify
						return appDataService.updateAppDataSegment(loc_out.getOwnerInfoByDataName(appDataSegInfo.dataName), appDataSegInfo.dataName, appDataSegInfo.id, value, handlers, request);
					}
					else{
						//new
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getAddAppDataSegmentRequest(loc_out.getOwnerInfoByDataName(appDataSegInfo.dataName), appDataSegInfo.dataName, appDataSegInfo.index, appDataSegInfo.id, value, appDataSegInfo.version, {
							success : function(request){
								appDataSegInfo.persist=true;
							}
						}));
						return out;
					}
				}
			));
		},

		//build input io for module
		//it is based on current input mapping
		buildMoudleInputIO : function(moduleInfo){
			var out = node_createIODataSet();
			var dynamicData = node_createDynamicIOData(
				function(handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					if(moduleInfo.currentInputMapping!=undefined){
						out.addRequest(moduleInfo.currentInputMapping.getExecuteRequest({
							success : function(request, dataIo){
								return dataIo.getGetDataValueRequest();
							}
						}));
					}
					return out;
				} 
			);
			out.setData(undefined, dynamicData);
			moduleInfo.inputIO = out;
		},
		
		//build all inputmapping, only one input mapping is current active
		buildModuleInputMapping : function(moduleInfo){
			var inputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_INPUTMAPPING].element;
			var out = {};
			_.each(inputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(
								moduleInfo.externalIO,
								mapping, 
								undefined, 
								node_dataAssociationUtility.buildDataAssociationName("APP", "CONTEXT", "MODULE", moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ID])
							);
			});
			moduleInfo.inputMapping = out;
		},
		
		buildModuleOutputMapping : function(moduleInfo){
			var outputMappings = moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_OUTPUTMAPPING].element;
			var out = {};
			_.each(outputMappings, function(mapping, name){
				out[name] = node_createDataAssociation(
								moduleInfo.module.getContextIODataSet(), 
								mapping, 
								moduleInfo.externalIO,
								node_dataAssociationUtility.buildDataAssociationName("MODULE", moduleInfo.moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ID], "APP", "CONTEXT")
							);
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
nosliw.registerSetNodeDataEvent("iotask.entity.createDynamicData", function(){node_createDynamicIOData = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("uiapp.ModuleInfo", function(){node_ModuleInfo = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.createAppDataInfo", function(){node_createAppDataInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("iotask.dataAssociationUtility", function(){node_dataAssociationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.ApplicationDataSegmentInfo", function(){node_ApplicationDataSegmentInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
