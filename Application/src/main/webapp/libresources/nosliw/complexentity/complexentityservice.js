//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_basicUtility;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createBundleCore;
	var node_EntityIdInDomain;
	var node_buildEntityPlugInObject;
	var node_buildAdapterPlugInObject;
	var node_createComponentRuntime;
	var node_componentUtility;
	var node_createPackageCore;
	var node_createApplication;
	var node_createLifeCycleRuntimeContext;
	var node_buildComponentInterface;
	var node_makeObjectWithComponentManagementInterface;
	var node_createBrickDefinition;
	var node_getObjectType;
	var node_createStateBackupService;
	
    var node_makeObjectWithEmbededEntityInterface;
    var node_getEmbededEntityInterface;

    var node_makeObjectWithComponentInterface;
    var node_getComponentInterface;

    var node_makeObjectEntityObjectInterface; 
    var node_getEntityObjectInterface;
    var node_makeObjectEntityTreeNodeInterface;
    var node_getEntityTreeNodeInterface;
	var node_makeObjectWithId;
	var node_complexEntityUtility;
	var node_makeObjectWithType;
	var node_buildComplexEntityCreationVariationPointObject;
    var node_createDynamicCore;
    
	var node_createTestComplex1Plugin;
	var node_createTestComplexScriptPlugin;
	var node_createTestComplexTaskPlugin;
	var node_createDataAssociationAdapterPlugin;
	var node_createDataAssociationInteractiveAdapterPlugin;
	var node_createDataAssociationForTaskAdapterPlugin;
	var node_createDataAssociationForExpressionAdapterPlugin;
	var node_createDataServiceEntityPlugin;
	var node_createDataExpressionGroupPlugin;
	var node_createScriptExpressionGroupPlugin;
	var node_createDataExpressionSinglePlugin;
	var node_createDataExpressionLibraryElementPlugin;
	var node_createExpressionGroupPlugin;
	var node_createExpressionSinglePlugin;
	var node_createTaskPlugin;
	var node_createScriptTaskTaskPlugin;
	var node_createDecorationScriptPlugin;
	var node_createScriptTaskGroupEntityPlugin;
	var node_createFlowTaskPlugin;
	var node_createTaskActivityPlugin;
	var node_createDynamicActivityPlugin;
	
	var node_createContainerPlugin;
	var node_createContainerListPlugin;
	var node_makeObjectWithValuePortInterface;
	var node_createWrapperBrickPlugin;
	var node_createModulePlugin;

//*******************************************   Start Node Definition  ************************************** 	

