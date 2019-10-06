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
	var node_createServiceRequestInfoSimple;
	var node_dataAssociationUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){

	var loc_appDataPrefex = "applicationData_";

	var loc_out = {
		
		createAppDataSegmentId : function(){
			return new Date().getMilliseconds() + "";
		},
			
		getCurrentOwnerInfo : function(){
			return nosliw.runtime.getSecurityService().getOwnerInfo();
		},
			
		//find which application data this module depend on
		getApplicationDataNames : function(moduleDef){
			var out = [];
			var dataDependency = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_DATADEPENDENCY];
			for(var i in dataDependency){
				var dataName = dataDependency[i];
				if(dataName.startsWith(loc_appDataPrefex)){
					out.push(dataName.substring(loc_appDataPrefex.length));
				}
			}
			return out;
		},
			
		getApplicationDataIOName : function(appDataName){
			return loc_appDataPrefex+appDataName;
		},

		buildApplicationModuleInfoRequest : function(moduleDef, uiApp, configureData, appDataService, handlers, request){
			var appDataNames = loc_out.getApplicationDataNames(moduleDef);
			
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(appDataNames.length==0){
				out.addRequest(loc_out.buildModuleInfoRequest(moduleDef, uiApp, [], configureData, appDataService));
			}
			else{
				out.addRequest(appDataService.getGetAppDataSegmentInfoRequest(loc_out.getCurrentOwnerInfo(), appDataNames, {
					success : function(request, appDataInfosByName){
						var appDatas = [];
						_.each(appDataNames, function(appDataName, index){
							var appDataInfos = appDataInfosByName[appDataName];
							if(appDataInfos==undefined || appDataInfos.length==0){
								appDatas.push(new node_ApplicationDataSegmentInfo(loc_out.getCurrentOwnerInfo(), appDataName, loc_out.createAppDataSegmentId(), "", false));
							}
							else{
								appDatas.push(appDataInfos[0]);
							}
						});
						return loc_out.buildModuleInfoRequest(moduleDef, uiApp, appDatas, configureData, appDataService);
					}
				}));
			}
			return out;
		},
		
		buildModuleInfoRequest : function(moduleDef, uiApp, applicationDataInfo, configureData, appDataService, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			
			var moduleInfo = new node_ModuleInfo(moduleDef);
			moduleInfo.root = configureData.root;
			moduleInfo.id = uiApp.getId()+"."+nosliw.generateId();
			if(applicationDataInfo!=undefined) moduleInfo.applicationDataInfo = applicationDataInfo;
			
			moduleInfo.externalIO = node_createIODataSet();
			moduleInfo.externalIO.setData(undefined, uiApp.getIOContext().generateIOData());
			loc_out.buildModuleExternalAppDataIO(uiApp.getIOContext(), moduleInfo, appDataService, uiApp.prv_componentData.componentDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPENTRY_APPLICATIONDATA]);
			
			loc_out.buildModuleInputMapping(moduleInfo);
			moduleInfo.currentInputMapping = moduleInfo.inputMapping[node_COMMONCONSTANT.DATAASSOCIATION_RELATEDENTITY_DEFAULT];
			loc_out.buildMoudleInputIO(moduleInfo);
			
//			if(applicationDataInfo!=undefined && applicationDataInfo.length==1){
//				moduleInfo.name = applicationDataInfo[0].version;
//			}
			
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

		buildModuleExternalAppDataIO : function(appIOContext, moduleInfo, appDataService, appDataDefs){
			_.each(moduleInfo.applicationDataInfo, function(appDataInfo, index){
				loc_out.buildExternalDataIOForAppDataInfo(moduleInfo.externalIO, appDataInfo, appDataService, appDataDefs[appDataInfo.dataName]);
			});
		},
		
		buildExternalDataIOForAppDataInfo : function(externalIO, appDataInfo, appDataService, appDef){
			var dataIOName = loc_out.getApplicationDataIOName(appDataInfo.dataName);
			externalIO.setData(dataIOName, node_createDynamicIOData(
				function(handlers, request){
					if(appDataInfo.persist==true){
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getGetAppDataSegmentByIdRequest(loc_out.getCurrentOwnerInfo(), appDataInfo.dataName, appDataInfo.id, {
							success : function(request, dataInfo){
								return dataInfo.data;
							}
						}));
						return out;
					}
					else{
						return node_createServiceRequestInfoSimple(undefined, function(request){
							var out = {};
							_.each(appDef[node_COMMONATRIBUTECONSTANT.CONTEXT_ELEMENT], function(ele, name){
								out[name] = ele[node_COMMONATRIBUTECONSTANT.CONTEXTDEFINITIONROOT_DEFAULT];
							});
							return out;
						}, handlers, request);
					}
				},
				function(value, handlers, request){
					if(appDataInfo.persist==true){
						//modify
						return appDataService.getUpdateAppDataSegmentRequest(appDataInfo.ownerInfo, appDataInfo.dataName, appDataInfo.id, value, handlers, request);
					}
					else{
						//new
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(appDataService.getAddAppDataSegmentRequest(appDataInfo.ownerInfo, appDataInfo.dataName, 0, appDataInfo.id, value, appDataInfo.version, {
							success : function(request){
								appDataInfo.persist=true;
							}
						}));
						return out;
					}
				}
			));
		},

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
								moduleInfo.module.prv_getIODataSet(), 
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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("iotask.dataAssociationUtility", function(){node_dataAssociationUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
