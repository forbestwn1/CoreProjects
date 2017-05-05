//get/create package
var packageObj = library.getChildPackage("objectWithName");    

(function(packageObj){
	//get used node
	var buildInterfaceNode = packageObj.require("common.interface.buildInterface");
	var getInterfaceNode = packageObj.require("common.interface.getInterfaceNode");
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
packageObj.createNode("makeObjectWithName", makeObjectWithType); 
packageObj.createNode("getObjectName", getObjectType); 

})(packageObj);
