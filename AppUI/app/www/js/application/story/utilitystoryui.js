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
			var childUINode = loc_buildUINode(childStroyNode, story);
			uiNode.addChild(childUINode, childId);
		});
		return uiNode;
	};
	
	var loc_out = {
		
		buildPageTree : function(story){
			var pageNode = this.getStoryNodeByType(story, node_COMMONCONSTANT.STORYNODE_TYPE_PAGE)[0];
			return loc_buildUINode(pageNode, story);
		},	
		
		buildUINodeFromStoryNode : function(storyNodeId, story){
			var storyNode = this.getNodeById(story, storyNodeId);
			return loc_buildUINodeFromStoryNode(storyNode, story);
		},
		
		buildUINodeFromUITag : function(uiTag){
			return node_createUINodeByTag(uiTag);
		},
			
		getStoryNodeByType : function(story, nodeType) {
			var out = [];
			_.each(story[node_COMMONATRIBUTECONSTANT.STORY_NODE], function(node, id){
				if(nodeType==node[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]){
					out.push(node);
				}
			});
			return out;
		},
		
		getAllChildNodes : function(parent, story) {
			var that  = this;
			var out = {};
			var childConnectionEnds = this.getNodeConnectionEnd(parent, node_COMMONCONSTANT.CONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
			_.each(childConnectionEnds, function(connectionEnd, i){
				var childId = that.getConnectionById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_CONNECTIONID])[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENTITY][node_COMMONATRIBUTECONSTANT.CONNECTIONENTITYCONTAIN_CHILD];
				out[childId] = that.getNodeById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID]);
			});
			return out;
		},

		getChildNodeByChildId : function(parent, childId, story) {
			var that  = this;
			var childConnectionEnds = this.getNodeConnectionEnd(parent, node_COMMONCONSTANT.CONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
			_.each(childConnectionEnds, function(connectionEnd, i){
				var id = that.getConnectionById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_CONNECTIONID])[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENTITY][node_COMMONATRIBUTECONSTANT.CONNECTIONENTITYCONTAIN_CHILD];
				if(id==childId)   return that.getNodeById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID]);
			});
			return;
		},
		
		getNodeConnectionEnd : function(node1, connectionType, profile1, node2Type, profile2, story) {
			var that  = this;
			var out = [];
			_.each(node1[node_COMMONATRIBUTECONSTANT.STORYNODE_CONNECTIONS], function(connectionId, i){
				var connection = that.getConnectionById(story, connectionId);
				if(connectionType==undefined || connection[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]==connectionType){
					var node1End = that.getConnectionEnd(connection, node1[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
					if(profile1==undefined || node1End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_PROFILE]==profile1){
						var node2End = that.getOtherConnectionEnd(connection, node1[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
						if(profile2==undefined || node2End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_PROFILE]==profile2){
							if(node2Type==undefined || that.getNodeById(node2End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID])[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]){
								out.push(node2End);
							}
						}
					}
				}
			});
			return out;
		},
		
		getOtherConnectionEnd : function(connection, nodeId) {
			var end1 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END1];
			var end2 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END2];
			if(end1[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID]==nodeId)  return end2;
			else return end1;
		},
		
		getConnectionEnd : function(connection, nodeId) {
			var end1 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END1];
			var end2 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END2];
			if(end1[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID]==nodeId)  return end1;
			else return end2;
		},
		
		getNodeById : function(story, nodeId){	return story[node_COMMONATRIBUTECONSTANT.STORY_NODE][nodeId];	},
		
		getConnectionById : function(story, connectionId){	return story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION][connectionId];	},
			
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
