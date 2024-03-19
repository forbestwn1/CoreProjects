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
		var attrs = loc_entityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ATTRIBUTE];
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
			_.each(loc_entityDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEENTITY_ATTRIBUTE], function(attr, i){
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
			return attr==undefined? undefined : attr.getValue();
		},
		
	};

	return loc_out;
};

var loc_createAttributeDefinition = function(attrDef){
	var loc_attrDef = attrDef;

	var loc_getEmbeded = function(){	return loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_VALUE];	};
	var loc_getAdapters = function(){   return loc_getEmbeded()[node_COMMONATRIBUTECONSTANT.EMBEDED_ADAPTER];   };
	
	var loc_out = {
		
		getAttributeInfo : function(){   return loc_attrDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];   },
		
		getEntityType : function(){  return loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_VALUETYPEINFO][node_COMMONATRIBUTECONSTANT.INFOENTITYTYPE_VALUETYPE];  },
		
		isComplex : function(){  return loc_attrDef[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_VALUETYPEINFO][node_COMMONATRIBUTECONSTANT.INFOENTITYTYPE_ISCOMPLEX];  },

		isExternalReference : function(){   return loc_getEmbeded()[node_COMMONATRIBUTECONSTANT.EMBEDED_VALUETYPE]==node_COMMONCONSTANT.EMBEDEDVALUE_TYPE_EXTERNALREFERENCE;     },
		
		getValue : function(){		return loc_getEmbeded()[node_COMMONATRIBUTECONSTANT.EMBEDED_VALUE];	},

		getAdaptersInfo : function(){    return loc_getAdapters();     },
		
		getAdapterInfo : function(name){    return loc_getAdapters()[name];     },
		
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
