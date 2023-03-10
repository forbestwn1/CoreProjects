//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSequence;
	var node_makeObjectWithType;
	var nod_createVariableDomain;
	var node_resourceUtility;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_MAIN_NAME = "main";
	
var node_createBundleCore = function(globalComplexEntitId, configure){

	var loc_id;
	
	var loc_globalComplexEntitId = globalComplexEntitId;
	
	var loc_bundleDef;
	
	var loc_configure = configure;
	var loc_configureValue = node_createConfigure(configure).getConfigureValue();
	
	var loc_lifecycleEntity;
	var loc_backupState;
	
	//variable domain for this bundle
	var loc_variableDomain;

	var loc_mainComplexEntity;
	
	var loc_runtimeContext;
	
	var loc_runtimeContextForMain;

	var loc_debugMode;
	var loc_debugView;

	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_init = function(){
		var debugConf = loc_configureValue[node_basicUtility.buildNosliwFullName("debug_package")];
		if("true"==debugConf){
			//debug mode
			loc_debugMode = true;
		}
	};
	
	var loc_getDebugView = function(){
		if(loc_debugView==undefined){
			loc_debugView = node_createPackageDebugView("Bundle: "+loc_out.getDataType()+"_"+loc_out.getId(), "purple");
		}
		return loc_debugView;
	};
	
	var loc_isDebugMode = function(){
		return loc_debugMode == true;
	};
	
	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCoreBundle"), handlers, request);

		//load related resources
		var resourceId = loc_globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_RESOURCEINFO][node_COMMONATRIBUTECONSTANT.INFORESOURCEIDNORMALIZE_ROOTRESOURCEID];
		out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceId, {
			success : function(requestInfo, resourceTree){
				//get bundle definition
				loc_bundleDef = node_resourceUtility.getResourceFromTree(resourceTree, resourceId).resourceData;
				//build variable domain in bundle
				loc_variableDomain = nod_createVariableDomain(loc_bundleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEBUNDLE_EXECUTABLEENTITYDOMAIN][node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_VALUESTRUCTUREDOMAIN]);
				//build complex entity runtime
				return nosliw.runtime.getPackageService().getCreateComplexEntityRuntimeRequest(loc_globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_ENTITYIDINDOMAIN], undefined, loc_out, configure, {
					success : function(request, mainCoplexEntity){
						loc_mainComplexEntity = mainCoplexEntity;
						return loc_mainComplexEntity.getPreInitRequest();
					}
				});
 			}
		}));
		return out;
	};

	var loc_out = {

		getDataType: function(){    return  "bundle";   },
		getId : function(){  return loc_id;   },
		setId : function(id){   loc_id = id;    },

		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},

		getUpdateRuntimeContextRequest : function(runtimeContext, handlers, request){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("UpdateRuntimeContextCoreBundle", {}), handlers, request);
			loc_runtimeContext = runtimeContext;
			loc_backupState= loc_runtimeContext.backupState;
			loc_lifecycleEntity = loc_runtimeContext.lifecycleEntity;
			
			loc_parentView = runtimeContext.view;
			
			loc_runtimeContextForMain = node_componentUtility.makeChildRuntimeContext(loc_runtimeContext, loc_MAIN_NAME, loc_mainComplexEntity); 

			if(loc_isDebugMode()){
				loc_runtimeContextForMain = loc_getDebugView().updateRuntimeContext(loc_runtimeContextForMain);
			}

			out.addRequest(loc_mainComplexEntity.getUpdateRuntimeContextRequest(loc_runtimeContextForMain));
			return out;

		},
			
		getLifeCycleRequest : function(transitName, handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DESTROY){
			}
			else{
				if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_INIT){
//					out.addRequest(loc_mainComplexEntity.getLifeCycleRequest(transitName));
				}
				else if(transitName==node_CONSTANT.LIFECYCLE_COMPONENT_TRANSIT_DEACTIVE){
				}
			}
			return out;
		},
		
		
		getBundleDefinition : function(){		return loc_bundleDef;	},
		
		getVariableDomain : function(){		return loc_variableDomain;	},

		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_runtimeEnv = runtimeEnv;
			}, handlers, request);
		},
		
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_BUNDLE);
	loc_out.id = nosliw.generateId();

	loc_init();
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.createVariableDomain", function(){nod_createVariableDomain = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createBundleCore", node_createBundleCore); 

})(packageObj);
