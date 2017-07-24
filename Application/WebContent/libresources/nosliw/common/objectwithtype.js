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

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.buildInterface", function(){node_buildInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.interface.getInterface", function(){node_getInterface = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("makeObjectWithType", node_makeObjectWithType); 
packageObj.createChildNode("getObjectType", node_getObjectType); 

})(packageObj);

