//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_makeObjectWithType;
	var node_complexEntityUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

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

var node_createEntityDefinition = function(original){
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
			return attr==undefined? undefined : attr.getAttributeValue();
		},
		
	};

	return loc_out;
};

var loc_createAttributeDefinition = function(attrDef){
	var loc_attrDef = attrDef;
	var loc_valueWrapper = loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEINBRICK_VALUEWRAPPER];

	var loc_getAdapters = function(){  }    // return loc_getEmbeded()[node_COMMONATRIBUTECONSTANT.EMBEDED_ADAPTER];   };
	
	var loc_out = {
		
		getAttributeInfo : function(){   return loc_attrDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];   },

		getAttributeValue : function(){
			var valueType = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_VALUETYPE];
			if(valueType==node_COMMONCONSTANT.EMBEDEDVALUE_TYPE_BRICK){
				return loc_createAttributeValueWithEntity(loc_valueWrapper);
			}
			else if(valueType==node_COMMONCONSTANT.EMBEDEDVALUE_TYPE_EXTERNALREFERENCE){
				return loc_createAttributeValueWithResourceReference(loc_valueWrapper);
			}
			else if(valueType==node_COMMONCONSTANT.EMBEDEDVALUE_TYPE_VALUE){
				return loc_createAttributeValueWithValue(loc_valueWrapper);
			}
		},

		getAdaptersInfo : function(){    return loc_getAdapters();     },
		
		getAdapterInfo : function(name){    return loc_getAdapters()[name];     },

	};
	
	return loc_out;
};

var loc_createAttributeValueWithEntity = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_brick = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_BRICK];

	var loc_out = {

		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_VALUETYPE];     },

		getEntityType : function(){  return loc_brick[node_COMMONATRIBUTECONSTANT.BRICK_BRICKTYPE];  },
		
		isComplex : function(){  return loc_brick[node_COMMONATRIBUTECONSTANT.BRICK_ISCOMPLEX];  },
	};
	
	return loc_out;
};

var loc_createAttributeValueWithValue = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_value = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_VALUE];

	var loc_out = {

		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_VALUETYPE];     },
		
		getValue : function(){   return loc_value;    }
		
	};
	
	return loc_out;
};

var loc_createAttributeValueWithResourceReference = function(valueWrapper){
	var loc_valueWrapper = valueWrapper;
	var loc_resourceId = loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_RESOURCEID];

	var loc_out = {
		getValueType : function(){   return loc_valueWrapper[node_COMMONATRIBUTECONSTANT.WRAPPERVALUEINATTRIBUTE_VALUETYPE];     },
		
		getResourceId : function(){   return loc_resourceId;    }
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.complexEntityUtility", function(){node_complexEntityUtility = this.getData();	});

//Register Node by Name
packageObj.createChildNode("EntityIdInDomain", node_EntityIdInDomain); 
packageObj.createChildNode("createEntityDefinition", node_createEntityDefinition); 

})(packageObj);
