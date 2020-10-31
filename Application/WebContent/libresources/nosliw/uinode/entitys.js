//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
var node_COMMONCONSTANT;
var node_COMMONATRIBUTECONSTANT;

//*******************************************   Start Node Definition  ************************************** 	
var node_createUINodeGroupView = function(uiNodes, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;

	var loc_uiNodes = uiNodes;
	var loc_uiNodeViews = [];
	_.each(loc_uiNodes, function(uiNode, i){
		var uiNodeType = uiNode.getNodeType();
		if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_UIDATA){
			loc_uiNodeViews.push(node_createUINodeTagView(uiNode, id+"_"+i, parentContext));
		}
		else if(uiNodeType==node_COMMONCONSTANT.STORYNODE_TYPE_HTML){
			loc_uiNodeViews.push(node_createUINodeHtmlView(uiNode, id+"_"+i, parentContext));
		}
	});
	
	var loc_viewContainer = loc_createViewContainer();
	var loc_childrenProcessed = false;
	
	var loc_prepareChildrenView = function(){
		if(loc_childrenProcessed==false){
			_.each(loc_uiNodeViews, function(uiNodeView, i){
				loc_viewContainer.append(uiNodeView);
			});
			loc_childrenProcessed = true;
		}
	};
	
	loc_out = {
		getChildren : function(){   return loc_uiNodeViews;  },
		
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

var node_createUINodeHtmlView = function(uiNode, id, parentContext){
	var loc_id = id;
	var loc_parentContext = parentContext;
	var loc_uiNode = uiNode;
	
	var loc_html;
	var loc_tagByChild = {};
	
	var loc_viewContainer = loc_createViewContainer();
	
	var loc_view;
	
	var loc_init = function(){
		var htmlStoryNode = loc_uiNode.getStoryNode();
		
		//parse html
		var html = htmlStoryNode[node_COMMONATRIBUTECONSTANT.STORYNODEUIHTML_HTML];
		var startIndex = html.indexOf("{{");
		while(startIndex!=-1){
			var endIndex = html.indexOf("}}");
			var childId = html.substring(startIndex+2, endIndex);
			var placeHolder = "<nosliw_start_"+loc_id+"_"+childId+"></nosliw>"+"<nosliw_end_"+loc_id+"_"+childId+"></nosliw>";
			html = html.substring(0, startIndex) + placeHolder + html.substring(endIndex+2);
			loc_tagByChild[childId] = [];
		}
		loc_html = html;
		
		//process children
		var childrenNodeInfo = loc_uiNode.getChildrenInfo();
		_.each(childrenNodeInfo, function(childNodeInfo, i){
			loc_tagByChild[childNodeInfo.childId].push(node_createUINodeTagView(childNodeInfo.childNode, loc_id+"_"+i, loc_parentContext));
		});
	};
	
	var loc_childrenProcessed = false;
	
	var loc_prepareChildrenView = function(){
		if(loc_childrenProcessed==false){
			loc_view = $(loc_html);
			_.each(loc_tagByChild, function(tagViews, childId){
				_.each(tagViews, function(tagView, i){
					tagView.insertAfter($("#."+tagView.getId()));
				});
			});
			loc_viewContainer.append(loc_view);
			loc_childrenProcessed = true;
		}
	};

	var loc_out = {
		
		getTagViewsByChild : function(){	return loc_tagByChild;	},
		
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
	
	loc_init();
	return lot_out;
};

var node_createUINodeTagView = function(uiNode, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;
	var loc_uiNode = uiNode;
	
	var loc_uiTag;

	var loc_viewContainer = loc_createViewContainer();
	
	var loc_out = {
			
		getUINodeType : function(){   loc_uiNode[node_COMMONATRIBUTECONSTANT.STORYELEMENT_TYPE];    }, 	
		
		getUINode : function(){   return loc_uiNode;  },
		getId : function(){  return loc_id;   },
		
		setUITag : function(uiTag){  loc_uiTag = uiTag;   },
		
		getStartElement : function(){  return loc_viewContainer.getStartElement();   },
		getEndElement : function(){  return loc_viewContainer.getEndElement();   },
		//append this views to some element as child
		appendTo : function(ele){  loc_viewContainer.appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_viewContainer.insertAfter(ele);		},
		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_viewContainer);		},
	};

	return loc_out;
};

var loc_createViewContainer = function(id){
	var loc_id = id;
	
	//render html to temporary document fragment
	var loc_fragmentDocument = $(document.createDocumentFragment());
	var loc_parentView = $("<div></div>");
	loc_fragmentDocument.append(loc_parentView);

	var loc_startEle = $("<nosliw_start_"+loc_id+"></nosliw>");
	var loc_endEle = $("<nosliw_end_"+loc_id+"></nosliw>");

	loc_parentView.append(loc_startEle);
	loc_parentView.append(loc_endEle);
	
	var loc_out = {

		getViews : function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle); },
		
		getStartElement : function(){  return loc_startEle;   },
		getEndElement : function(){    return loc_endEle;    },
		
		//append this views to some element as child
		appendTo : function(ele){  this.getViews().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	this.getViews().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(this.getViews());		},

		append : function(views){  this.getStartElement().after(views);   }
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUINodeGroupView", node_createUINodeGroupView); 

})(packageObj);
