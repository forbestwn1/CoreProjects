//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_CONSTANT;
var node_makeObjectWithType;
//*******************************************   Start Node Definition  ************************************** 	

var node_ServiceData = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
};

var node_createErrorData = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
	
	var loc_out = {
			
	}
	
	loc_out = node_makeObjectWithType(loc_out, node_CONSTANT.TYPEDOBJECT_TYPE_ERROR);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});


//Register Node by Name
packageObj.createChildNode("ServiceData", node_ServiceData); 

})(packageObj);

