//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_createPageTreeNode = function(storyNode){
	var loc_out = {
		storyNodeId : storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID],
		children : []
	};
	return loc_out;
};
	
//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("createPageTreeNode", node_createPageTreeNode); 

})(packageObj);
