//get/create package
var packageObj = library.getChildPackage("objectWithId");    

(function(packageObj){
	//get used node
	var buildInterfaceNode = packageObj.requireNode("common.interface.buildInterface");
	var getInterfaceNode = packageObj.requireNode("common.interface.getInterfaceNode");
//*******************************************   Start Node Definition  ************************************** 	

	var INTERFACENAME = "id";
	
	/*
	 * build an object have id info
	 */
	var makeObjectWithId = function(obj, id){
		return buildInterfaceNode.getData()(obj, INTERFACENAME, id);
	};

	/*
	 * get object's id info
	 */
	var getObjectId = function(object){
		return getInterfaceNode.getData()(object, INTERFACENAME);
	};
		

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("makeObjectWithId", makeObjectWithId); 
packageObj.createNode("getObjectId", getObjectId); 

})(packageObj);
