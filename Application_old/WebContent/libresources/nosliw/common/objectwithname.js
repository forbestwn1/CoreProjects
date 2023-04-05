//get/create package
var packageObj = library.getChildPackage("objectwithname");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "name";
	
	/*
	 * build an object to named object
	 */
	var node_makeObjectWithName = function(obj, name){
		return node_buildInterface(obj, INTERFACENAME, name);
	};

	/*
	 * get object's name
	 */
	var node_getObjectName = function(object){
		return node_getInterface(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
	
//Register Node by Name
packageObj.createChildNode("makeObjectWithName", node_makeObjectWithName); 
packageObj.createChildNode("getObjectName", node_getObjectName); 

})(packageObj);
