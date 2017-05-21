//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_buildInterface;
	var node_getInterface;
//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "TYPE";
	
/*
 * build an object to typed object
 */
var makeObjectWithType = function(obj, type){
	out = buildInterfaceNode.getData()(obj, INTERFACENAME, type);
	return out;
};

/*
 * get object's type info
 * if no type info, the use VALUE as type  
 */
var getObjectType = function(object){
	var type = getInterfaceNode.getData()(object, INTERFACENAME);
	if(type!=undefined)  return type;
	else return NOSLIWCONSTANT.TYPEDOBJECT_TYPE_VALUE;
};
	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("makeObjectWithType", makeObjectWithType); 
packageObj.createNode("getObjectType", getObjectType); 

	var module = {
		start : function(packageObj){
			node_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
			node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			node_getInterface = packageObj.getNodeData("common.interface.getInterface");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);

