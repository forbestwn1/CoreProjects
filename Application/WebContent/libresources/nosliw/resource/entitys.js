//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this.type = type;
	this.id = id;
};	
	
var node_Resource = function(resourceInfo, resourceData, info){
	this.resourceInfo = resourceInfo;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
