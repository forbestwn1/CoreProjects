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
	var node_resourceUtility;
	var node_createBundleCore;
	var node_EntityIdInDomain;
	var node_buildComplexEntityPlugInObject;
	var node_buildSimpleEntityPlugInObject;
	var node_createComponentRuntime;
	var node_componentUtility;
	var node_createPackageCore;
	var node_createApplication;
	var node_createLifeCycleRuntimeContext;
	var node_buildComponentInterface;
	var node_makeObjectWithComponentManagementInterface;
	var node_createEntityDefinition;
	var node_createComplexEntityRuntimeContainer;
	var node_buildComplexEntityCoreObject;
	var node_createComplexEntityEnvInterface;
	
	var node_createTestComplex1Plugin;
	var node_createTestComplexScriptPlugin;
	var node_createTestSimple1Plugin;
	var node_createTestDecoration1Plugin;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createComplexEntityRuntimeService = function() {
	
	var loc_complexEntityPlugins = {};
	var loc_simpleEntityPlugins = {};

	var loc_getCreateContainerComplexEntityRuntimeRequest = function(containerDef, parentComplexEntityCore, bundleCore, configure, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		var container = node_createComplexEntityRuntimeContainer();
		var elements = containerDef[node_COMMONATRIBUTECONSTANT.CONTAINERENTITY_ELEMENT];
		_.each(elements, function(ele, i){
			var entityId = ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ENTITY][node_COMMONATRIBUTECONSTANT.EMBEDED_VALUE];
			out.addRequest(loc_getCreateComplexEntityRuntimeRequest(entityId, parentComplexEntityCore, bundleCore, configure, {
				success : function(request, eleRuntime){
					container.addElement(eleEntityRuntime, ele[node_COMMONATRIBUTECONSTANT.ELEMENTCONTAINER_ELEMENTID]);
				}
			}));
		});
		
		out.addRequest(node_createServiceRequestInfoSimple({}, function(requestInfo){
			return container;
		}));
		return out;
	};
	
	var loc_getCreateComplexEntityCoreRequest = function(complexEntityDef, valueContextId, bundleCore, configure, handlers, request){
		var entityType = complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ENTITYTYPE];  //complexEntityId[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE]
		var complexEntityPlugin = loc_complexEntityPlugins[entityType];

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(complexEntityPlugin.getCreateComplexEntityCoreRequest(node_createEntityDefinition(complexEntityDef), valueContextId, bundleCore, configure, {
			success : function(request, complexEntityCore){
				
				complexEntityCore = node_makeObjectWithEmbededEntityInterface(complexEntityCore);
				
				complexEntityCore = node_makeObjectEntityTreeNodeInterface(complexEntityCore);
				
				complexEntityCore = node_makeObjectComplexEntityObject(complexEntityCore, valueContextId, bundleCore);
				
				complexEntityCore = node_makeObjectWithComponentInterface(complexEntityCore, false);
				
				var out = node_createServiceRequestInfoSequence(undefined, {
					success : function(request){
						return complexEntityCore;
					}
				}, request);

				out.addRequest(node_getComplexEntityInterface(complexEntityCore).getComplexEntityInitRequest());
				
				return out;
			}
		}));
		return out;
	};
	
	var loc_getCreateComplexEntityRuntimeRequest = function(complexEntityId, parentComplexEntityCore, bundleCore, configure, handlers, request){

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		
		complexEntityId = new node_EntityIdInDomain(complexEntityId);
		
		//get component definition
		var entityDefDomain = bundleCore.getBundleDefinition()[node_COMMONATRIBUTECONSTANT.EXECUTABLEBUNDLE_EXECUTABLEENTITYDOMAIN];
		var complexEntityInfo = entityDefDomain[node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_COMPLEXENTITY][complexEntityId.literateStr];

		var complexEntityDef = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE_ENTITY];
		if(complexEntityDef!=undefined){
			//internal entity
			//build variableGroup
			var valueContextId = null;
			var variableDomain = bundleCore.getVariableDomain();
			var valueContextDef = complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_VALUECONTEXT];
			valueContextId = variableDomain.creatValueContext(valueContextDef, parentComplexEntityCore==undefined?undefined : parentComplexEntityCore.getValueContextId());
			
			//new complexCore through complex plugin
			out.addRequest(loc_getCreateComplexEntityCoreRequest(complexEntityDef, valueContextId, bundleCore, configure, {
				success : function(request, componentCore){
					//build decorationInfos
					var decorationInfos = null;
					
					//create runtime
					return node_createComponentRuntime(componentCore, decorationInfos, request);
				}
			}));
			
		}
		else{
			out.addRequest(node_createServiceRequestInfoSimple({}, function(request){
				//refer to external Entity (bundle)
				var externalEntityId = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE_EXTERNALCOMPLEXENTITYID];
				var globalId = entityDefDomain[node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_EXTERNALENTITY][externalEntityId];
				//create bundle runtime
				return loc_out.createBundleRuntime(globalId, configure, request);
			}));
		}
		
		return out;
	};

	var loc_init = function(){
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, node_createTestComplex1Plugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, node_createTestComplexScriptPlugin());
		loc_out.registerSimpleEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1, node_createTestSimple1Plugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_DECORATION_1, node_createTestDecoration1Plugin());
	};


	var loc_buildOtherObject = function(entity){
		entity = node_makeObjectWithEmbededEntityInterface(entity);
		
		entity = node_makeObjectEntityTreeNodeInterface(entity);
		
		entity = node_makeObjectWithComponentInterface(entity, false);
		return entity;
	};
	
	var loc_out = {

		getCreateApplicationRequest : function(resourceId, configure, runtimeContext, runtimeInterface, handlers, request){
			var application = node_createApplication(resourceId, configure);

			application = loc_buildOtherObject(application);
			
			var applicationMan = node_makeObjectWithComponentManagementInterface(applicationComponent, application, application);
			
			//build backup state if not provided
			if(runtimeContext.backupState==undefined) runtimeContext.backupState = node_createStateBackupService(resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE], resourceId[[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]], "1.0.0", nosliw.runtime.getStoreService());			

			//init lifecycle entity
			if(runtimeContext.lifecycleEntity==undefined)	runtimeContext.lifecycleEntity = node_createLifeCycleRuntimeContext("application");
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("InitApplication", {}), handlers, request);
			out.addRequest(application.getPreInitRequest({
				success : function(request){
					return application.getUpdateRuntimeInterfaceRequest(runtimeInterface, {
						success : function(request){
							return application.getUpdateRuntimeContextRequest(runtimeContext, {
								success : function(request){
									return application.getPostInitRequest({
										success : function(){
											return application;
										}
									});
								}
							});
						}
					});
				}
			}));

			return out;
		},
			
		executeCreateApplicationRequest : function(packageResourceId, configure, runtimeContext, handlers, request){
			var requestInfo = this.getCreateApplicationRequest(packageResourceId, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		

		//create package runtime object
		createPackageRuntime : function(packageResourceId, configure, request){
			//get runtime configure & decoration info from configure
			var runtimeConfigureInfo = node_componentUtility.processRuntimeConfigure(configure);
			//create runtime object
			var packageCore = node_createPackageCore(packageResourceId, runtimeConfigureInfo.coreConfigure);
			packageCore = loc_buildOtherObject(packageCore);
			return node_createComponentRuntime(packageCore, runtimeConfigureInfo.decorations, request); 
		},
				
		//create package runtime object and init request
		getCreatePackageRuntimeRequest : function(packageResourceId, configure, runtimeContext, runtimeInterface, handlers, request){
			var packageRuntime = this.createPackageRuntime(packageResourceId, configure, request);
			
			//build backup state if not provided
			if(runtimeContext.backupState==undefined) runtimeContext.backupState = node_createStateBackupService(packageResourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE], packageResourceId[[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]], "1.0.0", nosliw.runtime.getStoreService());			

			//init lifecycle entity
			if(runtimeContext.lifecycleEntity==undefined)	runtimeContext.lifecycleEntity = node_createLifeCycleRuntimeContext("package");
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreatePackageRuntime", {"resourceId":packageResourceId}), handlers, request);
			//init package runtime
			out.addRequest(packageRuntime.getInitRequest(runtimeContext, runtimeInterface, {
				success : function(request){
					return packageRuntime;
				}
			}));

			return out;
		},
		
		executeCreatePackageRuntimeRequest : function(packageResourceId, configure, runtimeContext, runtimeInterface, handlers, request){
			var requestInfo = this.getCreatePackageRuntimeRequest(packageResourceId, configure, runtimeContext, runtimeInterface, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		

		createBundleRuntime : function(globalComplexEntitId, configure, request){
			var bundleCore = node_createBundleCore(globalComplexEntitId, configure);
			bundleCore = loc_buildOtherObject(bundleCore);
			return node_createComponentRuntime(bundleCore, undefined);
		},

		createComplexEntityCore : function(complexEntityDef, variableGroupId, bundleCore, configure){
			return loc_createComplexEntityCore(complexEntityDef, variableGroupId, bundleCore, configure);
		},
		
		createSimpleEntity : function(entityDef){
			var entityType = entityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ENTITYTYPE];
			var simpleEntityPlugin = loc_simpleEntityPlugins[entityType];
			return simpleEntityPlugin.createEntity(node_createEntityDefinition(entityDef));
		},
		
		getCreateComplexEntityRuntimeRequest : function(complexEntityId, parentCore, bundleCore, configure, handlers, request){
			return loc_getCreateComplexEntityRuntimeRequest(complexEntityId, parentCore, bundleCore, configure, handlers, request);
		},

		createContainerComplexEntityRuntime : function(containerDef, parentCore, bundleCore, configure, request){
			return loc_createContainerComplexEntityRuntime(containerDef, parentCore, bundleCore, configure, request);
		},
		
		registerComplexEntityPlugin : function(entityType, complexEntityPlugin){
			loc_complexEntityPlugins[entityType] = node_buildComplexEntityPlugInObject(complexEntityPlugin);
		},
		
		registerSimpleEntityPlugin : function(entityType, simpleEntityPlugin){
			loc_simpleEntityPlugins[entityType] = node_buildSimpleEntityPlugInObject(simpleEntityPlugin);
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
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createBundleCore", function(){node_createBundleCore = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.EntityIdInDomain", function(){node_EntityIdInDomain = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildComplexEntityPlugInObject", function(){node_buildComplexEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildSimpleEntityPlugInObject", function(){node_buildSimpleEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createPackageCore", function(){node_createPackageCore = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createApplication", function(){node_createApplication = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentInterface = this.getData();});
nosliw.registerSetNodeDataEvent("component.createLifeCycleRuntimeContext", function(){node_createLifeCycleRuntimeContext = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.entity.createEntityDefinition", function(){node_createEntityDefinition = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComplexEntityRuntimeContainer", function(){node_createComplexEntityRuntimeContainer = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.buildComplexEntityCoreObject", function(){node_buildComplexEntityCoreObject = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createComplexEntityEnvInterface", function(){node_createComplexEntityEnvInterface = this.getData();});

nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestComplexScriptPlugin", function(){node_createTestComplexScriptPlugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestSimple1Plugin", function(){node_createTestSimple1Plugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestDecoration1Plugin", function(){node_createTestDecoration1Plugin = this.getData();});

//Register Node by Name
packageObj.createChildNode("createComplexEntityRuntimeService", node_createComplexEntityRuntimeService); 

})(packageObj);
