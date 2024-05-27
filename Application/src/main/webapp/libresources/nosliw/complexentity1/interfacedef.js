//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_newOrderedContainer;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;
	var node_getObjectType;

//*******************************************   Start Node Definition  **************************************
	
//interface for plug in for complex entity,
//it create component core object
var node_buildComplexEntityPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		createComplexEntityCore : function(id, complexEntityDef, variableGroupId, bundleCore, configure){
			return {};
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};

var node_buildSimpleEntityPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		getCreateEntityRequest : function(entityDef, configure, handlers, request){
						
		},
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};

var node_buildAdapterPlugInObject = function(rawPluginObj){

	var interfaceDef = {
		//create component core object
		getNewAdapterRequest : function(adapterDefinition, handlers, request){
			return node_createServiceRequestInfoSimple({}, function(request){
				return {
					getExecuteRequest : function(parent, child, handlers, request){
						return;
					}
				};
			}, handlers, request);
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};

var node_makeObjectBasicEntityObjectInterface = function(rawEntity, entityDefinition, configure){

	var loc_rawEntity = rawEntity;
	
	var loc_entityDefinition = entityDefinition;
	
	var loc_configure = configure;
	
	var loc_extraData = {};
	
	var loc_interfaceEntity = {
		getConfigure : function(){    return loc_configure;     },
		getEntityDefinition : function(){   return loc_entityDefinition;    },
		getExtraData : function(name){   return loc_extraData[name];    },
		setExtraData : function(name, data){    loc_extraData[name] = data;   },
	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_BASICENTITY, {});
	}

	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_BASICENTITY, loc_interfaceEntity);
	return loc_out;
};

var node_getBasicEntityObjectInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_BASICENTITY);
};


var node_makeObjectComplexEntityObjectInterface = function(rawEntity, valueContextId, bundleCore){
	
	var loc_rawEntity = rawEntity;
	
	var loc_valueContextId = valueContextId;
	
	var loc_bundleCore = bundleCore;
	
	var loc_interfaceEntity = {

		getEntityInitRequest : function(handlers, request){   return loc_rawEntity.getEntityInitRequest==undefined?undefined:loc_rawEntity.getEntityInitRequest(handlers, request);     },

		getValueContextId : function(){   return loc_valueContextId;   },
		
		getValueContext : function(){    return loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);   },

		getBundle : function(){   return loc_bundleCore;  },
		
	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	var treeNodeEntityInterface =  node_getEntityTreeNodeInterface(rawEntity);
	var basicEntityInterface = node_getBasicEntityObjectInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_ENTITY, {

/*			
			getExecuteAdapterRequest : function(attrName){
				treeNodeEntityInterface.getChild(attrName).getAdapter().getExecuteRequest(loc_out, child.getChildValue());
			},
*/

			createAttributeRequest : function(attrName, variationPoints, handlers, request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexAttribute", {}), handlers, request);
				
				var complexEntityDef = basicEntityInterface.getEntityDefinition();
				var configure = basicEntityInterface.getConfigure();
				var attr = complexEntityDef.getAttribute(attrName);
				
				var attrValue = attr.getValue();
				var entityType = attr.getEntityType();
				var adaptersInfo = attr.getAdaptersInfo();
				var childConfigure = configure.getChildConfigure(attrName);
				if(attr.isExternalReference()==true){
					//external bundle reference attribute
					var externalBundleRuntime = nosliw.runtime.getComplexEntityService().createBundleRuntime(attrValue[node_COMMONATRIBUTECONSTANT.REFERENCEEXTERNAL_NORMALIZEDRESOURCEID], childConfigure, request);
					var adaptersRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("createAdapters", {}), {
						success : function(request, adaptersResult){
							return treeNodeEntityInterface.addChild(attrName, externalBundleRuntime, adaptersResult.getResults(), true);
						}
					});
					
					_.each(adaptersInfo, function(adapterInfo){
						adaptersRequest.addRequest(adapterInfo.name, nosliw.runtime.getComplexEntityService().getCreateAdapterRequest(adapterInfo.valueType, adapterInfo.value));
					});
					out.addRequest(adaptersRequest);
				}
				else if(attr.isComplex()==true){
					//complex attribute
					var childEntitId = attrValue;

					out.addRequest(nosliw.runtime.getComplexEntityService().getCreateComplexEntityRuntimeRequest(childEntitId, loc_out, loc_bundleCore, variationPoints, childConfigure, {
						success : function(request, complexEntityRuntime){
							
							var adaptersRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("createAdapters", {}), {
								success : function(request, adaptersResult){
									return treeNodeEntityInterface.addChild(attrName, complexEntityRuntime, adaptersResult.getResults(), true);
								}
							});
							
							_.each(adaptersInfo, function(adapterInfo){
								adaptersRequest.addRequest(adapterInfo.name, nosliw.runtime.getComplexEntityService().getCreateAdapterRequest(adapterInfo.valueType, adapterInfo.value));
							});
							return adaptersRequest;
						}
					}));
				}
				else{
					//simple attribute
					var childEntityDef = attrValue;
					out.addRequest(nosliw.runtime.getComplexEntityService().getCreateSimpleEntityRequest(entityType, childEntityDef, childConfigure, {
						success : function(request, simpleEntity){
							var adaptersRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("createAdapters", {}), {
								success : function(request, adaptersResult){
									return treeNodeEntityInterface.addChild(attrName, simpleEntity, adaptersResult.getResults(), false);
								}	
							});
							
							_.each(adaptersInfo, function(adapterInfo){
								adaptersRequest.addRequest(adapterInfo.name, nosliw.runtime.getComplexEntityService().getCreateAdapterRequest(adapterInfo.valueType, adapterInfo.value));
							});
							return adaptersRequest;
						}
					}));
				}
				return out;
			}
		});
	}
	
	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_ENTITY, loc_interfaceEntity);
	return loc_out;
};


