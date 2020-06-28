//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
var node_basicUtility;	
//*******************************************   Start Node Definition  ************************************** 	


var node_createUINodeGroupView = function(uiNodes, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;

	var loc_uiNodes = uiNodes;
	var loc_uiNodeViews = [];
	_.each(loc_uiNodes, function(uiNode, i){
		loc_uiNodeViews.push(node_createUINodeView(uiNode, id+"_"+i, parentContext));
	});
	
	//render html to temporary document fragment
	var loc_fragmentDocument = $(document.createDocumentFragment());
	var loc_parentView = $("<div></div>");
	loc_fragmentDocument.append(loc_parentView);

	var loc_startEle = $("<nosliw_start_"+loc_id+"></nosliw>");
	var loc_endEle = $("<nosliw_end_"+loc_id+"></nosliw>");
	
	var loc_uiTag;

	var loc_getViews = function(){	return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle);  };

	var loc_prepareChildrenView = function(){
		loc_parentView.append(loc_startEle);
		_.each(loc_uiNodeViews, function(uiNodeView, i){
			uiNodeView.appendTo(loc_parentView);
		});
		loc_parentView.append(loc_endEle);
	};
	
	loc_out = {
		getStartElement : function(){  return loc_startEle;   },
		getEndElement : function(){  return loc_endEle;   },

		getChildren : function(){   return loc_uiNodeViews;  },
		
		//append this views to some element as child
		appendTo : function(ele){ 
			loc_prepareChildrenView();
			loc_getViews().appendTo(ele);   
		},
		//insert this resource view after some element
		insertAfter : function(ele){	
			loc_prepareChildrenView();
			loc_getViews().insertAfter(ele);		
		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_getViews());		},

	};

	return loc_out;
};


var node_createUINodeView = function(uiNode, id, parentContext){
	
	var loc_id = id;
	var loc_parentContext = parentContext;
	var loc_uiNode = uiNode;
	
	var loc_uiTag;

	//render html to temporary document fragment
	var loc_fragmentDocument = $(document.createDocumentFragment());
	var loc_parentView = $("<div></div>");
	loc_fragmentDocument.append(loc_parentView);

	var loc_startEle = $("<nosliw_start_"+loc_id+"></nosliw>");
	var loc_endEle = $("<nosliw_end_"+loc_id+"></nosliw>");

	loc_parentView.append(loc_startEle);
	loc_parentView.append(loc_endEle);
	
	var loc_getViews = function(){	
		var out = loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle);
		return out;
	};
	
	var loc_out = {
		getStartElement : function(){  return loc_startEle;   },
		getEndElement : function(){  return loc_endEle;   },
		getUINode : function(){   return loc_uiNode;  },
		getId : function(){  return loc_id;   },
		
		setUITag : function(uiTag){  loc_uiTag = uiTag;   },
		
		//append this views to some element as child
		appendTo : function(ele){  loc_getViews().appendTo(ele);   },
		//insert this resource view after some element
		insertAfter : function(ele){	loc_getViews().insertAfter(ele);		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	loc_parentView.append(loc_getViews());		},

	};

	return loc_out;
};



//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("createUINodeGroupView", node_createUINodeGroupView); 

})(packageObj);
