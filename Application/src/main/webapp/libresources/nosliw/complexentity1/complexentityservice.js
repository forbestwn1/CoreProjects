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
	var node_buildComplexEntityPlugInObject;
	var node_buildSimpleEntityPlugInObject;
	var node_buildAdapterPlugInObject;
	var node_createComponentRuntime;
	var node_componentUtility;
	var node_createPackageCore;
	var node_createApplication;
	var node_createLifeCycleRuntimeContext;
	var node_buildComponentInterface;
	var node_makeObjectWithComponentManagementInterface;
	var node_createEntityDefinition;
	var node_getObjectType;
	var node_createStateBackupService;
	
    var node_makeObjectWithEmbededEntityInterface;
    var node_getEmbededEntityInterface;

    var node_makeObjectWithComponentInterface;
    var node_getComponentInterface;

    var node_makeObjectComplexEntityObjectInterface; 
    var node_getComplexEntityObjectInterface;
    var node_makeObjectEntityTreeNodeInterface;
    var node_getEntityTreeNodeInterface;
	var node_makeObjectWithId;
	var node_complexEntityUtility;
	var node_makeObjectWithType;
	var node_buildComplexEntityCreationVariationPointObject;
    
	var node_createTestComplex1Plugin;
	var node_createTestComplexScriptPlugin;
	var node_createDataAssociationAdapterPlugin;
	var node_createDataAssociationInteractiveAdapterPlugin;
	var node_createDataAssociationTaskAdapterPlugin;
	var node_createDataServiceEntityPlugin;
	var node_createDataExpressionGroupPlugin;
	var node_createDataExpressionSinglePlugin;
	var node_createExpressionGroupPlugin;
	var node_createExpressionSinglePlugin;
	var node_createTaskPlugin;
	var node_createDecorationScriptPlugin;
	var node_createComplexEntityContainerPlugin;
	var node_createScriptTaskGroupEntityPlugin;
	
	var node_createSimpleEntityContainerPlugin;
	var node_makeObjectWithValuePortInterface;

//*******************************************   Start Node Definition  ************************************** 	

