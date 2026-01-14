//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_makeObjectWithType;
	var node_complexEntityUtility;
	var node_getApplicationInterface;
	
//*******************************************   Start Node Definition  ************************************** 	

var loc_createDynamicInput = function(dynamicEntity){
	
	var loc_dynamicEntity = dynamicEntity;
	
	var loc_out = {
		
		getDynamicCoreEntity : function(){    return loc_dynamicEntity;    },
		
	};
	return loc_out;
};


var node_createDynamicInputContainer = function(dynamicInputDefs, dynamicInputSourceBundleCore){
	
	var loc_dynamicInputDefs = dynamicInputDefs;
	
	var loc_dynamicInputBundleCore = dynamicInputSourceBundleCore;
	
	var loc_dynamicInputs = {};
	
	var loc_out = {
		
		getDynamicInput : function(inputId){
			return loc_dynamicInputs[inputId];
		},
		
		prepareDyanmicInputRequest : function(inputId, handlers, request){
			var out = loc_dynamicInputs[inputId];
			if(out==undefined){
				var dynamicInputDef = loc_dynamicInputDefs[node_COMMONATRIBUTECONSTANT.DYNAMICEXECUTEINPUTCONTAINER_ELEMENT][inputId];
				var refType = dynamicInputDef[node_COMMONATRIBUTECONSTANT.DYNAMICEXECUTEINPUTITEM_TYPE];
				if(refType==node_COMMONCONSTANT.DYNAMICTASK_REF_TYPE_SINGLE){
					var relativePath = dynamicInputDef[node_COMMONATRIBUTECONSTANT.DYNAMICEXECUTEINPUTITEM_BRICKID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_RELATIVEPATH];
					var absolutePath = dynamicInputDef[node_COMMONATRIBUTECONSTANT.DYNAMICEXECUTEINPUTITEM_BRICKID][node_COMMONATRIBUTECONSTANT.IDBRICKINBUNDLE_IDPATH];
					var dynamicInputEntityCore = node_complexEntityUtility.getBrickCoreByRelativePath(loc_dynamicInputBundleCore, relativePath);
					
//					var dynamicInputEntityCore = node_complexEntityUtility.getDescendantCore(loc_dynamicInputSourceBundleCore, absolutePath);
					
					
					var factory = node_getApplicationInterface(dynamicInputEntityCore, node_CONSTANT.INTERFACE_APPLICATIONENTITY_FACADE_FACTORY);
//					if(factory==undefined){
	                if(true){
						return node_createServiceRequestInfoSimple(undefined, function(request){
							var dynamicInput = loc_createDynamicInput(dynamicInputEntityCore);
							loc_dynamicInputs[inputId] = dynamicInput;
							return dynamicInput;  
						}, handlers, request);
					}
					else{
						//is factory
						var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
						out.addRequest(factory.getCreateEntityRequest({
							success : function(request, entityCore){
								var dynamicInput = loc_createDynamicInput(entityCore);
								loc_dynamicInputs[inputId] = dynamicInput;
								return dynamicInput;  
							}
						}));
						return out;
					}
				}
			}
			else{
					return node_createServiceRequestInfoSimple(undefined, function(request){
						return out;  
					}, handlers, request);
			}
			
		}
	};
	
	return loc_out;
};



var node_EntityIdInDomain = function(parm1, parm2){
	if(typeof parm1 === 'object'){
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm1[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID];
		this.literateStr =  this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID]+node_COMMONCONSTANT.SEPERATOR_LEVEL1+this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
	}
	else if(typeof parm1 === 'string' && parm2==undefined){
		this.literateStr = parm1;
		var index = parm1.indexOf(node_COMMONCONSTANT.SEPERATOR_LEVEL1);
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm1.substring(0, index);
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1.substring(index+node_COMMONCONSTANT.SEPERATOR_LEVEL1.length);
	}
	else{
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE] = parm1;
		this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID] = parm2;
		this.literateStr =  this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYID]+node_COMMONCONSTANT.SEPERATOR_LEVEL1+this[node_COMMONATRIBUTECONSTANT.IDENTITYINDOMAIN_ENTITYTYPE];
	}
};	

