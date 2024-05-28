//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_newOrderedContainer;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSet;
	var node_ServiceInfo;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;
	var node_getObjectType;
	var node_getBasicEntityObjectInterface;
	var node_complexEntityUtility;

//*******************************************   Start Node Definition  **************************************

var node_makeObjectEntityObjectInterface = function(rawEntity, valueContextId, bundleCore){
	
	var loc_rawEntity = rawEntity;
	
	var loc_valueContextId = valueContextId;
	
	var loc_bundleCore = bundleCore;
	
	var loc_interfaceEntity = {

		getEntityInitRequest : function(handlers, request){   return loc_rawEntity.getEntityInitRequest==undefined?undefined:loc_rawEntity.getEntityInitRequest(handlers, request);     },

		getValueContextId : function(){   return loc_valueContextId;   },
		
		getValueContext : function(){    return loc_bundleCore.getVariableDomain().getValueContext(loc_valueContextId);   },

		getBundle : function(){   return loc_bundleCore;  },
		
	};

	var loc_createAdaptersRequest = function(attrDef, baseObj, handlers, request){
		var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createAdaptersRequest", {}), handlers, request);

		var adaptersRequest = node_createServiceRequestInfoSet(new node_ServiceInfo("createAdapters", {}), {
			success : function(request, adaptersResult){
				return adaptersResult.getResults();
			}
		});
		
		var adapterNames = attrDef.getAdapterNames();
		_.each(adapterNames, function(adapterName){
			var adapterValueWrapper = attrDef.getAdapterValueWrapper(adapterName);								
			if(adapterValueWrapper.getValueType()==node_COMMONCONSTANT.EMBEDEDVALUE_TYPE_BRICK){
				var adapterEntityDef = adapterValueWrapper.getEntityDefinition();
				adaptersRequest.addRequest(adapterName, nosliw.runtime.getComplexEntityService().getCreateAdapterRequest(adapterEntityDef, node_complexEntityUtility.getCoreEntity(baseObj)));
			}
		});
		
		out.addRequest(adaptersRequest);
		return out;
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
				return this.createChildByAttributeRequest(attrName, attrName, variationPoints, handlers, request);
			},


			createChildByAttributeRequest : function(childName, attrName, variationPoints, handlers, request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexAttribute", {}), handlers, request);
				
				var complexEntityDef = basicEntityInterface.getEntityDefinition();
				var configure = basicEntityInterface.getConfigure();
				var attrDef = complexEntityDef.getAttribute(attrName);

				var childConfigure = configure.getChildConfigure(attrName);

				var attrValueWrapper = attrDef.getAttributeValueWrapper();
	
				if(attrValueWrapper.getValueType()==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_BRICK){
					var attrEntityDef = attrValueWrapper.getEntityDefinition();

					out.addRequest(nosliw.runtime.getComplexEntityService().getCreateEntityRuntimeRequest(attrEntityDef, loc_out, loc_bundleCore, variationPoints, childConfigure, {
						success : function(request, entityRuntime){
							node_getEntityTreeNodeInterface(entityRuntime.getCoreEntity()).setParentCore(rawEntity);
							return loc_createAdaptersRequest(attrDef, entityRuntime, {
								success : function(request, adapters){
									return treeNodeEntityInterface.addChild(childName, entityRuntime, adapters, true);
								}
							});
						}
					}));
				}
				else if(attrValueWrapper.getValueType()==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID){
					//external bundle reference attribute
					
					out.addRequest(nosliw.runtime.getComplexEntityService().getCreateBundleRuntimeRequest(attrValueWrapper.getResourceId(), childConfigure, {
						success: function(request, bundleRuntime){
							node_getEntityTreeNodeInterface(bundleRuntime.getCoreEntity()).setParentCore(rawEntity);
							return loc_createAdaptersRequest(attrDef, bundleRuntime, {
								success : function(request, adapters){
									return treeNodeEntityInterface.addChild(childName, bundleRuntime, adapters, true);
								}
							});
						}
					}));
				}

				return out;
			},
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

	var loc_parentCore;

	var loc_interfaceEntity = {

		getParentCore : function(){   return loc_parentCore;     },
		setParentCore : function(parentCore){    loc_parentCore = parentCore;    },

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
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){	node_createServiceRequestInfoSet = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.getBasicEntityObjectInterface", function(){node_getBasicEntityObjectInterface = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();});



//Register Node by Name
packageObj.createChildNode("makeObjectEntityObjectInterface", node_makeObjectEntityObjectInterface); 
packageObj.createChildNode("getEntityObjectInterface", node_getEntityObjectInterface); 
packageObj.createChildNode("makeObjectEntityTreeNodeInterface", node_makeObjectEntityTreeNodeInterface); 
packageObj.createChildNode("getEntityTreeNodeInterface", node_getEntityTreeNodeInterface); 
packageObj.createChildNode("buildComplexEntityCreationVariationPointObject", node_buildComplexEntityCreationVariationPointObject); 


})(packageObj);