var node_createComplexEntityRuntimeService = function() {
	
	var loc_entityPlugins = {};
	var loc_adapterPlugins = {};

	var loc_getCreateAdapterRequest = function(rawAdapterDefinition, baseCore, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		var entityType = rawAdapterDefinition[node_COMMONATRIBUTECONSTANT.BRICK_BRICKTYPE];
		var adapterEntityPlugin = loc_adapterPlugins[entityType[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]][entityType[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_VERSION]];
		var adapterDefinition = node_createBrickDefinition(rawAdapterDefinition);
		out.addRequest(adapterEntityPlugin.getNewAdapterRequest(adapterDefinition, baseCore, {
			success : function(request, adapterEntity){
				return node_makeObjectBasicEntityObjectInterface(adapterEntity, adapterDefinition, undefined);
			}
		}));
		return out;
	};

	var loc_getCreateEntityCoreRequest = function(rawEntityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, handlers, request){
		var brickDef = node_createBrickDefinition(rawEntityDef);
		var brickType = brickDef.getBrickType();
		var complexEntityPlugin = loc_entityPlugins[brickType[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_BRICKTYPE]][brickType[node_COMMONATRIBUTECONSTANT.IDBRICKTYPE_VERSION]];

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(complexEntityPlugin.getCreateEntityCoreRequest(brickDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, configure, {
			success : function(request, entityCore){
				
				entityCore = node_makeObjectWithEmbededEntityInterface(entityCore);
				
				entityCore = node_makeObjectEntityTreeNodeInterface(entityCore);
				
				entityCore = node_makeObjectBasicEntityObjectInterface(entityCore, brickDef, configure);
				
				entityCore = node_makeObjectEntityObjectInterface(entityCore, internalValuePortContainerId, externalValuePortContainerId, bundleCore);
				
				entityCore = node_makeObjectWithValuePortInterface(entityCore);
				
				entityCore = node_makeObjectWithComponentInterface(brickType, entityCore, false);
				
				entityCore = node_makeObjectWithId(entityCore);
				
				entityCore = node_makeObjectWithType(entityCore, brickType);
				
				
				var out1 = node_createServiceRequestInfoSequence(undefined, {
					success : function(request){
						return entityCore;
					}
				});
				out1.addRequest(node_getEntityObjectInterface(entityCore).getEntityInitRequest());
				return out1;
			}
		}));
		return out;
	};

	var loc_getCreateDynamicRuntimeRequest = function(dynamicDef, parentEntityCore, bundleCore, variationPoints, configure, handlers, request){

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);

		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var dynamicCore = node_createDynamicCore(dynamicDef);
			
			dynamicCore = node_makeObjectWithEmbededEntityInterface(dynamicCore);
			
			dynamicCore = node_makeObjectEntityTreeNodeInterface(dynamicCore);
			
			dynamicCore = node_makeObjectBasicEntityObjectInterface(dynamicCore, dynamicDef, configure);
			
			dynamicCore = node_makeObjectWithComponentInterface(undefined, dynamicCore, false);
			
			dynamicCore = node_makeObjectEntityObjectInterface(dynamicCore, undefined, function(){
					return node_getEntityObjectInterface(dynamicCore.getCurrentTaskEntityCore()).getExternalValuePortContainer();
				}, bundleCore);
			
			var runtimeConfigureInfo = node_componentUtility.processRuntimeConfigure(configure);
			return node_createComponentRuntime(dynamicCore, runtimeConfigureInfo.decorations, request);
		}));
		
		return out;
	};

	var loc_getCreateEntityRuntimeRequest = function(entityDef, parentEntityCore, bundleCore, variationPoints, configure, handlers, request){

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		//build value context
		var parentValuePortContainer = parentEntityCore==undefined?undefined : node_getEntityObjectInterface(parentEntityCore).getInternalValuePortContainer();
		var externalValuePortContainerId = bundleCore.getValuePortDomain().creatValuePortContainer(entityDef[node_COMMONATRIBUTECONSTANT.WITHEXTERNALVALUEPORT_EXTERNALVALUEPORT], parentValuePortContainer==undefined?undefined:parentValuePortContainer.getId());
		var internalValuePortContainerId = bundleCore.getValuePortDomain().creatValuePortContainer(entityDef[node_COMMONATRIBUTECONSTANT.WITHINTERNALVALUEPORT_INTERNALVALUEPORT], externalValuePortContainerId);
		
		//process raw configure			
		//get runtime configure & decoration info from configure
		var runtimeConfigureInfo = node_componentUtility.processRuntimeConfigure(configure);
		
		variationPoints = node_buildComplexEntityCreationVariationPointObject(variationPoints);
		out.addRequest(variationPoints.afterValueContext(entityDef, internalValuePortContainerId, bundleCore, runtimeConfigureInfo.coreConfigure));
		
		//new complexCore through complex plugin
		out.addRequest(loc_getCreateEntityCoreRequest(entityDef, internalValuePortContainerId, externalValuePortContainerId, bundleCore, runtimeConfigureInfo.coreConfigure, {
			success : function(request, componentCore){
				//create runtime
				return node_createComponentRuntime(componentCore, runtimeConfigureInfo.decorations, request);
			}
		}));
		
		return out;
	};

	var loc_init = function(){

//		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_WRAPPERBRICK, "1.0.0", node_createWrapperBrickPlugin());

//		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_MODULE, "1.0.0", node_createModulekPlugin());

		//complex entity plugin
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "1.0.0", node_createTestComplex1Plugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, "1.0.0", node_createTestComplexScriptPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_TASK, "1.0.0", node_createTestComplexTaskPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, "1.0.0", node_createDataExpressionGroupPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, "1.0.0", node_createDataExpressionGroupPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, "1.0.0", node_createScriptExpressionGroupPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, "1.0.0", node_createDataExpressionSinglePlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONLIBELEMENT, "1.0.0", node_createDataExpressionLibraryElementPlugin());

