//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_ResourceId = function(type, id){
	this[node_COMMONATRIBUTECONSTANT.RESOURCEID_TYPE] = type;
	if(!id.startsWith(node_COMMONCONSTANT.SEPERATOR_RESOURCEID_START)){
		id = node_COMMONCONSTANT.SEPERATOR_RESOURCEID_START+node_COMMONCONSTANT.RESOURCEID_TYPE_SIMPLE+node_COMMONCONSTANT.SEPERATOR_RESOURCEID_STRUCTURE+id;
	}
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
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ResourceId", node_ResourceId); 
packageObj.createChildNode("Resource", node_Resource); 

})(packageObj);
