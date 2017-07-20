//get/create package
var packageObj = library.getChildPackage("objectWithId");    

(function(packageObj){
	//get used node
	var node_buildInterface;
	var node_getInterface;
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
	packageObj.createChildNode("makeObjectWithId", makeObjectWithId); 
	packageObj.createChildNode("getObjectId", getObjectId); 

	var module = {
		start : function(packageObj){
			node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			node_getInterface = packageObj.getNodeData("common.interface.getInterface");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
