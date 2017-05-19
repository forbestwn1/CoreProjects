//get/create package
var packageObj = library.getChildPackage("objectWithName");    

(function(packageObj){
	//get used node
	var loc_buildInterface;
	var loc_getInterface;
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

	var module = {
		start : function(packageObj){
			loc_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			loc_getInterfaceNode = packageObj.getNodeData("common.interface.getInterfaceNode");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
