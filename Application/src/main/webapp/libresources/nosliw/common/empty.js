//get/create package
var packageObj = library.getChildPackage("empty");    

(function(packageObj){
	//get used node
	var node_basicUtility;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_CONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createEmptyValue = function(){
	var loc_out = {
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_EMPTY);
	
	return loc_out;
};	
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createEmptyValue", node_createEmptyValue);  
	
})(packageObj);
