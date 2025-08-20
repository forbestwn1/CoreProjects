//get/create package
var packageObj = library.getChildPackage("interfacedef");    

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
	var node_getObjectType;

//*******************************************   Start Node Definition  **************************************

var node_makeObjectWithEmbededEntityInterface = function(rawEntity){
	
	var loc_envInterface = {};
	var loc_rawEntity = rawEntity;

	var loc_interfaceEntity = {

		setEnvironmentInterface : function(path, envInterface){
			var newInterface = {};
			newInterface[path] = envInterface;
			loc_envInterface = _.extend({}, loc_envInterface, newInterface);
			if(loc_rawEntity.setEnvironmentInterface!=undefined)   loc_rawEntity.setEnvironmentInterface(loc_envInterface);  
		},
		
		getEnvironmentInterface : function(){
			return loc_envInterface;
		}
		
	};
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_EMBEDEDENTITY, loc_interfaceEntity);
};
	
var node_getEmbededEntityInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_EMBEDEDENTITY);
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


var INTERFACENAME_NAME = "name";

/*
 * build an object to named object
 */
var node_makeObjectWithName = function(obj, name){
	return node_buildInterface(obj, INTERFACENAME_NAME, name);
};

/*
 * get object's name
 */
var node_getObjectName = function(object){
	return node_getInterface(object, INTERFACENAME_NAME);
};

var INTERFACENAME_TYPE = "TYPE";
	
/*
 * build an object to typed object
 */
var node_makeObjectWithType = function(obj, type){
	return node_buildInterface(obj, INTERFACENAME_TYPE, type);
};

/*
 * get object's type info
 * if no type info, the use VALUE as type  
 */
var node_getObjectType = function(object){
	var type = node_getInterface(object, INTERFACENAME_TYPE);
	if(type!=undefined)  return type;
	else return node_CONSTANT.TYPEDOBJECT_TYPE_VALUE;
};

/*
 * build an object have id info
 */
var node_makeObjectWithId = function(obj, id){
	var loc_id = id;
	if(loc_id==undefined)  loc_id = nosliw.generateId();
	
	var embededEntityInterface =  node_getEmbededEntityInterface(obj);
	if(embededEntityInterface!=null){
		embededEntityInterface.setEnvironmentInterface(node_CONSTANT.INTERFACE_WITHID, {
			getId : function(){		return loc_id; },
		});
	}
	
	return node_buildInterface(obj, node_CONSTANT.INTERFACE_WITHID, loc_id);
};

/*
 * get object's id info
 */
var node_getObjectId = function(object){
	return node_getInterface(object, node_CONSTANT.INTERFACE_WITHID);
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
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});


//Register Node by Name
packageObj.createChildNode("makeObjectWithEmbededEntityInterface", node_makeObjectWithEmbededEntityInterface); 
packageObj.createChildNode("getEmbededEntityInterface", node_getEmbededEntityInterface); 

packageObj.createChildNode("makeObjectBasicEntityObjectInterface", node_makeObjectBasicEntityObjectInterface); 
packageObj.createChildNode("getBasicEntityObjectInterface", node_getBasicEntityObjectInterface); 

packageObj.createChildNode("makeObjectWithName", node_makeObjectWithName); 
packageObj.createChildNode("getObjectName", node_getObjectName); 

packageObj.createChildNode("makeObjectWithType", node_makeObjectWithType); 
packageObj.createChildNode("getObjectType", node_getObjectType); 

packageObj.createChildNode("makeObjectWithId", node_makeObjectWithId); 
packageObj.createChildNode("getObjectId", node_getObjectId); 

})(packageObj);
