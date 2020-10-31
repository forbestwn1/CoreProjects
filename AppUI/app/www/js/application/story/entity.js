//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_ChildStoryNodeInfo = function(childNode, connectionId, childId){
	this.childNode = childNode;
	this.connectionId = connectionId;
	this.childId = childId;
	return this;
};

var node_ChildUINodeInfo = function(childNode, connectionId, childId){
	this.childNode = childNode;
	this.connectionId = connectionId;
	this.childId = childId;
	return this;
};


var node_createUINodeFromStoryNode = function(nodeId, story){
	
	var loc_out = {
		prv_story : story,
		prv_nodeId : nodeId,
		prv_childrenInfo : [],
		prv_storyNode : undefined,
			
		getStoryNodeId : function(){   return this.prv_nodeId;   },
			
		getStoryNode : function(){
			if(this.prv_storyNode==undefined){
				this.prv_storyNode = node_storyUtility.getNodeById(this.prv_story, this.prv_nodeId);  
			}
			return this.prv_storyNode;
		},	
		
		getNodeType : function(){
			return this.getStoryNode()[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];
		},
			
//		getTagId : function(){
//			var storyNode = loc_getStoryNode();
//			var nodeEntity = storyNode[node_COMMONATRIBUTECONSTANT.STORYELEMENT_ENTITY];
//			return nodeEntity.tag;
//		},
			
		addChildInfo : function(childInfo){
			this.prv_childrenInfo.push(childInfo);
		},
		
		getChildrenInfo : function(){   return this.prv_childrenInfo;    },
		
		getBody : function(){
			return loc_out;
		}
	
	};
	return loc_out;
};
	
var node_createUINodeByTag = function(tagId){
	
	var loc_tagId = tagId;
	var loc_children = [];
	
	var loc_out = {
		
		getNodeType : function(){  return node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA;	},
			
		getTagId : function(){  return loc_tagId; },
			
		addChild : function(uiNode, index){
			loc_children[index] = uiNode;
		},
		
		getChildren : function(){   return loc_children;    },
		
		getBody : function(){
			return loc_out;
		}
	
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.utility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createUINodeFromStoryNode", node_createUINodeFromStoryNode); 
packageObj.createChildNode("createUINodeByTag", node_createUINodeByTag); 
packageObj.createChildNode("ChildStoryNodeInfo", node_ChildStoryNodeInfo); 
packageObj.createChildNode("ChildUINodeInfo", node_ChildUINodeInfo); 

})(packageObj);
