//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;
var node_createViewContainer;
var node_UICommonUtility;

//*******************************************   Start Node Definition  ************************************** 	
var node_createUINodeGroupView = function(uiNodes, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;

	var loc_uiNodes = uiNodes;
	var loc_uiNodeViews = [];
	_.each(loc_uiNodes, function(uiNode, i){
		loc_uiNodeViews.push(node_createUINodeView(uiNode, uiNode.getId(), parentContext));
	});
	
	var loc_viewContainer = node_createViewContainer(loc_id);
	var loc_childrenProcessed = false;
	
	var loc_prepareChildrenView = function(){
		if(loc_childrenProcessed==false){
			_.each(loc_uiNodeViews, function(uiNodeView, i){
				loc_viewContainer.append(uiNodeView.getViews());
			});
			loc_childrenProcessed = true;
		}
	};
	
	loc_out = {
		getChildren : function(){   return loc_uiNodeViews;  },
		
		getViewContainer : function(){   return loc_viewContainer;   },
		getViews : function(){     return loc_viewContainer.getViews();     },
		getStartElement : function(){  return loc_viewContainer.getStartElement();   },
		getEndElement : function(){  return loc_viewContainer.getEndElement();   },
		//append this views to some element as child
		appendTo : function(ele){ 
			loc_prepareChildrenView();
			loc_viewContainer.appendTo(ele);   
		},
		//insert this resource view after some element
		insertAfter : function(ele){	
			loc_prepareChildrenView();
			loc_viewContainer.insertAfter(ele);		
		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_viewContainer.detachViews();		},
	};

	return loc_out;
};

var node_createUINodeView = function(uiNode, id, parentContext){
	var out;
	var uiNodeType = uiNode.getNodeType();
	if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA){
		out = node_createUINodeTagView(uiNode, uiNode.getId(), parentContext);
	}
	else if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_HTML){
		out = node_createUINodeHtmlView(uiNode, uiNode.getId(), parentContext);
	}
	return out;
};

var node_createUINodeHtmlView = function(uiNode, id, parentContext){
	var loc_id = id;
	var loc_parentContext = parentContext;
	var loc_uiNode = uiNode;
	
	var loc_html;

	var loc_childrenViewById = {};

	var loc_viewContainer = node_createViewContainer(loc_id);
	
	var loc_view;
	
	var loc_init = function(){
		var htmlStoryNode = loc_uiNode.getStoryNode();

		//organize child by childId
		var childrenInfoByChildId = {};
		var childrenNodeInfo = loc_uiNode.getChildrenInfo();
		_.each(childrenNodeInfo, function(childNodeInfo, i){
			var childId = childNodeInfo.childId;
			var childrenInfo = childrenInfoByChildId[childId];
			if(childrenInfo==undefined){
				childrenInfo = [];
				childrenInfoByChildId[childId] = childrenInfo;
			}
			childrenInfo.push(childNodeInfo);
		});

		//parse html
		var html = htmlStoryNode[node_COMMONATRIBUTECONSTANT.STORYNODEUIHTML_HTML];
		var startIndex = html.indexOf("{{");
		while(startIndex!=-1){
			var endIndex = html.indexOf("}}");
			var childId = html.substring(startIndex+2, endIndex);
			var replace = "";
			
			var childrenInfo = childrenInfoByChildId[childId];
			_.each(childrenInfo, function(childInfo, index){
				var childElementId = loc_getChildElementId(childInfo, index);
				var uiNode = childInfo.childNode;
				var uiNodeType = uiNode.getNodeType();
				if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA){
					replace = replace +  node_UICommonUtility.createStartPlaceHolderWithId(childElementId) + node_UICommonUtility.createEndPlaceHolderWithId(childElementId); 
					loc_childrenViewById[childElementId] = node_createUINodeView(uiNode, childElementId, loc_parentContext);
				}
				else if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_HTML){
					//merge html with parent html
					var childHtmlNodeView = node_createUINodeHtmlView(uiNode, childElementId, parentContext);
					replace = replace + childHtmlNodeView.getHtml();
					_.each(childHtmlNodeView.getChildrenView(), function(childView, id){
						loc_childrenViewById[id] = childView;
					});
				}
			});
			
			html = html.substring(0, startIndex) + replace + html.substring(endIndex+2);

			startIndex = html.indexOf("{{");
		}
		loc_html = html;
		
	};
	
	var loc_getChildElementId = function(childInfo, index){
		var id = "child_"+childInfo.childNode.getId()+"_"+childInfo.childId+"_"+index;
		return id;
	};

	var loc_prepareChildrenView = function(){
		if(loc_childrenProcessed==false){
			loc_view = $(loc_html);
			_.each(loc_childrenViewById, function(tagView, viewId){
				var childEle = node_UICommonUtility.findStartPlaceHolderView(loc_view, viewId); 
				tagView.insertAfter(childEle);
			});
			loc_viewContainer.append(loc_view);
			loc_childrenProcessed = true;
		}
	};
	
	var loc_getPlaceHolderElementId = function(childId){
		var id = "placeholder_"+loc_id+"_"+childId;
		return id;
	};
	
	var loc_childrenProcessed = false;
	
	var loc_out = {
		getId : function(){   return loc_id;     },
		getUINodeType : function(){   return loc_uiNode.getNodeType();    }, 	
			
		getUINode : function(){   return loc_uiNode;  },
		
		getHtml : function(){    return loc_html;    },
		
		getChildrenView : function(){	return loc_childrenViewById;	},
		
		getViewContainer : function(){   return loc_viewContainer;   },

		getViews : function(){     
			loc_prepareChildrenView();
			return loc_viewContainer.getViews();     
		},
		getStartElement : function(){  
			loc_prepareChildrenView();
			return loc_viewContainer.getStartElement();   
		},
		getEndElement : function(){  
			loc_prepareChildrenView();
			return loc_viewContainer.getEndElement();   
		},
		//append this views to some element as child
		appendTo : function(ele){ 
			loc_prepareChildrenView();
			loc_viewContainer.appendTo(ele);   
		},
		//insert this resource view after some element
		insertAfter : function(ele){	
			loc_prepareChildrenView();
			loc_viewContainer.insertAfter(ele);		
		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_viewContainer.detachViews();		},
	};
	
	loc_init();
	return loc_out;
};

