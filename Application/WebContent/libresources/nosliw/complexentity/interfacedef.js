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

var node_buildComplexEntityCoreObject = function(rawComplexEntityCore, valueContextId, bundleCore){
	
	var loc_rawComplexEntityCore = rawComplexEntityCore.getRawEntity==undefined?rawComplexEntityCore:rawComplexEntityCore.getRawEntity();
	
	var loc_valueContextId = valueContextId;
	
	var loc_bundleCore = bundleCore;
	
	var loc_children = node_newOrderedContainer();

	var loc_addChildComplexEntity = function(attrName, complexEntityRuntime){
		loc_children.addElement(attrName, loc_createComplexEntityAttribute(attrName, complexEntityRuntime));
	};
	
	var interfaceDef = {

		getRawEntity : function(){   return loc_rawComplexEntityCore;    },
			
		getComplexEntityInitRequest : function(handlers, request){   return loc_rawComplexEntityCore.getComplexEntityInitRequest==undefined?undefined:loc_rawComplexEntityCore.getComplexEntityInitRequest(handlers, request);     },
			
		
		getComplexEntityEnvInterface : function(){
			return {
				complexUtility : {
					createComplexAttributeRequest : function(attrName, complexEntityId, configure, handlers, request){
						var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("createComplexAttribute", {}), handlers, request);
						out.addRequest(nosliw.runtime.getComplexEntityService().getCreateComplexEntityRuntimeRequest(complexEntityId, loc_out, loc_bundleCore, configure, {
							success : function(request, complexEntityRuntime){
								loc_addChildComplexEntity(attrName, complexEntityRuntime);
							}
						}));
						return out;
					},
					
					getAttributesName : function(){   return loc_children.getAllKeys();	},
					
					getAttribute : function(attrName){   return loc_children.getElement(attrName);     },
					
				}	
			};
		},

		getValueContextId : function(){   return loc_valueContextId;   },
		
		
		addChildComplexEntity : function(attrName, complexEntityRuntime){},
		
		getChildrenComplexEntityNames : function(){},
		
		getChildComplexEntity : function(attrName){},
		
	
	};
		
	var loc_out = _.extend({}, interfaceDef, rawComplexEntityCore);
	return loc_out;
};


var loc_createComplexEntityAttribute = function(attrName, complexEntityRuntime){
	
	var loc_attrName = attrName;
	
	var loc_complexEntityRuntime = complexEntityRuntime;
	
	var loc_out = {
		
		getAttributeName : function(){   return loc_attrName;   },
			
		getAttributeValue : function(){    return loc_complexEntityRuntime;   },
		
		getAttributeType : function(){   return node_CONSTANT.ATTRIBUTE_TYPE_NORMAL;   },
		
		isComplexEntity : function(){   return true;    }
		
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


//Register Node by Name
packageObj.createChildNode("buildComplexEntityPlugInObject", node_buildComplexEntityPlugInObject); 
packageObj.createChildNode("buildSimpleEntityPlugInObject", node_buildSimpleEntityPlugInObject); 
packageObj.createChildNode("buildComplexEntityCoreObject", node_buildComplexEntityCoreObject); 

})(packageObj);
