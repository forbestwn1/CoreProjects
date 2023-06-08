//get/create package
var packageObj = library.getChildPackage("embeded");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
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
		
	};
	
	return node_buildInterface(rawEntity, node_CONSTANT.INTERFACE_EMBEDEDENTITY, loc_interfaceEntity);
};
	
var node_getEmbededEntityInterface = function(baseObject){
	return node_getInterface(baseObject, node_CONSTANT.INTERFACE_EMBEDEDENTITY);
};

	
//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithEmbededEntityInterface", node_makeObjectWithEmbededEntityInterface); 
packageObj.createChildNode("getEmbededEntityInterface", node_getEmbededEntityInterface); 

})(packageObj);
