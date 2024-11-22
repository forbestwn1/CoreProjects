//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_ElementId;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ChildStoryNodeInfo;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_isValidElement = function(element, validOnly){
		if(validOnly==false)  return true;
		else return element[node_COMMONATRIBUTECONSTANT.STORYSTORYELEMENT_ENABLE]!=false;
	};
	
	var loc_getChildNodesInfo = function(parent, story, validOnly) {
		var out = [];
		var childConnectionEnds = loc_getNodeConnectionEnd(parent, node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
		_.each(childConnectionEnds, function(connectionEnd, i){
			var connectionId = connectionEnd[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_CONNECTIONID];
			var connection = loc_getConnectionById(story, connectionId);
			if(loc_isValidElement(connection, validOnly)){
				var childNode = loc_getStoryElementByRef(story, connectionEnd[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_NODEREF]);
				if(loc_isValidElement(childNode, validOnly)){
					out.push(new node_ChildStoryNodeInfo(childNode, connectionId, connection[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONCONTAIN_CHILDID]));
				}
			}
		});
		return out;
	};

	var loc_getStoryElementByRef = function(story, ref){
		var elementId = loc_getElementIdByReference(story, ref);
		return loc_getStoryElement(story, elementId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY], elementId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID]);
	};

	var loc_getNodeConnectionEnd = function(node1, connectionType, profile1, node2Type, profile2, story) {
		var node;
		if(typeof node1 === 'string')  node = loc_getNodeById(story, node1);
		else node = node1;

		var out = [];
		_.each(node[node_COMMONATRIBUTECONSTANT.STORYNODE_CONNECTIONS], function(connectionId, i){
			var connection = loc_getConnectionById(story, connectionId);
			if(connectionType==undefined || connection[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]==connectionType){
				var node1End = loc_getConnectionEnd(story, connection, node[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
				if(profile1==undefined || node1End[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_PROFILE]==profile1){
					var node2End = loc_getOtherConnectionEnd(story, connection, node[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID]);
					if(profile2==undefined || node2End[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_PROFILE]==profile2){
						if(node2Type==undefined || loc_getStoryElementByRef(node2End[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]){
							out.push(node2End);
						}
					}
				}
			}
		});
		return out;
	};

	var loc_getStoryElement = function(story, elementCategary, elementId){
		var out;
		if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_NODE){
			out = loc_getNodeById(story, elementId);
		}
		else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_CONNECTION){
			out = loc_getConnectionById(story, elementId);
		}
		else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
			out = loc_getGroupById(story, elementId);
		}
		return out;
	};

	var loc_getNodeById = function(story, nodeId){	return story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE][nodeId];	};
	var loc_getConnectionById = function(story, connectionId){	return story[node_COMMONATRIBUTECONSTANT.STORYSTORY_CONNECTION][connectionId];	};
	var loc_getGroupById = function(story, groupId){	return story[node_COMMONATRIBUTECONSTANT.STORYSTORY_ELEMENTGROUP][groupId];	};

	var loc_getElementIdByReference = function(story, reference){
		if(reference[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY]!=null)  return reference;
		else return story[node_COMMONATRIBUTECONSTANT.STORYSTORY_ALIAS][reference[node_COMMONATRIBUTECONSTANT.STORYALIASELEMENT_NAME]];
	};

	var loc_getConnectionEnd = function(story, connection, nodeId) {
		var end1 = connection[node_COMMONATRIBUTECONSTANT.STORYCONNECTION_END1];
		var end2 = connection[node_COMMONATRIBUTECONSTANT.STORYCONNECTION_END2];
		if(loc_getElementIdByReference(story, end1[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID]==nodeId)  return end1;
		else return end2;
	};

	var loc_getOtherConnectionEnd = function(story, connection, nodeId) {
		var end1 = connection[node_COMMONATRIBUTECONSTANT.STORYCONNECTION_END1];
		var end2 = connection[node_COMMONATRIBUTECONSTANT.STORYCONNECTION_END2];
		if(loc_getElementIdByReference(story, end1[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_NODEREF])[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID]==nodeId)  return end2;
		else return end1;
	};

	var loc_out = {
		
		getElementIdWithoutNodeType : function(fullId){
			var i = fullId.indexOf(";");
			var out = fullId.substring(i+1);
			return out;
		},
			
		getNextIdForStory : function(story){
			var index = this.getIdIndex(story);
			index = index + 1;
			this.setIdIndex(story, index);
			return index+"";
		},
		
		getIdIndex : function(story){
			var info = story[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			return info[node_COMMONCONSTANT.STORY_INFO_IDINDEX];
		},
		
		setIdIndex : function(story, index){
			var info = story[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			info[node_COMMONCONSTANT.STORY_INFO_IDINDEX] = index;
		},
			
		getElementIdByReference : function(story, reference){	return loc_getElementIdByReference(story, reference);	},	
			
		getQuestionTargetElement : function(story, question){
			return loc_getStoryElementByRef(story, question[node_COMMONATRIBUTECONSTANT.STORYQUESTION_TARGETREF]);
		},
			
		getAllNodes : function(story){  return story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE];   },
		
		addStoryElement : function(story, element, aliasObj){
			var elementCategary = element[node_COMMONATRIBUTECONSTANT.STORYELEMENT_CATEGARY];
			var elementId = element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID];
			if(elementId==undefined){
				elementId = this.getNextIdForStory(story);
				element[node_COMMONATRIBUTECONSTANT.ENTITYINFO_ID] = elementId;
			}
			if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_NODE){
				story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE][elementId] = element;
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_CONNECTION){
				story[node_COMMONATRIBUTECONSTANT.STORYSTORY_CONNECTION][elementId] = element;
			}
			else if(elementCategary==node_COMMONCONSTANT.STORYELEMENT_CATEGARY_GROUP){
				story[node_COMMONATRIBUTECONSTANT.STORY_ELEMENTGROUP][elementId] = element;
			}
			//process alias
			if(aliasObj!=undefined){
				story[node_COMMONATRIBUTECONSTANT.STORYSTORY_ALIAS][aliasObj[node_COMMONATRIBUTECONSTANT.STORYALIASELEMENT_NAME]] = new node_ElementId(elementCategary, elementId); 
			}
			return element;
		},
		
		getStoryElementByRef : function(story, ref){	return loc_getStoryElementByRef(story, ref);	},

		getStoryElement : function(story, elementCategary, elementId){		return loc_getStoryElement(story, elementCategary, elementId);		},
		
		deleteStoryElementByRef : function(story, ref){
			var elementId = loc_getElementIdByReference(story, ref);
			return this.deleteStoryElement(elementId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_CATEGARY], elementId[node_COMMONATRIBUTECONSTANT.STORYIDELEMENT_ID]);
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
		
		getStoryNodeByTypes : function(story, nodeTypes){
			var out = [];
			_.each(story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE], function(node, id){
				if(nodeTypes.includes(node[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE])){
					out.push(node);
				}
			});
			return out;
		},
		
		getStoryNodeByType : function(story, nodeType) {
			var out = [];
			_.each(story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE], function(node, id){
				if(nodeType==node[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE]){
					out.push(node);
				}
			});
			return out;
		},
		
		getChildNodesInfo : function(parent, story, onlyValid){		return loc_getChildNodesInfo(parent, story, onlyValid);		},
		getAllChildNodesInfo : function(parent, story) {	return loc_getChildNodesInfo(parent, story, false);	},

		getChildNodeByChildId : function(parent, childId, story) {
			var childConnectionEnds = loc_getNodeConnectionEnd(parent, node_COMMONCONSTANT.STORYCONNECTION_TYPE_CONTAIN, node_COMMONCONSTANT.STORYNODE_PROFILE_CONTAINER, undefined, undefined, story);
			_.each(childConnectionEnds, function(connectionEnd, i){
				var id = loc_getConnectionById(story, connectionEnd[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_CONNECTIONID])[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONCONTAIN_CHILDID];
				if(id==childId)   return loc_getStoryElementByRef(story, connectionEnd[node_COMMONATRIBUTECONSTANT.STORYCONNECTIONEND_NODEREF]);
			});
			return;
		},
		
		getNodeConnectionEnd : function(node1, connectionType, profile1, node2Type, profile2, story) {
			return loc_getNodeConnectionEnd(node1, connectionType, profile1, node2Type, profile2, story);
		},
		
		getOtherConnectionEnd : function(story, connection, nodeId) {  return loc_getOtherConnectionEnd(story, connection, nodeId); },
		
		getConnectionEnd : function(story, connection, nodeId) {		return loc_getConnectionEnd(story, connection, nodeId); },
		
		getNodeById : function(story, nodeId){	return loc_getNodeById(story, nodeId);	},
		
		getConnectionById : function(story, connectionId){	return loc_getConnectionById(story, connectionId);	},
			
		getGroupById : function(story, groupId){	return loc_getGroupById(story, groupId);	},
		
		deleteNodeById : function(story, nodeId){
			var out = story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE][nodeId];
			delete story[node_COMMONATRIBUTECONSTANT.STORYSTORY_NODE][nodeId];
			return out;
		},
		
		deleteConnectionById : function(story, connectionId){	
			var out = story[node_COMMONATRIBUTECONSTANT.STORYSTORY_CONNECTION][connectionId];
			delete story[node_COMMONATRIBUTECONSTANT.STORYSTORY_CONNECTION][connectionId];
			return out;
		},

		deleteGroupById : function(story, groupId){	
			var out = story[node_COMMONATRIBUTECONSTANT.STORYSTORY_ELEMENTGROUP][groupId];
			delete story[node_COMMONATRIBUTECONSTANT.STORYSTORY_ELEMENTGROUP][groupId];
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
nosliw.registerSetNodeDataEvent("application.instance.story.entity.ElementId", function(){node_ElementId = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.entity.ChildStoryNodeInfo", function(){node_ChildStoryNodeInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("storyUtility", node_utility); 

})(packageObj);