var node_createComplexEntityRuntimeService = function() {
	
	var loc_complexEntityPlugins = {};
	var loc_simpleEntityPlugins = {};

	var loc_adapterPlugins = {};

	var loc_getCreateAdapterRequest = function(adapterType, adapterDefinition, handlers, request){
		if(adapterDefinition==undefined){
			return node_createServiceRequestInfoSimple({}, function(requestInfo){
				return;
			});		
		}
		else{
			return loc_adapterPlugins[adapterType].getNewAdapterRequest(node_createEntityDefinition(adapterDefinition), handlers, request);
		}
	};

	var loc_getCreateSimpleEntityRequest = function(entityType, entityDef, configure, handlers, request){
		var out = node_createServiceRequestInfoSequence("CreateSimpleEntityRequest", handlers, request);
		var entityDef = node_createEntityDefinition(entityDef);
		var simpleEntityPlugin = loc_simpleEntityPlugins[entityType];
		out.addRequest(simpleEntityPlugin.getCreateEntityRequest(entityDef, configure, {
			success : function(request, simpleEntity){
				simpleEntity = node_makeObjectWithEmbededEntityInterface(simpleEntity);
				
				simpleEntity = node_makeObjectBasicEntityObjectInterface(simpleEntity, entityDef, configure);
				
				simpleEntity = node_makeObjectWithValuePortInterface(simpleEntity);
				
				simpleEntity = node_makeObjectWithComponentInterface(entityType, simpleEntity, false);
				
				simpleEntity = node_makeObjectWithId(simpleEntity);
				
				simpleEntity = node_makeObjectWithType(simpleEntity, entityType);
				
			}
		}));
		return out;
	};

	var loc_getCreateComplexEntityCoreRequest = function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
		var entityType = complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ENTITYTYPE];  //complexEntityId[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE]
		var complexEntityPlugin = loc_complexEntityPlugins[entityType];

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var complexEntityDef = node_createEntityDefinition(complexEntityDef);
		out.addRequest(complexEntityPlugin.getCreateComplexEntityCoreRequest(complexEntityDef, valueContextId, bundleCore, configure, {
			success : function(request, complexEntityCore){
				
				complexEntityCore = node_makeObjectWithEmbededEntityInterface(complexEntityCore);
				
				complexEntityCore = node_makeObjectEntityTreeNodeInterface(complexEntityCore);
				
				complexEntityCore = node_makeObjectBasicEntityObjectInterface(complexEntityCore, complexEntityDef, configure);
				
				complexEntityCore = node_makeObjectComplexEntityObjectInterface(complexEntityCore, valueContextId, bundleCore);
				
				complexEntityCore = node_makeObjectWithValuePortInterface(complexEntityCore);
				
				complexEntityCore = node_makeObjectWithComponentInterface(entityType, complexEntityCore, false);
				
				complexEntityCore = node_makeObjectWithId(complexEntityCore);
				
				complexEntityCore = node_makeObjectWithType(complexEntityCore, entityType);
				
				
				var out = node_createServiceRequestInfoSequence(undefined, {
					success : function(request){
						return complexEntityCore;
					}
				}, request);

				out.addRequest(node_getComplexEntityObjectInterface(complexEntityCore).getComplexEntityInitRequest());
				
				return out;
			}
		}));
		return out;
	};
	
	var loc_getCreateComplexEntityRuntimeRequest = function(complexEntityDef, parentComplexEntityCore, bundleCore, variationPoints, configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		variationPoints = node_buildComplexEntityCreationVariationPointObject(variationPoints);
		
		//internal entity
		//build variableGroup
		var valueContextId = null;
		var variableDomain = bundleCore.getVariableDomain();
		var valueContextDef = complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_VALUECONTEXT];
		var parentComplexEntityInterface = node_getComplexEntityObjectInterface(parentComplexEntityCore);
		valueContextId = variableDomain.creatValueContext(valueContextDef, parentComplexEntityCore==undefined?undefined : parentComplexEntityInterface.getValueContextId());
		
		//process raw configure			
		//get runtime configure & decoration info from configure
		var runtimeConfigureInfo = node_componentUtility.processRuntimeConfigure(configure);
		
		out.addRequest(variationPoints.afterValueContext(complexEntityDef, valueContextId, bundleCore, runtimeConfigureInfo.coreConfigure));
		
		//new complexCore through complex plugin
		out.addRequest(loc_getCreateComplexEntityCoreRequest(complexEntityDef, valueContextId, bundleCore, runtimeConfigureInfo.coreConfigure, {
			success : function(request, componentCore){
				//create runtime
				return node_createComponentRuntime(componentCore, runtimeConfigureInfo.decorations, request);
			}
		}));
		
		return out;
	};

	var loc_init = function(){
		//complex entity plugin
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, node_createTestComplex1Plugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, node_createTestComplexScriptPlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, node_createDataExpressionGroupPlugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, node_createDataExpressionGroupPlugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, node_createDataExpressionSinglePlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, node_createExpressionGroupPlugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, node_createExpressionGroupPlugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, node_createExpressionSinglePlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TASK, node_createTaskPlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, node_createScriptBasedPlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, node_createComplexEntityContainerPlugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_CONTAINERSIMPLE, node_createSimpleEntityContainerPlugin());

		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, node_createScriptTaskGroupEntityPlugin());


		//simple entity plugin
		loc_out.registerSimpleEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, node_createDataServiceEntityPlugin());


		//adapter plugin
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION, node_createDataAssociationAdapterPlugin());
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONINTERACTIVE, node_createDataAssociationInteractiveAdapterPlugin());
		loc_out.registerAdapterPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_DATAASSOCIATIONTASK, node_createDataAssociationTaskAdapterPlugin());
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

		//create package runtime object
		createPackageRuntime : function(parm, configure, request){
			//create runtime object
			var packageCore = node_createPackageCore(parm, configure);
			packageCore = loc_buildOtherObject(packageCore, parm, configure);
			return node_createComponentRuntime(packageCore, undefined, request); 
		},
				
		createBundleRuntime : function(parm, configure, request){
			var bundleCore = node_createBundleCore(parm, configure);
			bundleCore = loc_buildOtherObject(bundleCore, parm, configure);
			return node_createComponentRuntime(bundleCore, undefined);
		},

		createComplexEntityCore : function(complexEntityDef, variableGroupId, bundleCore, configure){
			return loc_createComplexEntityCore(complexEntityDef, variableGroupId, bundleCore, configure);
		},
		
		getCreateComplexEntityRuntimeRequest : function(complexEntityDef, parentCore, bundleCore, variationPoints, configure, handlers, request){
			return loc_getCreateComplexEntityRuntimeRequest(complexEntityDef, parentCore, bundleCore, variationPoints, configure, handlers, request);
		},

		getCreateSimpleEntityRequest : function(entityType, entityDef, configure, handlers, request){
			return loc_getCreateSimpleEntityRequest(entityType, entityDef, configure, handlers, request);
		},
		
		createContainerComplexEntityRuntime : function(containerDef, parentCore, bundleCore, configure, request){
			return loc_createContainerComplexEntityRuntime(containerDef, parentCore, bundleCore, configure, request);
		},
		
		getCreateAdapterRequest : function(adapterType, adapterDefinition, handlers, request){
			return loc_getCreateAdapterRequest(adapterType, adapterDefinition, handlers, request);
		},
		
		registerComplexEntityPlugin : function(entityType, complexEntityPlugin){
			loc_complexEntityPlugins[entityType] = node_buildComplexEntityPlugInObject(complexEntityPlugin);
		},
		
		registerSimpleEntityPlugin : function(entityType, simpleEntityPlugin){
			loc_simpleEntityPlugins[entityType] = node_buildSimpleEntityPlugInObject(simpleEntityPlugin);
		},
		
		registerAdapterPlugin : function(adapterType, adapterPlugin){
			loc_adapterPlugins[adapterType] = node_buildAdapterPlugInObject(adapterPlugin);
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
nosliw.registerSetNodeDataEvent("complexentity.buildComplexEntityPlugInObject", function(){node_buildComplexEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildSimpleEntityPlugInObject", function(){node_buildSimpleEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildAdapterPlugInObject", function(){node_buildAdapterPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createPackageCore", function(){node_createPackageCore = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createApplication", function(){node_createApplication = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createLifeCycleRuntimeContext", function(){node_createLifeCycleRuntimeContext = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createEntityDefinition", function(){node_createEntityDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("component.createStateBackupService", function(){node_createStateBackupService = this.getData();});

nosliw.registerSetNodeDataEvent("common.embeded.makeObjectWithEmbededEntityInterface", function(){node_makeObjectWithEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});

nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentInterface", function(){node_makeObjectWithComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentInterface", function(){node_getComponentInterface = this.getData();});

nosliw.registerSetNodeDataEvent("complexentity.makeObjectBasicEntityObjectInterface", function(){node_makeObjectBasicEntityObjectInterface = this.getData();}); 
nosliw.registerSetNodeDataEvent("complexentity.makeObjectComplexEntityObjectInterface", function(){node_makeObjectComplexEntityObjectInterface = this.getData();}); 
nosliw.registerSetNodeDataEvent("complexentity.getComplexEntityObjectInterface", function(){node_getComplexEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.makeObjectEntityTreeNodeInterface", function(){node_makeObjectEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithid.makeObjectWithId", function(){node_makeObjectWithId = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createScriptBasedPlugin", function(){node_createScriptBasedPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildComplexEntityCreationVariationPointObject", function(){node_buildComplexEntityCreationVariationPointObject = this.getData();});


nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestComplexScriptPlugin", function(){node_createTestComplexScriptPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationAdapterPlugin", function(){node_createDataAssociationAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationInteractiveAdapterPlugin", function(){node_createDataAssociationInteractiveAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("iovalue.createDataAssociationTaskAdapterPlugin", function(){node_createDataAssociationTaskAdapterPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("dataservice.createDataServiceEntityPlugin", function(){node_createDataServiceEntityPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionGroupPlugin", function(){node_createDataExpressionGroupPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createDataExpressionSinglePlugin", function(){node_createDataExpressionSinglePlugin = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskPlugin", function(){node_createTaskPlugin = this.getData();});


nosliw.registerSetNodeDataEvent("expression.createExpressionGroupPlugin", function(){node_createExpressionGroupPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("expression.createExpressionSinglePlugin", function(){node_createExpressionSinglePlugin = this.getData();});
nosliw.registerSetNodeDataEvent("entitycontainer.createComplexEntityContainerPlugin", function(){node_createComplexEntityContainerPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("scripttaskgroup.createScriptTaskGroupEntityPlugin", function(){node_createScriptTaskGroupEntityPlugin = this.getData();});

nosliw.registerSetNodeDataEvent("entitycontainer.createSimpleEntityContainerPlugin", function(){node_createSimpleEntityContainerPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.makeObjectWithValuePortInterface", function(){node_makeObjectWithValuePortInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("createComplexEntityRuntimeService", node_createComplexEntityRuntimeService); 

})(packageObj);