var node_createBrickDefinition = function(original){
	var loc_entityDef = original;
	
	var loc_getAttributeByName = function(attrName){
		var attrs = loc_entityDef[node_COMMONATRIBUTECONSTANT.BRICK_ATTRIBUTE];
		for(var i in attrs){
			var attr = attrs[i];
			if(attrName == attr[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]){
				return attr;
			}
		}
	};
	
	var loc_out = {
		
		getAllAttributesName : function(){
			var out = [];
			_.each(loc_entityDef[node_COMMONATRIBUTECONSTANT.BRICK_ATTRIBUTE], function(attr, i){
				out.push(attr[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]);
			});
			return out;
		},
			
		getAttribute : function(attrName){
			var out;
			var attr = loc_getAttributeByName(attrName);
			if(attr==undefined)  return;
			return loc_createAttributeDefinition(attr);
		},

		getAttributeValue : function(attrName){
			var attr = this.getAttribute(attrName);
			return attr==undefined? undefined : attr.getAttributeValueWrapper().getValue();
		},

		getOtherAttributeValue : function(attrName){
			return loc_entityDef[attrName];
		},
		
		getBrickType : function(){
			return loc_entityDef[node_COMMONATRIBUTECONSTANT.BRICK_BRICKTYPE];
		},
		
		original : loc_entityDef
		
	};

	return loc_out;
};

var loc_createAttributeDefinition = function(attrDef){
	var loc_attrDef = attrDef;
	var loc_valueWrapper = loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEINBRICK_VALUEWRAPPER];
	var loc_adapters = {};
	
	_.each(loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEINBRICK_ADAPTER], function(adapter, i){
		loc_adapters[adapter[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME]] = adapter;
	});
	
	var loc_createValeuWrapper = function(rawObj){
		var valueType = rawObj[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUETYPE];
		if(valueType==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_BRICK){
			return loc_createAttributeValueWithEntity(rawObj);
		}
		else if(valueType==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID){
			return loc_createAttributeValueWithResourceReference(rawObj);
		}
		else if(valueType==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_VALUE){
			return loc_createAttributeValueWithValue(rawObj);
		}
		else if(valueType==node_COMMONCONSTANT.ENTITYATTRIBUTE_VALUETYPE_DYNAMIC){
			return loc_createAttributeValueWithDynamic(rawObj);
		}
	};
	
	var loc_out = {
		
		getAttributeInfo : function(){   return loc_attrDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];   },

		getAttributeValueWrapper : function(){
			return loc_createValeuWrapper(loc_valueWrapper);
		},

		getAdapterNames : function(){     
			var names = [];
			_.each(loc_adapters, function(adapter, name){
				names.push(name);
			});
			return names;
		},

		getAdapterValueWrapper : function(name){
			return loc_createValeuWrapper(loc_adapters[name][node_COMMONATRIBUTECONSTANT.ADAPTER_VALUEWRAPPER]); 
		},

	};
	
	return loc_out;
};

var loc_createAttributeValueWithEntity = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_brick = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_BRICK];

	var loc_out = {

		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUETYPE];     },

		getEntityType : function(){  return loc_brick[node_COMMONATRIBUTECONSTANT.BRICK_BRICKTYPE];  },

		getEntityDefinition : function(){   return loc_brick;     },		
		
		getValue : function(){  return loc_brick;    },
		
		isComplex : function(){  return loc_brick[node_COMMONATRIBUTECONSTANT.BRICK_ISCOMPLEX];  },
	};
	
	return loc_out;
};

var loc_createAttributeValueWithValue = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_value = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUE];

	var loc_out = {

		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUETYPE];     },
		
		getValue : function(){   return loc_value;    }
		
	};
	
	return loc_out;
};

var loc_createAttributeValueWithResourceReference = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_resourceId = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_RESOURCEID];

	var loc_out = {
		
		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUETYPE];     },
		
		getResourceId : function(){   return loc_resourceId;    },
		
		getDynamicInput : function(){     return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_DYNAMICINPUT];    }
	};
	
	return loc_out;
};

var loc_createAttributeValueWithDynamic = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_dynamic = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_DYNAMIC];

	var loc_out = {

		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUE_VALUETYPE];     },
		
		getValue : function(){   return loc_dynamic;    },
		
		getDynamic : function(){   return loc_dynamic;    }
		
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();	});
nosliw.registerSetNodeDataEvent("component.getApplicationInterface", function(){node_getApplicationInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("EntityIdInDomain", node_EntityIdInDomain); 
packageObj.createChildNode("createBrickDefinition", node_createBrickDefinition); 
packageObj.createChildNode("createDynamicInputContainer", node_createDynamicInputContainer); 

})(packageObj);
