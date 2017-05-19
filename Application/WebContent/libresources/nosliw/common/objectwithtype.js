//get/create package
var packageObj = library.getChildPackage("objectwithtype");    

(function(packageObj){
	//get used node
	var loc_basicUtility;
	var loc_buildInterface;
	var loc_getInterface;
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
			loc_basicUtility = packageObj.getNodeData("common.utility.basicUtility");
			loc_buildInterface = packageObj.getNodeData("common.interface.buildInterface");
			loc_getInterface = packageObj.getNodeData("common.interface.getInterfaceNode");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);

