//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_buildInterface;
	var node_getInterface;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var INTERFACENAME = "TYPE";
	
/*
 * build an object to typed object
 */
var node_makeObjectWithType = function(obj, type){
	out = node_buildInterface(obj, INTERFACENAME, type);
	return out;
};

/*
 * get object's type info
 * if no type info, the use VALUE as type  
 */
var node_getObjectType = function(object){
	var type = node_getInterface(object, INTERFACENAME);
	if(type!=undefined)  return type;
	else return node_CONSTANT.TYPEDOBJECT_TYPE_VALUE;
};
	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createChildNode("makeObjectWithType", node_makeObjectWithType); 
packageObj.createChildNode("getObjectType", node_getObjectType); 

	var module = {
		start : function(packageObj){
			node_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
			node_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			node_getInterface = packageObj.getNodeData("common.interface.getInterface");
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);

