//get/create package
var packageObj = library.getChildPackage("valueinvar.entity");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
var node_CONSTANT;	
//*******************************************   Start Node Definition  ************************************** 	
/*
 * data is a combination of value + dataType
 */
var node_createValueInVar = function(value, dataTypeInfo){
	var loc_out = {
		value : value,
		dataTypeInfo : dataTypeInfo,
	};
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_DATA);
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("createValueInVar", node_createValueInVar); 

})(packageObj);
