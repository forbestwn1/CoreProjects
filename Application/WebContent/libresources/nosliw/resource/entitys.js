//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this.type = type;
	this.id = id;
};	
	
var node_Resource = function(resourceId, resourceData, info){
	this.resourceId = resourceId;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("ResourceId", node_ResourceId); 
packageObj.createNode("Resource", node_Resource); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
