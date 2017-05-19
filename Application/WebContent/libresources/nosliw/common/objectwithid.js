//get/create package
var packageObj = library.getChildPackage("objectWithId");    

(function(packageObj){
	//get used node
	var loc_buildInterface;
	var loc_getInterface;
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

	var module = {
		start : function(packageObj){
			loc_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			loc_getInterface = packageObj.getNodeData("common.interface.getInterfaceNode");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
