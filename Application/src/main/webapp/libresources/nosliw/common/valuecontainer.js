//get/create package
var packageObj = library.getChildPackage("valuecontainer");    

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

var node_makeObjectValueContainerProvider = function(rawEntity){
	
	var loc_rawEntity = rawEntity;

	var loc_interfaceEntity = {
		getValueContainer : function(){   return loc_rawEntity.getValueContainer==undefined?undefined:loc_rawEntity.getValueContainer();    }
	};
	
	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_VALUECONTAINERPROVIDER, loc_interfaceEntity);
	return loc_out;
};


var node_createValueContainerList = function(){
	
	var loc_children = [];
	
	var loc_out = {
		
		addChild : function(){},

		getGetValueRequest : function(categary, name, handlers, request){   
			
		}
		
	};

	return loc_out;	
};








var node_makeObjectValueContainerInterface = function(rawEntity, categary){

	var loc_categary = categary;
	var loc_rawEntity = rawEntity;

	var loc_interfaceEntity = {
		getCategary : function(){    return loc_categary;     },
		getGetValueRequest : function(name, handlers, request){   return loc_rawEntity.getGetValueRequest==undefined?undefined:loc_rawEntity.getGetValueRequest(name, handlers, request);    }
	};
	
	var loc_out = node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_VALUECONTAINER, loc_interfaceEntity);
	return loc_out;
};

var node_getValueContainerInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_VALUECONTAINER);
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
packageObj.createChildNode("makeObjectValueContainerInterface", node_makeObjectValueContainerInterface); 
packageObj.createChildNode("getValueContainerInterface", node_getValueContainerInterface); 


})(packageObj);
