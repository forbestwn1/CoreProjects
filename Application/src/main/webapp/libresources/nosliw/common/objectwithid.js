//get/create package
var packageObj = library.getChildPackage("objectwithid");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_buildInterface;
	var node_getInterface;
	var node_getEmbededEntityInterface;
//*******************************************   Start Node Definition  ************************************** 	

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
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getEmbededEntityInterface", function(){node_getEmbededEntityInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithId", node_makeObjectWithId); 
packageObj.createChildNode("getObjectId", node_getObjectId); 

})(packageObj);