//		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, "1.0.0", node_createExpressionGroupPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, "1.0.0", node_createExpressionGroupPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, "1.0.0", node_createExpressionSinglePlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, "1.0.0", node_createScriptBasedPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONTAINER, "1.0.0", node_createContainerPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONTAINERLIST, "1.0.0", node_createContainerListPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, "1.0.0", node_createScriptTaskGroupEntityPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TASK_FLOW, "1.0.0", node_createFlowTaskPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_FLOW_ACTIVITYTASK, "1.0.0", node_createTaskActivityPlugin());
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_FLOW_ACTIVITYDYNAMIC, "1.0.0", node_createDynamicActivityPlugin());



		//simple entity plugin
		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, "1.0.0", node_createDataServiceEntityPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TASKWRAPPER, "1.0.0", node_createTaskPlugin());

		loc_out.registerEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TASK_TASK_SCRIPT, "1.0.0", node_createScriptTaskTaskPlugin());


		//adapter plugin
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION, "1.0.0", node_createDataAssociationAdapterPlugin());
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONINTERACTIVE, "1.0.0", node_createDataAssociationInteractiveAdapterPlugin());
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFORTASK, "1.0.0", node_createDataAssociationForTaskAdapterPlugin());
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONFOREXPRESSION, "1.0.0", node_createDataAssociationForExpressionAdapterPlugin());
	};


	var loc_buildOtherObject = function(entity, entityDef, configure){
		
		entity = node_makeObjectWithEmbededEntityInterface(entity);
		
		entity = node_makeObjectBasicEntityObjectInterface(entity, entityDef, configure);

		entity = node_makeObjectEntityTreeNodeInterface(entity);
		
		entity = node_makeObjectWithComponentInterface(node_getObjectType(entity), entity, false);
		
		entity = node_makeObjectWithId(entity);

		return entity;
	};
	
	var loc_out = {

		getCreateApplicationRequest : function(parm, configureInfo, runtimeContext, envInterface, handlers, request){
			var out = node_createServiceRequestInfoSequence("getCreateApplicationRequest", handlers, request);
			
			out.addRequest(node_complexEntityUtility.getRootConfigureRequest(configureInfo, {
				success : function(request, configure){
					var application = node_createApplication(parm, configure);
		
					application = loc_buildOtherObject(application, parm, configure);
					
					node_makeObjectWithComponentManagementInterface(application, application);
					
					return node_getComponentInterface(application).getPreInitRequest({
						success : function(request){
							//try pass envInterface to main entity
							if(envInterface!=undefined){
								var embededInterface = node_getEmbededEntityInterface(application.getMainEntityRuntime());
								_.each(envInterface, function(interfacee, name){
									embededInterface.setEnvironmentInterface(name, interfacee);
								});
							}

							//update backup state object
							var backupStateObj = runtimeContext==undefined?undefined:runtimeContext.backupState;
//							if(backupStateObj==undefined)  backupStateObj =	node_createStateBackupService(resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE], resourceId[[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]], "1.0.0", nosliw.runtime.getStoreService());
							if(backupStateObj!=undefined)  node_getComponentInterface(application).updateBackupStateObject(backupStateObj);

							//update lifecycle entity
							var lifecycleEntity = runtimeContext==undefined?undefined:runtimeContext.lifecycleEntity;
							if(lifecycleEntity==undefined)  lifecycleEntity = node_createLifeCycleRuntimeContext("application");
							node_getComponentInterface(application).updateLifecycleEntityObject(lifecycleEntity);

							//update view
							if(runtimeContext!=undefined && runtimeContext.view!=undefined){
								node_getComponentInterface(application).updateView(runtimeContext.view);
							}

							return node_getComponentInterface(application).getPostInitRequest({
								success : function(request){
									return application;
								}
							});
						}
					});
				}
			}));
			
			return out;
		},
			
		executeCreateApplicationRequest : function(parm, configure, runtimeContext, handlers, request){
			var requestInfo = this.getCreateApplicationRequest(parm, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		

/*
		//create package runtime object
		createPackageRuntime : function(parm, configure, request){
			//create runtime object
			var packageCore = node_createPackageCore(parm, configure);
			packageCore = loc_buildOtherObject(packageCore, parm, configure);
			return node_createComponentRuntime(packageCore, undefined, request); 
		},
*/

		getCreateBundleRuntimeRequest : function(parm, configure, handlers, request){
			return node_createServiceRequestInfoSimple("createBundleRuntimeRequest", function(request){
				var bundleCore = node_createBundleCore(parm, configure);
				bundleCore = loc_buildOtherObject(bundleCore, parm, configure);
				var bundleRuntime = node_createComponentRuntime(bundleCore, undefined);
				return bundleRuntime;
			}, handlers, request);
		},

		getCreateBundleRuntimeWithInitRequest : function(parm, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple("createBundleRuntimeRequest", function(request){
				var bundleCore = node_createBundleCore(parm, configure);
				bundleCore = loc_buildOtherObject(bundleCore, parm, configure);
				var bundleRuntime = node_createComponentRuntime(bundleCore, undefined);
				return node_getComponentInterface(bundleRuntime.getCoreEntity()).getPreInitRequest({
					success : function(request){
						return bundleRuntime;
					}
				});
			}));
			return out;
		},
				

				
		getCreateBundleRuntimeRequest1 : function(parm, configure, handlers, request){
			var out = node_createServiceRequestInfoSequence("createBundleRuntimeRequest", handlers, request);
			var bundleCore = node_createBundleCore(parm, configure);
			bundleCore = loc_buildOtherObject(bundleCore, parm, configure);
			out.addRequest(bundleCore.getPreInitRequest1({
				success: function(request){
					return node_createComponentRuntime(bundleCore, undefined);
				}
			}))
			return out;
		},

		getCreateEntityRuntimeRequest : function(entityDef, parentCore, bundleCore, variationPoints, configure, handlers, request){
			return loc_getCreateEntityRuntimeRequest(entityDef, parentCore, bundleCore, variationPoints, configure, handlers, request);
		},

		getCreateDynamicRuntimeRequest : function(dynamicDef, parentCore, bundleCore, variationPoints, configure, handlers, request){
			return loc_getCreateDynamicRuntimeRequest(dynamicDef, parentCore, bundleCore, variationPoints, configure, handlers, request);
		},

		getCreateAdapterRequest : function(adapterDefinition, baseCore, handlers, request){
			return loc_getCreateAdapterRequest(adapterDefinition, baseCore, handlers, request);
		},

		registerEntityPlugin : function(entityType, version, entityPlugin){
			loc_entityPlugins[entityType] = {};
			loc_entityPlugins[entityType][version] = node_buildEntityPlugInObject(entityPlugin);
		},
	
		registerAdapterPlugin : function(adapterType, version, adapterPlugin){
			loc_adapterPlugins[adapterType] = {};
			loc_adapterPlugins[adapterType][version] = node_buildAdapterPlugInObject(adapterPlugin);
		},
		
	};

	loc_init();
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createBundleCore", function(){node_createBundleCore = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.EntityIdInDomain", function(){node_EntityIdInDomain = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildEntityPlugInObject", function(){node_buildEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildAdapterPlugInObject", function(){node_buildAdapterPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createPackageCore", function(){node_createPackageCore = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createApplication", function(){node_createApplication = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createLifeCycleRuntimeContext", function(){node_createLifeCycleRuntimeContext = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createBrickDefinition", function(){node_createBrickDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});

nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithEmbededEntityInterface", function(){node_makeObjectWithEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});

nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentInterface", function(){node_makeObjectWithComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});

nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectBasicEntityObjectInterface", function(){node_makeObjectBasicEntityObjectInterface = this.getData();}); 
nosliw.registerSetNodeDataEvent("complexentity.makeObjectEntityObjectInterface", function(){node_makeObjectEntityObjectInterface = this.getData();}); 
nosliw.registerSetNodeDataEvent("complexentity.getEntityObjectInterface", function(){node_getEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.makeObjectEntityTreeNodeInterface", function(){node_makeObjectEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("scriptbased.createScriptBasedPlugin", function(){node_createScriptBasedPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildComplexEntityCreationVariationPointObject", function(){node_buildComplexEntityCreationVariationPointObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createDynamicCore", function(){node_createDynamicCore = this.getData();});


nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestComplexScriptPlugin", function(){node_createTestComplexScriptPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestComplexTaskPlugin", function(){node_createTestComplexTaskPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationAdapterPlugin", function(){node_createDataAssociationAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationInteractiveAdapterPlugin", function(){node_createDataAssociationInteractiveAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationForTaskAdapterPlugin", function(){node_createDataAssociationForTaskAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationForExpressionAdapterPlugin", function(){node_createDataAssociationForExpressionAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("dataservice.createDataServiceEntityPlugin", function(){node_createDataServiceEntityPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionGroupPlugin", function(){node_createDataExpressionGroupPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createScriptExpressionGroupPlugin", function(){node_createScriptExpressionGroupPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionSinglePlugin", function(){node_createDataExpressionSinglePlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionLibraryElementPlugin", function(){node_createDataExpressionLibraryElementPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskPlugin", function(){node_createTaskPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("taskscript.createScriptTaskTaskPlugin", function(){node_createScriptTaskTaskPlugin = this.getData();});

nosliw.registerSetNodeDataEvent("expression.createExpressionGroupPlugin", function(){node_createExpressionGroupPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createExpressionSinglePlugin", function(){node_createExpressionSinglePlugin = this.getData();});
nosliw.registerSetNodeDataEvent("scripttaskgroup.createScriptTaskGroupEntityPlugin", function(){node_createScriptTaskGroupEntityPlugin = this.getData();});

nosliw.registerSetNodeDataEvent("taskflow.createFlowTaskPlugin", function(){node_createFlowTaskPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("taskflow.createTaskActivityPlugin", function(){node_createTaskActivityPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("taskflow.createDynamicActivityPlugin", function(){node_createDynamicActivityPlugin = this.getData();});

nosliw.registerSetNodeDataEvent("entitycontainer.createContainerPlugin", function(){node_createContainerPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("entitycontainer.createContainerListPlugin", function(){node_createContainerListPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.makeObjectWithValuePortInterface", function(){node_makeObjectWithValuePortInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createContainerPlugin", function(){node_createWrapperBrickPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("module.createModulePlugin", function(){node_createModulePlugin = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComplexEntityRuntimeService", node_createComplexEntityRuntimeService); 

})(packageObj);
