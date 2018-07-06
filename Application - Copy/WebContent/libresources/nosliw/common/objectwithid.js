//get/create package
var packageObj = library.getChildPackage("objectwithid");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "id";
	
	/*
	 * build an object have id info
	 */
	var node_makeObjectWithId = function(obj, id){
		return node_buildInterface(obj, INTERFACENAME, id);
	};

	/*
	 * get object's id info
	 */
	var node_getObjectId = function(object){
		return node_getInterface(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithId", node_makeObjectWithId); 
packageObj.createChildNode("getObjectId", node_getObjectId); 

})(packageObj);
