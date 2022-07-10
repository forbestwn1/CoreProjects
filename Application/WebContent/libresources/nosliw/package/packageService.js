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
	var loc_getCreateComplexEntityRuntimeRequest = function(complexEntityId, parentComplexEntityCore, bundleCore, configure, runtimeContext, handlers, request){
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
				variableGroupId = variableDomain.creatVariableGroup(valueStructureComplex, parentComplexEntityCore==undefined?undefined : parentComplexEntityCore.getVariableGroupId());
				
				//new complexCore through complex plugin
				var complexEntityPlugin = loc_complexEntityPlugins[complexEntityId[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE]];
				var componentCore = complexEntityPlugin.createComplexEntityCore(complexEntity, variableGroupId, bundleCore, configure);
				
				//build decorationInfos
				var decorationInfos = null;
				
				//create runtime
				var complexEntityRuntime = node_createComponentRuntime(componentCore, decorationInfos, runtimeContext, request);

				//runtime init
				return complexEntityRuntime.getInitRequest(runtimeContext, {
					success : function(request){
						return complexEntityRuntime;
					}
				});
			}));
		}
		else{
			//refer to external Entity (bundle)
			var externalEntityId = complexEntityInfo[node_COMMONATRIBUTECONSTANT.INFOENTITYINDOMAINEXECUTABLE.EXTERNALCOMPLEXENTITYID];
			//create bundle runtime
			out.addRequest(loc_out.getCreateBundleRuntimeRequest(externalEntityId, configure, runtimeContext));
		}
		return out;
	};

	var loc_init = function(){
		loc_out.registerComplexEntityPlugin(node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, node_createTestComplex1Plugin());
	};
	
	var loc_out = {

		getCreatePackageRuntimeRequest : function(resourceId, configure, runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreatePackageRuntime", {"resourceId":resourceId}), handlers, request);

			if(runtimeContext.backupState==undefined) runtimeContext.backupState = node_createStateBackupService(resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE], resourceId[[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]], "1.0.0", nosliw.runtime.getStoreService());			
			var packageRuntime = node_createComponentRuntime(node_createPackageCore(resourceId, configure), undefined, out);
			out.addRequest(packageRuntime.getInitRequest(runtimeContext, {
				success : function(request){
					return packageRuntime;
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
			var bundleRuntime = node_createComponentRuntime(node_createBundleCore(globalComplexEntitId, configure), undefined, out);
			out.addRequest(bundleRuntime.getInitRequest(runtimeContext, {
				success : function(request){
					return bundleRuntime;
				}
			}));
			return out;
		},
		
		executeCreateBundleRuntimeRequest : function(globalComplexEntitId, configure, runtimeContext, handlers, request){
			var requestInfo = getCreateBundleRuntimeRequest(globalComplexEntitId, configure, runtimeContext, handlers, request);
			node_requestServiceProcessor.processRequest(requestInfo);
		},		
		
		getCreateComplexEntityRuntimeRequest : function(complexEntityId, parentCore, bundleCore, configure, runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("CreateComplexEntityRuntime", {}), handlers, request);
			out.addRequest(loc_getCreateComplexEntityRuntimeRequest(complexEntityId, parentCore, bundleCore, configure, runtimeContext, {
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
		
		executeCreateComplexEntityRuntimeRequest : function(complexEntityId, parentCore, bundleCore, configure, runtimeContext, handlers, request){
			var requestInfo = this.getCreateComplexEntityRuntimeRequest(complexEntityId, parentCore, bundleCore, configure, runtimeContext, handlers, request);
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
nosliw.registerSetNodeDataEvent("package.createBundleCore", function(){node_createBundleCore = this.getData();});
nosliw.registerSetNodeDataEvent("package.entity.EntityIdInDomain", function(){node_EntityIdInDomain = this.getData();});
nosliw.registerSetNodeDataEvent("package.buildComplexEntityPlugInObject", function(){node_buildComplexEntityPlugInObject = this.getData();});
nosliw.registerSetNodeDataEvent("component.createComponentRuntime", function(){node_createComponentRuntime = this.getData();});
nosliw.registerSetNodeDataEvent("package.createPackageCore", function(){node_createPackageCore = this.getData();});


nosliw.registerSetNodeDataEvent("testcomponent.createTestComplex1Plugin", function(){node_createTestComplex1Plugin = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPackageRuntimeService", node_createPackageRuntimeService); 

})(packageObj);
