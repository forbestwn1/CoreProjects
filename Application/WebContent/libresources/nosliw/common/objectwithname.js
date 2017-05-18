//get/create package
var packageObj = library.getChildPackage("objectWithName");    

(function(packageObj){
	//get used node
	var buildInterfaceNode = packageObj.requireNode("common.interface.buildInterface");
	var getInterfaceNode = packageObj.requireNode("common.interface.getInterfaceNode");
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "name";
	
	/*
	 * build an object to named object
	 */
	var makeObjectWithName = function(obj, name){
		return buildInterfaceNode.getData()(obj, INTERFACENAME, name);
	};

	/*
	 * get object's name
	 */
	var getObjectName = function(object){
		return getInterfaceNode.getData()(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("makeObjectWithName", makeObjectWithName); 
packageObj.createNode("getObjectName", getObjectName); 

})(packageObj);
