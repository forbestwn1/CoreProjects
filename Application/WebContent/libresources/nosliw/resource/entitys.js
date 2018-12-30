//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = type; 
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = id; 
};	
	
var node_Resource = function(resourceInfo, resourceData, info){
	this.resourceInfo = resourceInfo;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