var node_createUINodeTagView = function(uiNode, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;
	var loc_uiNode = uiNode;
	
	var loc_uiTag;

	var loc_viewContainer = node_createViewContainer(loc_id);
	
	var loc_out = {
		getId : function(){  return loc_id;   },
			
		getUINodeType : function(){   return loc_uiNode.getNodeType();    }, 	
		
		getUINode : function(){   return loc_uiNode;  },
		
		getTagId : function(){
			var out;
			if(loc_uiNode.getTagId!=undefined)  out = loc_uiNode.getTagId(); 
			else{
				var uiTagStoryNode = loc_uiNode.getStoryNode();
				out = uiTagStoryNode[node_COMMONATRIBUTECONSTANT.STORYNODEUITAG_TAGNAME];
			}
			return out;
		},
		
		setUITag : function(uiTag){  loc_uiTag = uiTag;   },
		
		getAttributes : function(){
			var out;
			if(loc_uiNode.getAttributes!=undefined)  out = loc_uiNode.getAttributes(); 
			else{
				var uiTagStoryNode = loc_uiNode.getStoryNode();
				out = uiTagStoryNode[node_COMMONATRIBUTECONSTANT.STORYNODEUITAG_ATTRIBUTES];
			}
			return out;
		},
		
		getViewContainer : function(){   return loc_viewContainer;   },

		getViews : function(){     return loc_viewContainer.getViews();     },
		getStartElement : function(){  return loc_viewContainer.getStartElement();   },
		getEndElement : function(){  return loc_viewContainer.getEndElement();   },
		//append this views to some element as child
		appendTo : function(ele){  
			loc_viewContainer.appendTo(ele);   
		},
		//insert this resource view after some element
		insertAfter : function(ele){
			loc_viewContainer.insertAfter(ele);		
		},
		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_viewContainer);		},
	};

	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.createViewContainer", function(){node_createViewContainer = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.utility", function(){node_UICommonUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUINodeGroupView", node_createUINodeGroupView); 

})(packageObj);
