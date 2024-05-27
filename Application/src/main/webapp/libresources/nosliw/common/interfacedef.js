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
packageObj.createChildNode("makeObjectBasicEntityObjectInterface", node_makeObjectBasicEntityObjectInterface); 
packageObj.createChildNode("getBasicEntityObjectInterface", node_getBasicEntityObjectInterface); 


})(packageObj);
