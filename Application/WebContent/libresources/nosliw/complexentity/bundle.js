
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

//bundle is executable resource unit
var node_createBundleCore = function(parm, configure){

	var loc_globalComplexEntitId;
	
	var loc_bundleDef;
	var loc_mainEntityId;
	
	var loc_configure;
	var loc_configureValue;
	
	//variable domain for this bundle
	var loc_variableDomain;

	var loc_envInterface;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_init = function(parm, configure){
		loc_configure = configure;
		loc_configureValue = node_createConfigure(loc_configure).getConfigureValue();

		if(parm.bundleDef!=undefined){
			//parm is bundle entity
			loc_bundleDef = parm.bundleDef;
			loc_mainEntityId = parm.mainEntityId;
		}
		else{
			//parm is global complex entity id
			loc_globalComplexEntitId = parm;
			loc_mainEntityId = loc_globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_ENTITYIDINDOMAIN];
		}
	};
	
	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCoreBundle"), handlers, request);

		if(loc_globalComplexEntitId!=undefined){
			//load related resources
			var resourceId = loc_globalComplexEntitId[node_COMMONATRIBUTECONSTANT.IDCOMPLEXENTITYINGLOBAL_RESOURCEINFO][node_COMMONATRIBUTECONSTANT.INFORESOURCEIDNORMALIZE_ROOTRESOURCEID];
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(resourceId, {
				success : function(requestInfo, resourceTree){
					//get bundle definition
					loc_bundleDef = node_resourceUtility.getResourceFromTree(resourceTree, resourceId).resourceData;
	 			}
			}));
		}
		
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			//build variable domain in bundle
			loc_variableDomain = nod_createVariableDomain(loc_bundleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEBUNDLE_EXECUTABLEENTITYDOMAIN][node_COMMONATRIBUTECONSTANT.DOMAINENTITYEXECUTABLERESOURCECOMPLEX_VALUESTRUCTUREDOMAIN]);

			return nosliw.runtime.getComplexEntityService().getCreateComplexEntityRuntimeRequest(loc_mainEntityId, undefined, loc_out, undefined, loc_configure, {
				success : function(request, mainCoplexEntity){
					loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_MAIN_NAME, mainCoplexEntity, true);
				}
			});

		}));
		
		//build complex entity runtime
/*
		out.addRequest(nosliw.runtime.getComplexEntityService().getCreateComplexEntityRuntimeRequest(loc_mainEntityId, undefined, loc_out, loc_configure, {
			success : function(request, mainCoplexEntity){
				loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_MAIN_NAME, mainCoplexEntity, true);
			}
		}));
*/
		return out;
	};

	var loc_getMainEntity = function(){
		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_MAIN_NAME).getChildValue();
	};

	var loc_out = {

		getDataType: function(){    return  "bundle";   },

		getMainEntity : function(){    return loc_getMainEntity();     },

		getMainEntityRuntime : function(){ return this.getMainEntity();  },

		setEnvironmentInterface : function(envInterface){	loc_envInterface = envInterface;	},
		
		getPreInitRequest : function(handlers, request){   return loc_getPreInitRequest(handlers, request);	},

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
		
		updateView : function(view){    
			loc_parentView = view;
			loc_getMainEntity().updateView(view);     
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

	loc_init(parm, configure);
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
nosliw.registerSetNodeDataEvent("complexentity.createVariableDomain", function(){nod_createVariableDomain = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createBundleCore", node_createBundleCore); 

})(packageObj);
