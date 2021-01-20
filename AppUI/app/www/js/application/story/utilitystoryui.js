//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createUINodeFromStoryNode;
	var node_createUINodeByTag;
	var node_storyUtility;
	var node_ChildUINodeInfo;
	
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_buildUINodeFromStoryNode = function(storyNode, story){
		var uiNode = node_createUINodeFromStoryNode(storyNode[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID], story);
		var childStoryNodesInfo = node_storyUtility.getChildNodesInfo(storyNode, story);
		_.each(childStoryNodesInfo, function(childStroyNodeInfo, i){
			var childUINode = loc_buildUINodeFromStoryNode(childStroyNodeInfo.childNode, story);
			uiNode.addChildInfo(new node_ChildUINodeInfo(childUINode, childStroyNodeInfo.connectionId, childStroyNodeInfo.childId));
		});
		return uiNode;
	};
	
	var loc_getDisplayValue = function(entity, attributeName, displayResource){
		var out;
		if(displayResource!=undefined)	out = displayResource[attributeName];
		if(out==undefined)  out = entity[attributeName];
		return out;
	};
	
	var loc_getStoryElementDisplayValue = function(storyElement, attributeName){
		return loc_getDisplayValue(storyElement, attributeName, storyElement[node_COMMONATRIBUTECONSTANT.STORYELEMENT_DISPLAYRESOURCE]);
	};
	
	var loc_out = {
		
		buildPageTree : function(story){
			var pageNode = node_storyUtility.getStoryNodeByType(story, node_COMMONCONSTANT.STORYNODE_TYPE_PAGE)[0];
			return loc_buildUINodeFromStoryNode(pageNode, story);
		},	
		
		buildUINodeFromStoryNode : function(storyNodeId, story){
			var storyNode = this.getNodeById(story, storyNodeId);
			return loc_buildUINodeFromStoryNode(storyNode, story);
		},
		
//		buildUINodeFromUITag1 : function(uiTag, nodeId, attributes, matchers){
//			return node_createUINodeByTag(uiTag, nodeId, attributes, matchers);
//		},

		buildUINodeFromUITag : function(uiTagInfo, nodeId, attrs, dataInfos){
			var tagId = uiTagInfo[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			var matchers = uiTagInfo[node_COMMONATRIBUTECONSTANT.UITAGINFO_MATCHERS];
			var attributes = uiTagInfo[node_COMMONATRIBUTECONSTANT.UITAGINFO_ATTRIBUTES];
			_.each(attrs, function(value, name){
				attributes[name] = value;
			});
			return node_createUINodeByTag(tagId, nodeId, attributes, matchers, dataInfos);
		},

		getStoryElementDisplayName : function(storyElement){
			return loc_getStoryElementDisplayValue(storyElement, "displayName");
		}
		
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.createUINodeFromStoryNode", function(){node_createUINodeFromStoryNode = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.createUINodeByTag", function(){node_createUINodeByTag = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.ChildUINodeInfo", function(){node_ChildUINodeInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("storyUIUtility", node_utility); 

})(packageObj);
