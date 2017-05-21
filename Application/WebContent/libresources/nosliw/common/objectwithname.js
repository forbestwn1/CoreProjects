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
	//Register Node by Name
	packageObj.createNode("makeObjectWithName", node_makeObjectWithName); 
	packageObj.createNode("getObjectName", node_getObjectName); 

	var module = {
		start : function(packageObj){
			node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			node_getInterface = packageObj.getNodeData("common.interface.getInterface");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
