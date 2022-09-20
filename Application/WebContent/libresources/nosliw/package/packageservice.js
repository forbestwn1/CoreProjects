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
	var node_createComponentRuntime;
	var node_componentUtility;
	var node_createPackageCore;
	var node_createApplication;
	var node_createLifeCycleRuntimeContext;
	var node_buildComponentCore;
	var node_makeObjectWithComponentManagementInterface;
	
	var node_createTestComplex1Plugin;
	var node_createTestDecoration1Plugin;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createPackageRuntimeService = function() {
	
	var loc_complexEntityPlugins = {};

	var loc_createComplexEntityRuntime = function(complexEntityId, parentComplexEntityCore, bundleCore, configure, request){
		complexEntityId = new node_EntityIdInDomain(complexEntityId);
		
		//get component definition
		var entityDefDomain = bundleCore.getBundleDefinition()[node_COMMONATRIBUTECONSTANT.EXECUTABLEBUNDLE_EXECUTABLEENTITYDOMAIN];
		var complexEntityInfo = entityDefDomain[node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_COMPLEXENTITY][complexEntityId.literateStr];

		var complexEntityDef = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE_ENTITY];
		if(complexEntityDef!=undefined){
			//internal entity
			//build variableGroup
			var variableGroupId = null;
			var variableDomain = bundleCore.getVariableDomain();
			var valueStructureComplexDef = complexEntityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_VALUESTRUCTURECOMPLEX];
			variableGroupId = variableDomain.creatVariableGroup(valueStructureComplexDef, parentComplexEntityCore==undefined?undefined : parentComplexEntityCore.getVariableGroupId());
			
			//new complexCore through complex plugin
			var complexEntityPlugin = loc_complexEntityPlugins[complexEntityId[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE]];
			var componentCore = complexEntityPlugin.createComplexEntityCore(complexEntityDef, variableGroupId, bundleCore, configure);
			
			//build decorationInfos
			var decorationInfos = null;
			
			//create runtime
			return node_createComponentRuntime(componentCore, decorationInfos, request);
		}
		else{
			//refer to external Entity (bundle)
			var externalEntityId = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE_EXTERNALCOMPLEXENTITYID];
			//create bundle runtime
			return loc_out.createBundleRuntime(externalEntityId, configure, request);
		}
	};

	var loc_init = function(){
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, node_createTestComplex1Plugin());
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_DECORATION1, node_createTestDecoration1Plugin());
	};


	var loc_out = {

		getCreateApplicationRequest : function(resourceId, configure, runtimeContext, runtimeInterface, handlers, request){
			var application = node_createApplication(resourceId, configure);
			application = node_buildComponentCore(application);
			application = node_makeObjectWithComponentManagementInterface(application, application, application);

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
			return node_createComponentRuntime(node_createPackageCore(packageResourceId, runtimeConfigureInfo.coreConfigure), runtimeConfigureInfo.decorations, request); 
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
			return node_createComponentRuntime(node_createBundleCore(globalComplexEntitId, configure), undefined);
		},

		createComplexEntityRuntime : function(complexEntityId, parentCore, bundleCore, configure, request){
			return loc_createComplexEntityRuntime(complexEntityId, parentCore, bundleCore, configure, request);
		},
		
		registerComplexEntityPlugin : function(entityType, complexEntityPlugin){
			loc_complexEntityPlugins[entityType] = node_buildComplexEntityPlugInObject(complexEntityPlugin);
		}
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
nosliw.registerSetNodeDataEvent("package.createBundleCore", function(){node_createBundleCore = this.getData();});
nosliw.registerSetNodeDataEvent("package.entity.EntityIdInDomain", function(){node_EntityIdInDomain = this.getData();});
nosliw.registerSetNodeDataEvent("package.buildComplexEntityPlugInObject", function(){node_buildComplexEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("package.createPackageCore", function(){node_createPackageCore = this.getData();});
nosliw.registerSetNodeDataEvent("package.createApplication", function(){node_createApplication = this.getData();});
nosliw.registerSetNodeDataEvent("component.buildComponentCore", function(){node_buildComponentCore = this.getData();});
nosliw.registerSetNodeDataEvent("component.createLifeCycleRuntimeContext", function(){node_createLifeCycleRuntimeContext = this.getData();});
nosliw.registerSetNodeDataEvent("component.makeObjectWithComponentManagementInterface", function(){node_makeObjectWithComponentManagementInterface = this.getData();});

nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});
nosliw.registerSetNodeDataEvent("testcomponent.createTestDecoration1Plugin", function(){node_createTestDecoration1Plugin = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageRuntimeService", node_createPackageRuntimeService); 

})(packageObj);
