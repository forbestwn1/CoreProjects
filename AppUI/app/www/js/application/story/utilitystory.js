//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_out = {
		
		getNextIdForStory : function(story){
			var index = this.getIdIndex(story);
			index = index + 1;
			this.setIdIndex(index);
		},
		
		getIdIndex : function(story){
			var info = story[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			return info[node_COMMONCONSTANT.STORY_INFO_IDINDEX];
		},
		
		setIdIndex : function(story, index){
			var info = story[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			info[node_COMMONCONSTANT.STORY_INFO_IDINDEX] = index;
		},
			
		getElementIdByReference : function(story, reference){
			if(reference[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY]!=null)  return reference;
			else return story[node_COMMONATRIBUTECONSTANT.STORY_ALIAS][reference[node_COMMONATRIBUTECONSTANT.ALIASELEMENT_NAME]];
		},	
			
		getQuestionTargetElement : function(story, question){
			return this.getStoryElementByRef(story, question[node_COMMONATRIBUTECONSTANT.QUESTION_TARGETREF]);
		},
			
		getAllNodes : function(story){  return story[node_COMMONATRIBUTECONSTANT.STORY_NODE];   },
		
		addStoryElement : function(story, element, aliasObj){
			var elementCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
			var elementId = element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			if(elementId==undefined){
				elementId = this.getNextIdForStory(story);
				element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID] = elementId;
			}
			if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_NODE){
				story[node_COMMONATRIBUTECONSTANT.STORY_NODE][elementId] = element;
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_CONNECTION){
				story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION][elementId] = element;
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
				story[node_COMMONATRIBUTECONSTANT.STORY_ELEMENTGROUP][elementId] = element;
			}
			//process alias
			if(aliasObj!=undefined){
				story[node_COMMONATRIBUTECONSTANT.STORY_ALIAS][aliasObj[node_COMMONATRIBUTECONSTANT.ALIASELEMENT_NAME]] = new node_ElementId(elementCategary, elementId); 
			}
			return element;
		},
		
		getStoryElementByRef : function(story, ref){
			var elementId = this.getElementIdByReference(story, ref);
			return this.getStoryElement(story, elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]);
		},

		getStoryElement : function(story, elementCategary, elementId){
			var out;
			if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_NODE){
				out = this.getNodeById(story, elementId);
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_CONNECTION){
				out = this.getConnectionById(story, elementId);
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
				out = this.getGroupById(story, elementId);
			}
			return out;
		},
		
		deleteStoryElementByRef : function(story, ref){
			var elementId = node_storyUtility.getElementIdByReference(story, ref);
			return this.deleteStoryElement(elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_CATEGARY], elementId[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]);
		},
		
		deleteStoryElement : function(story, elementCategary, elementId){
			var out;
			if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_NODE){
				out = this.deleteNodeById(story, elementId);
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_CONNECTION){
				out = this.deleteConnectionById(story, elementId);
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
				out = this.deleteGroupById(story, elementId);
			}
			return out;
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
		
//		getAllChildNodes : function(parent, story) {
//			var that  = this;
//			var out = {};
//			var childConnectionEnds = this.getNodeConnectionEnd(parent, node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
//			_.each(childConnectionEnds, function(connectionEnd, i){
//				var childId = that.getConnectionById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_CONNECTIONID])[node_COMMONATRIBUTECONSTANT.CONNECTIONCONTAIN_CHILDID];
//				out[childId] = that.getNodeById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEID]);
//			});
//			return out;
//		},

		getAllChildNodesInfo : function(parent, story) {
			var that  = this;
			var out = [];
			var childConnectionEnds = this.getNodeConnectionEnd(parent, node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
			_.each(childConnectionEnds, function(connectionEnd, i){
				var connectionId = connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_CONNECTIONID];
				var childId = that.getConnectionById(story, connectionId)[node_COMMONATRIBUTECONSTANT.CONNECTIONCONTAIN_CHILDID];
				out.push({
					node: that.getStoryElementByRef(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF]),
					connectionId : connectionId,
					childId : childId
				});
			});
			return out;
		},

		getChildNodeByChildId : function(parent, childId, story) {
			var that  = this;
			var childConnectionEnds = this.getNodeConnectionEnd(parent, node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
			_.each(childConnectionEnds, function(connectionEnd, i){
				var id = that.getConnectionById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_CONNECTIONID])[node_COMMONATRIBUTECONSTANT.CONNECTIONCONTAIN_CHILDID];
				if(id==childId)   return that.getStoryElementByRef(story, connectionEnd[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF]);
			});
			return;
		},
		
		getNodeConnectionEnd : function(node1, connectionType, profile1, node2Type, profile2, story) {
			var node;
			if(typeof node1 === 'string')  node = this.getNodeById(story, node1);
			else node = node1;

			var that  = this;
			var out = [];
			_.each(node[node_COMMONATRIBUTECONSTANT.STORYNODE_CONNECTIONS], function(connectionId, i){
				var connection = that.getConnectionById(story, connectionId);
				if(connectionType==undefined || connection[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]==connectionType){
					var node1End = that.getConnectionEnd(story, connection, node[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
					if(profile1==undefined || node1End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_PROFILE]==profile1){
						var node2End = that.getOtherConnectionEnd(story, connection, node[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
						if(profile2==undefined || node2End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_PROFILE]==profile2){
							if(node2Type==undefined || that.getStoryElementByRef(node2End[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]){
								out.push(node2End);
							}
						}
					}
				}
			});
			return out;
		},
		
		getOtherConnectionEnd : function(story, connection, nodeId) {
			var end1 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END1];
			var end2 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END2];
			if(this.getElementIdByReference(end1[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]==nodeId)  return end2;
			else return end1;
		},
		
		getConnectionEnd : function(story, connection, nodeId) {
			var end1 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END1];
			var end2 = connection[node_COMMONATRIBUTECONSTANT.CONNECTION_END2];
			if(this.getElementIdByReference(end1[node_COMMONATRIBUTECONSTANT.CONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.IDELEMENT_ID]==nodeId)  return end1;
			else return end2;
		},
		
		getNodeById : function(story, nodeId){	return story[node_COMMONATRIBUTECONSTANT.STORY_NODE][nodeId];	},
		
		getConnectionById : function(story, connectionId){	return story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION][connectionId];	},
			
		getGroupById : function(story, groupId){	return story[node_COMMONATRIBUTECONSTANT.STORY_ELEMENTGROUP][groupId];	},
		
		deleteNodeById : function(story, nodeId){
			var out = story[node_COMMONATRIBUTECONSTANT.STORY_NODE][nodeId];
			delete story[node_COMMONATRIBUTECONSTANT.STORY_NODE][nodeId];
			return out;
		},
		
		deleteConnectionById : function(story, connectionId){	
			var out = story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION][connectionId];
			delete story[node_COMMONATRIBUTECONSTANT.STORY_CONNECTION][connectionId];
			return out;
		},

		deleteGroupById : function(story, groupId){	
			var out = story[node_COMMONATRIBUTECONSTANT.STORY_ELEMENTGROUP][groupId];
			delete story[node_COMMONATRIBUTECONSTANT.STORY_ELEMENTGROUP][groupId];
			return out;
		},
		
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("storyUtility", node_utility); 

})(packageObj);
