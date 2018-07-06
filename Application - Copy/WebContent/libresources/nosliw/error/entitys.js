//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_ServiceData = function(code, message, data){
	this.code = code;
	this.message = message;
	this.data = data;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data


//Register Node by Name
packageObj.createChildNode("ServiceData", node_ServiceData); 

})(packageObj);

