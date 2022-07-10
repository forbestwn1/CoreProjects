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
	var node_createPackageCore;
	
	var node_createTestComplex1Plugin;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_createPackageRuntimeService = function() {
	
	var loc_complexEntityPlugins = {};

	//create component runtime object
	var loc_getCreateComponentRuntimeRequest = function(complexEntityId, parentRuntime, bundleCore, configure, runtimeContext, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexRuntime"), handlers, request);

		complexEntityId = new node_EntityIdInDomain(complexEntityId);
		
		//get component definition
		var entityDefDomain = bundleCore.getBundleDefinition()[node_COMMONATRIBUTECONSTANT.EXECUTABLEBUNDLECOMPLEXRESOURCE_EXECUTABLEENTITYDOMAIN];
		var complexEntityInfo = entityDefDomain[node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_COMPLEXENTITY][complexEntityId.literateStr];

		var complexEntity = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE_ENTITY];
		if(complexEntity!=undefined){
			//internal entity
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				//build variableGroup
				var variableGroupId = null;
				var variableDomain = bundleCore.getVariableDomain();
				var valueStructureComplex = complexEntity[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITYCOMPLEX_VALUESTRUCTURECOMPLEX];
				variableGroupId = variableDomain.creatVariableGroup(valueStructureComplex, parentRuntime==undefined?undefined : parentRuntime.getVariableGroupId());
				
				//new complexCore through complex plugin
				var complexEntityPlugin = loc_complexEntityPlugins[complexEntityId[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE]];
				var componentCore = complexEntityPlugin.createComplexEntityCore(complexEntity, variableGroupId, bundleCore, configure);
				
				//build decorationInfos
				var decorationInfos = null;
				
				//create runtime
				var runtime = node_createComponentRuntime(componentCore, decorationInfos, runtimeContext, request);
				return runtime;
			}));
		}
		else{
			//refer to external Entity (bundle)
			var externalEntityId = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE.EXTERNALCOMPLEXENTITYID];
			//create bundle runtime
			out.addRequest(loc_getCreateBundleRuntimeRequest(externalEntityId, configure, runtimeContext));
		}
		return out;
	};

	var loc_getCreateBundleRuntimeRequest = function(globalComplexEntitId, configure, runtimeContext, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createBundleRuntime"), handlers, request);

		//load related resources
		var resourceId = globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_ROOTRESOURCEID];
		out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceId, {
			success : function(requestInfo, resourceTree){
				//get bundle definition
				var bundleDefinition = node_resourceUtility.getResourceFromTree(resourceTree, resourceId).resourceData;
				//create bundle runtime
				var bundleCore = node_createBundleCore(globalComplexEntitId, bundleDefinition, configure);
				var bundleRuntime = node_createComponentRuntime(bundleCore, undefined, runtimeContext, request);
				//create component runtime
				return loc_getCreateComponentRuntimeRequest(globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_ENTITYIDINDOMAIN], undefined, bundleCore, configure, runtimeContext, {
					success : function(request, componetRuntime){
						bundleCore.setMainComponent(componetRuntime);
						return bundleRuntime;
					}
				});
 			}
		}));
		return out;
	};
	
	var loc_getCreatePackageRuntimeRequest = function(resourceId, configure, runtimeContext, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExectuePackage", {"resourceId":resourceId}), handlers, request);

		//state backup service for this package
		if(runtimeContext.backupState==undefined) runtimeContext.backupState = node_createStateBackupService(resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE], resourceId[[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]], "1.0.0", nosliw.runtime.getStoreService());

		var gatewayParm = {};
		gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYPACKAGE_COMMAND_LOADEXECUTABLEPACKAGE_RESOURCEID] = resourceId;
		out.addRequest(nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(
				node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_PACKAGE, 
				node_COMMONATRIBUTECONSTANT.GATEWAYPACKAGE_COMMAND_LOADEXECUTABLEPACKAGE, 
				gatewayParm,
				{
					success : function(requestInfo, packageDef){
						var packageRuntimeRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("createPackageRuntime"));
						
						var packageCore = node_createPackageCore(packageDef, configure);
						var packageRuntime = node_createComponentRuntime(packageCore, undefined, runtimeContext, request);

						//load all related resources first
						packageRuntimeRequest.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(packageDef[node_COMMONATRIBUTECONSTANT.PACKAGEEXECUTABLE_DEPENDENCY], {
							success : function(requestInfo, resourceTree){
								var kkkk = 5555;
								kkkk++;
							}
						}));
						
						//create bundle runtime
						packageRuntimeRequest.addRequest(loc_getCreateBundleRuntimeRequest(packageDef[node_COMMONATRIBUTECONSTANT.PACKAGEEXECUTABLE_MAINENTITYID], configure, runtimeContext, {
							success : function(request, bundleRuntime){
								packageCore.setMainBundleRuntime(bundleRuntime);
								return packageRuntime;
							}
						}));
						return packageRuntimeRequest;
					}
				}
		));
		return out;
	};

	var loc_init = function(){
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, node_createTestComplex1Plugin());
	};
	
	var loc_out = {

		getCreatePackageRuntimeRequest : function(resourceId, configure, runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreatePackageRuntime", {"resourceId":resourceId}), handlers, request);
			out.addRequest(node_createComponentRuntime(node_createPackageCore(resourceId, configure), undefined, {
				success : function(request, packageRuntime){
					return packageRuntime.getInitRequest(runtimeContext, {
						success : function(request){
							return packageRuntime;
						}
					});
				}
			}));
			return out;
		},
		
		executeCreatePackageRuntimeRequest : function(resourceId, configure, runtimeContext, handlers, request){
			var requestInfo = this.getCreatePackageRuntimeRequest(resourceId, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		getCreateBundleRuntimeRequest : function(globalComplexEntitId, configure, runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateBundleRuntime", {"globalComplexEntitId":globalComplexEntitId}), handlers, request);
			out.addRequest(node_createComponentRuntime(node_createBundleCore(globalComplexEntitId, configure), undefined, {
				success : function(request, bundleRuntime){
					return bundleRuntime.getInitRequest(runtimeContext, {
						success : function(request){
							return bundleRuntime;
						}
					});
				}
			}));
			return out;
		},
		
		executeCreateBundleRuntimeRequest : function(globalComplexEntitId, configure, runtimeContext, handlers, request){
			var requestInfo = getCreateBundleRuntimeRequest(globalComplexEntitId, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		
		
		getCreateComponentRuntimeRequest : function(complexEntityId, parentRuntime, bundleCore, configure, runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateComplexEntityRuntime", {}), handlers, request);
			out.addRequest(node_createComponentRuntime(loc_createComplexEntityCore(complexEntityId, parentRuntime, bundleCore, configure), undefined, {
				success : function(request, complexEntityRuntime){
					return complexEntityRuntime.getInitRequest(runtimeContext, {
						success : function(request){
							return complexEntityRuntime;
						}
					});
				}
			}));
			return out;
		},
		
		executeCreateComponentRuntimeRequest : function(complexEntityId, parentRuntime, bundleRuntime, configure, runtimeContext, handlers, request){
			var requestInfo = this.getCreateComponentRuntimeRequest(complexEntityId, parentRuntime, bundleRuntime, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
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
nosliw.registerSetNodeDataEvent("package.entity.createBundleCore", function(){node_createBundleCore = this.getData();});
nosliw.registerSetNodeDataEvent("package.entity.EntityIdInDomain", function(){node_EntityIdInDomain = this.getData();});
nosliw.registerSetNodeDataEvent("package.buildComplexEntityPlugInObject", function(){node_buildComplexEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("package.entity.createPackageCore", function(){node_createPackageCore = this.getData();});


nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageRuntimeService", node_createPackageRuntimeService); 

})(packageObj);
