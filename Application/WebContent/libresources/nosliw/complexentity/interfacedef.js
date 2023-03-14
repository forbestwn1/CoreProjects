//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_newOrderedContainer;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;

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
		createComplexEntityCore : function(id, complexEntityDef, variableGroupId, bundleCore, configure){
			return {};
		}
	};
		
	return _.extend({}, interfaceDef, rawPluginObj);	
};


var node_makeObjectComplexEntityObjectInterface = function(rawEntity, valueContextId, bundleCore){
	
	var loc_rawEntity = rawEntity;
	
	var loc_valueContextId = valueContextId;
	
	var loc_bundleCore = bundleCore;
	
	var loc_interfaceEntity = {

		getComplexEntityInitRequest : function(handlers, request){   return loc_rawEntity.getComplexEntityInitRequest==undefined?undefined:loc_rawEntity.getComplexEntityInitRequest(handlers, request);     },

		getValueContextId : function(){   return loc_valueContextId;   },

	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	var treeNodeEntityInterface =  node_getEntityTreeNodeInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_COMPLEXENTITY, {
			createComplexAttributeRequest : function(attrName, complexEntityId, configure, handlers, request){
				var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexAttribute", {}), handlers, request);
				out.addRequest(nosliw.runtime.getComplexEntityService().getCreateComplexEntityRuntimeRequest(complexEntityId, loc_out, loc_bundleCore, configure, {
					success : function(request, complexEntityRuntime){
						treeNodeEntityInterface.addNormalChild(attrName, complexEntityRuntime);
					}
				}));
				return out;
			},
		});
	}
	
	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_COMPLEXENTITY, loc_interfaceEntity);
	return loc_out;
};

var node_getComplexEntityObjectInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_COMPLEXENTITY);
};



var node_makeObjectEntityTreeNodeInterface = function(rawEntity){
	
	var loc_children = node_newOrderedContainer();

	var loc_interfaceEntity = {

		getChildrenName : function(){   return loc_children.getAllKeys();   },
		
		getChild : function(childName){   return loc_children.getElement(childName);	},

		addNormalChild : function(childName, entityRuntime){
			loc_children.addElement(childName, loc_createNormalTreeChild(childName, entityRuntime));
		},
		
		addContainerChild : function(childName, containerValue){
			
		}
	};

	var embededEntityInterface =  node_getEmbededEntityInterface(rawEntity);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_TREENODEENTITY, {
			
			getChildrenName : function(){   return loc_interfaceEntity.getChildrenName();   },
			
			getChild : function(childName){   return loc_interfaceEntity.getChild(childName);	},
			
			addNormalChild : function(childName, entityRuntime){   loc_interfaceEntity.addNormalChild(childName, entityRuntime);  }
		});
	}
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_TREENODEENTITY, loc_interfaceEntity);
};
	
var node_getEntityTreeNodeInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_TREENODEENTITY);
};



var loc_createContainerTreeChild = function(){
	
	var loc_attributes = node_newOrderedContainer();
	
	var loc_out = {
		
		addAttribute : function(attribute){
			loc_attributes.addElement(attribute.getAttributeName(), attribute);
		},
		
	};
	
	return loc_out;
};

var loc_createNormalTreeChild = function(childName, entityRuntime){
	
	var loc_childName = childName;
	
	var loc_entityRuntime = entityRuntime;
	
	var loc_out = {
		
		getChildName : function(){   return loc_childName;   },
			
		getChildValue : function(){    return loc_entityRuntime;   },
		
		getChildType : function(){   return node_CONSTANT.ATTRIBUTE_TYPE_NORMAL;   },
		
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
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.embeded.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});


//Register Node by Name
packageObj.createChildNode("buildComplexEntityPlugInObject", node_buildComplexEntityPlugInObject); 
packageObj.createChildNode("buildSimpleEntityPlugInObject", node_buildSimpleEntityPlugInObject); 
packageObj.createChildNode("makeObjectComplexEntityObjectInterface", node_makeObjectComplexEntityObjectInterface); 
packageObj.createChildNode("getComplexEntityObjectInterface", node_getComplexEntityObjectInterface); 
packageObj.createChildNode("makeObjectEntityTreeNodeInterface", node_makeObjectEntityTreeNodeInterface); 
packageObj.createChildNode("getEntityTreeNodeInterface", node_getEntityTreeNodeInterface); 

})(packageObj);
