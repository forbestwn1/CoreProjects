//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var basicUtilityNode = packageObj.require("common.utility.basicUtility");
	var buildInterfaceNode = packageObj.require("common.interface.buildInterface");
	var getInterfaceNode = packageObj.require("common.interface.getInterfaceNode");
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

})(packageObj);

