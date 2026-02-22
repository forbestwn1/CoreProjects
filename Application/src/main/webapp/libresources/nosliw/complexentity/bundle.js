
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
	var nod_createValuePortDomain;
	var node_resourceUtility;
	var node_createPackageDebugView;
	var node_createConfigure;
	var node_basicUtility;
	var node_componentUtility;
	var node_namingConvensionUtility;
	var node_getEntityTreeNodeInterface;
	var node_buildInterface;
	var node_complexEntityUtility;
	var node_getObjectType;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_MAIN_NAME = "main";

//bundle is executable resource unit
var node_createBundleCore = function(parm, configure){

	var loc_resourceId;
	var loc_bundleDef;

	var loc_configure;
	var loc_configureValue;
	
	//variable domain for this bundle
	var loc_valueportDomain;

	var loc_envInterface;
	
	var loc_runtimeEnv;

	var loc_parentView;
	
	var loc_dynamicInputContainer;
	
	var loc_init = function(parm, configure){
		loc_configure = configure;
		loc_configureValue = node_createConfigure(loc_configure).getConfigureValue();

		if(parm.bundleDef!=undefined){
			//parm is bundle entity
			loc_bundleDef = parm.bundleDef;
		}
		else{
			//parm is global complex entity id
			loc_resourceId = parm;
		}
	};
	
	var loc_getPreInitRequest = function(handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PreInitCoreBundle"), handlers, request);

		if(loc_resourceId!=undefined){
			//load related resources
			out.addRequest(nosliw.runtime.getResourceService().getGetResourcesRequest(loc_resourceId, {
				success : function(requestInfo, resourceTree){
					//get bundle definition
					loc_bundleDef = node_resourceUtility.getResourceFromTree(resourceTree, loc_resourceId).resourceData;

					//build variable domain in bundle
					loc_valueportDomain = nod_createValuePortDomain(loc_bundleDef[node_COMMONATRIBUTECONSTANT.RESOURCEDATABRICK_VALUESTRUCTUREDOMAIN]);

					var branchBrickRequest = node_createServiceRequestInfoSequence(undefined, undefined, requestInfo);
					var brickDefs = loc_bundleDef[node_COMMONATRIBUTECONSTANT.RESOURCEDATABRICK_SUPPORTBRICKS];
					_.each(brickDefs, function(brickDef, name){
						branchBrickRequest.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
							//build variable domain in bundle
//							loc_valueportDomain = nod_createValuePortDomain(loc_bundleDef[node_COMMONATRIBUTECONSTANT.RESOURCEDATABRICK_VALUESTRUCTUREDOMAIN]);
							return nosliw.runtime.getComplexEntityService().getCreateBrickRuntimeRequest(brickDef, undefined, loc_out, undefined, loc_configure, {
								success : function(request, mainEntityRuntime){
									node_getEntityTreeNodeInterface(mainEntityRuntime.getCoreEntity()).setParentCore(loc_out);
									loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(name, mainEntityRuntime, true);
								}
							});
				
						}));
					});
					return branchBrickRequest;
	 			}
			}));
		}
		
		//main brick
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			var entityDef = loc_bundleDef[node_COMMONATRIBUTECONSTANT.RESOURCEDATABRICK_BRICK];
			return nosliw.runtime.getComplexEntityService().getCreateBrickRuntimeRequest(entityDef, undefined, loc_out, undefined, loc_configure, {
				success : function(request, mainEntityRuntime){
					node_getEntityTreeNodeInterface(mainEntityRuntime.getCoreEntity()).setParentCore(loc_out);
					loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].addChild(loc_MAIN_NAME, mainEntityRuntime, true);
					return node_complexEntityUtility.getBuildAttributeWithResourceId(mainEntityRuntime.getCoreEntity());
				}
			});

		}));
		
		return out;
	};

	var loc_getMainEntity = function(){
		var mainChild = loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_MAIN_NAME);
		if(mainChild!=undefined)	return mainChild.getChildValue();
	};

	var loc_valueContainer = {
		
		getGetValueRequest : function(categary, name, handlers, request){ 
			if(categary!=undefined&&categar!=node_COMMONCONSTANT.VALUEADDRESSCATEGARY_BUNDLE)  return;
			var complexPath = node_namingConvensionUtility.parseComplexPath(name);
			if(complexPath.root=="#dynamicTask"){
				return loc_dynamicInputContainer.getDyanmicTaskInputRequest(complexPath.path, handlers, request);
			}
		},
		
		getAllValueInfoRequest : function(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
				return {
					categary : node_COMMONCONSTANT.VALUEADDRESSCATEGARY_BUNDLE
				};
			}));
			return out;
		},
	};

	var loc_out = {

		getDataType: function(){    return  "bundle";   },

		getMainEntityCore : function(){
			var mainEntity = loc_getMainEntity();
			if(mainEntity!=undefined)	return mainEntity.getCoreEntity();     
		},

		getMainEntityRuntime : function(){ return loc_getMainEntity();  },

		getMainEntityNode : function(){		return loc_envInterface[node_CONSTANT.INTERFACE_TREENODEENTITY].getChild(loc_MAIN_NAME);	},

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

		getValuePortDomain : function(){		return loc_valueportDomain;	},

		getUpdateRuntimeEnvRequest : function(runtimeEnv, handlers, request){
			return node_createServiceRequestInfoSimple(undefined, function(request){
				loc_runtimeEnv = runtimeEnv;
			}, handlers, request);
		},
		
		setDynamicInputContainer : function(dynamicInputContainer){		loc_dynamicInputContainer = dynamicInputContainer;		},
		
		getDynamicInputContainer : function(){	return loc_dynamicInputContainer;		},
		
		getValueContainer : function(){		return loc_valueContainer;	},
		
	};
	
	loc_out = node_buildInterface(loc_out, node_CONSTANT.INTERFACE_VALUECONTAINERPROVIDER, loc_out);
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
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("valueport.createValuePortDomain", function(){nod_createValuePortDomain = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("debug.createPackageDebugView", function(){node_createPackageDebugView = this.getData();});
nosliw.registerSetNodeDataEvent("component.createConfigure", function(){node_createConfigure = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("component.componentUtility", function(){node_componentUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.getEntityTreeNodeInterface", function(){node_getEntityTreeNodeInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});

//Register Node by Name
packageObj.createChildNode("createBundleCore", node_createBundleCore); 

})(packageObj);
