//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_makeObjectWithType;
//*******************************************   Start Node Definition  ************************************** 	

var node_ServiceData = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});


//Register Node by Name
packageObj.createChildNode("ServiceData", node_ServiceData); 

})(packageObj);