var node_getEntityObjectInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_ENTITY);
};

var node_makeObjectEntityTreeNodeInterface = function(rawEntity){
	
	var loc_children = node_newOrderedContainer();

	var loc_interfaceEntity = {

		getChildrenName : function(){   return loc_children.getAllKeys();   },
		
		getChild : function(childName){   return loc_children.getElement(childName);	},

		addChild : function(childName, entityRuntime, adapters, isComplex){
			//avoid duplicated name
			while(this.getChild(childName)!=null){
				childName = childName + "_1";
			}
			
			var child = loc_createTreeNodeChild(childName, entityRuntime, adapters, isComplex);
			loc_children.addElement(childName, child);
			return child;
		},
		
	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_TREENODEENTITY, {
			
			getChildrenName : function(){   return loc_interfaceEntity.getChildrenName();   },
			
			getChild : function(childName){   return loc_interfaceEntity.getChild(childName);	},
			
			addChild : function(childName, entityRuntime, adapters, isComplex){   return loc_interfaceEntity.addChild(childName, entityRuntime, adapters, isComplex);  },
			
			processChildren : function(processFun){
				var that = this;
				_.each(this.getChildrenName(), function(childName){
					var child = that.getChild(childName);
					processFun.call(child, child);
				});
			},
			
			processComplexChildren : function(processFun){
				var that = this;
				_.each(this.getChildrenName(), function(childName){
					var child = that.getChild(childName);
					if(child.getIsComplex()==true){
						processFun.call(child, child);
					}
				});
			}
		});
	}
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_TREENODEENTITY, loc_interfaceEntity);
};
	
var node_getEntityTreeNodeInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_TREENODEENTITY);
};

var loc_createTreeNodeChild = function(childName, entityRuntime, adapters, isComplex){
	
	var loc_childName = childName;
	
	var loc_entityRuntime = entityRuntime;
	
	var loc_adapters = adapters;
	
	var loc_isComplex = isComplex;
	
	var loc_out = {
		
		getChildName : function(){   return loc_childName;   },
			
		getChildValue : function(){    return loc_entityRuntime;   },
		
		getChildType : function(){   return node_CONSTANT.ATTRIBUTE_TYPE_NORMAL;   },
		
		getAdapters : function(){   return loc_adapters;    },
		
		getIsComplex : function(){    return loc_isComplex;     }
		
	};
	
	return loc_out;
};

//interface for ui tag core object
var node_buildComplexEntityCreationVariationPointObject = function(rawEntity){
	var loc_rawEntity = rawEntity;
	
	var loc_out = {
		afterValueContext : function(complexEntityDef, valueContextId, bundleCore, coreConfigure, handlers, request){
			if(loc_rawEntity==undefined)  return;   
			return loc_rawEntity.afterValueContext==undefined?undefined:loc_rawEntity.afterValueContext(complexEntityDef, valueContextId, bundleCore, coreConfigure, handlers, request);   
		},
	};
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.newOrderedContainer", function(){node_newOrderedContainer = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});



//Register Node by Name
packageObj.createChildNode("buildComplexEntityPlugInObject", node_buildComplexEntityPlugInObject); 
packageObj.createChildNode("buildSimpleEntityPlugInObject", node_buildSimpleEntityPlugInObject); 
packageObj.createChildNode("buildAdapterPlugInObject", node_buildAdapterPlugInObject); 
packageObj.createChildNode("makeObjectComplexEntityObjectInterface", node_makeObjectComplexEntityObjectInterface); 
packageObj.createChildNode("getEntityObjectInterface", node_getEntityObjectInterface); 
packageObj.createChildNode("makeObjectBasicEntityObjectInterface", node_makeObjectBasicEntityObjectInterface); 
packageObj.createChildNode("getBasicEntityObjectInterface", node_getBasicEntityObjectInterface); 
packageObj.createChildNode("makeObjectEntityTreeNodeInterface", node_makeObjectEntityTreeNodeInterface); 
packageObj.createChildNode("getEntityTreeNodeInterface", node_getEntityTreeNodeInterface); 
packageObj.createChildNode("buildComplexEntityCreationVariationPointObject", node_buildComplexEntityCreationVariationPointObject); 


})(packageObj);
