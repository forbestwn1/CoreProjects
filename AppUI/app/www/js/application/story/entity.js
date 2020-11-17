//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_DesignStep = function(changes, question, info){
	this.changes = changes;
	this.question = question;
	this.info = info;
	return this;
};

var node_ElementId = function(categary, id){
	this[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY] = categary;
	this[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID] = id;
};
	
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


var node_createUINodeFromStoryNode = function(storyNodeId, story){
	
	var loc_out = {
		prv_story : story,
		prv_storyNodeId : storyNodeId,
		prv_childrenInfo : [],
		prv_storyNode : undefined,

		getId : function(){   return this.prv_storyNodeId;   },
		getNodeType : function(){	return this.getStoryNode()[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];	},
			
		getStoryNodeId : function(){   return this.prv_storyNodeId;   },
		getStoryNode : function(){
			if(this.prv_storyNode==undefined){
				this.prv_storyNode = node_storyUtility.getNodeById(this.prv_story, this.prv_storyNodeId);  
			}
			return this.prv_storyNode;
		},	
		
		addChildInfo : function(childInfo){		this.prv_childrenInfo.push(childInfo);	},
		getChildrenInfo : function(){   return this.prv_childrenInfo;    },
		getChildren : function(){ 
			var out = [];
			_.each(this.prv_childrenInfo, function(childInfo, i){
				out.push(childInfo.childNode);
			});
			return out;
		},
		
		getBody : function(){	return loc_out;	}
	
	};
	return loc_out;
};
	
var node_createUINodeByTag = function(tagId, nodeId, attributes){
	var loc_nodeId = nodeId;
	var loc_tagId = tagId;
	var loc_children = [];
	var loc_attributes = attributes==undefined?{}:attributes;
	
	var loc_out = {
		
		getId : function(){   return loc_nodeId;   },
		getNodeType : function(){  return node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA;	},
		
		getTagId : function(){  return loc_tagId; },
			
		addChild : function(uiNode, index){
			loc_children[index] = uiNode;
		},
		
		getChildren : function(){   return loc_children;    },
		
		getBody : function(){	return loc_out;	},
		
		getAttributes : function(){    return loc_attributes;     },
	
	};
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("DesignStep", node_DesignStep); 
packageObj.createChildNode("ElementId", node_ElementId); 
packageObj.createChildNode("createUINodeFromStoryNode", node_createUINodeFromStoryNode); 
packageObj.createChildNode("createUINodeByTag", node_createUINodeByTag); 
packageObj.createChildNode("ChildStoryNodeInfo", node_ChildStoryNodeInfo); 
packageObj.createChildNode("ChildUINodeInfo", node_ChildUINodeInfo); 

})(packageObj);
