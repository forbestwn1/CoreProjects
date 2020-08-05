//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createUINodeFromStoryNode;
	var node_createUINodeByTag;
	
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	
	
	var loc_buildUINodeFromStoryNode = function(storyNode, story){
		var uiNode = node_createUINodeFromStoryNode(storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID], story);
		var childStoryNodes = loc_out.getAllChildNodes(storyNode, story);
		_.each(childStoryNodes, function(childStroyNode, childId){
			var childUINode = loc_buildUINodeFromStoryNode(childStroyNode, story);
			uiNode.addChild(childUINode, childId);
		});
		return uiNode;
	};
	
	var loc_out = {
		
		buildPageTree : function(story){
			var pageNode = this.getStoryNodeByType(story, node_COMMONCONSTANT.STORYNODE_TYPE_PAGE)[0];
			return loc_buildUINodeFromStoryNode(pageNode, story);
		},	
		
		buildUINodeFromStoryNode : function(storyNodeId, story){
			var storyNode = this.getNodeById(story, storyNodeId);
			return loc_buildUINodeFromStoryNode(storyNode, story);
		},
		
		buildUINodeFromUITag : function(uiTag){
			return node_createUINodeByTag(uiTag);
		},
			
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.createUINodeFromStoryNode", function(){node_createUINodeFromStoryNode = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.createUINodeByTag", function(){node_createUINodeByTag = this.getData();});

//Register Node by Name
packageObj.createChildNode("storyUIUtility", node_utility); 

})(packageObj);
