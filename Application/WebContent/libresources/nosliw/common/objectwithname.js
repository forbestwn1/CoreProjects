//get/create package
var packageObj = library.getChildPackage("objectWithName");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "name";
	
	/*
	 * build an object to named object
	 */
	var makeObjectWithName = function(obj, name){
		return node_buildInterface(obj, INTERFACENAME, name);
	};

	/*
	 * get object's name
	 */
	var getObjectName = function(object){
		return node_getInterface(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	
	//Register Node by Name
	packageObj.createNode("makeObjectWithName", makeObjectWithName); 
	packageObj.createNode("getObjectName", getObjectName); 

	var module = {
		start : function(packageObj){
			node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			node_getInterface = packageObj.getNodeData("common.interface.getInterfaceNode");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
