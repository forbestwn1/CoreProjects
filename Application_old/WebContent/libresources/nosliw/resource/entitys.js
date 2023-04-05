//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_RESOURCETYPE] = type;
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID] = node_resourceUtility.buildReourceCoreIdLiterate(id);
	this.name = id;
};	
	
var node_Resource = function(resourceInfo, resourceData, info){
	this.resourceInfo = resourceInfo;
	this.resourceData = resourceData;
	this.info = info;
}


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
