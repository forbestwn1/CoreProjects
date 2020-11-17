//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_UICommonUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createViewContainer = function(id, attrs, html){
	var loc_id = id;
	var loc_html = html;
	var loc_attrs = attrs;
	
	//render html to temporary document fragment
	var loc_fragmentDocument;
	var loc_parentView;
	
	var loc_wrapperView;
	var loc_startEle;
	var loc_endEle;

	var loc_viewReady = false;
	
	var loc_prepareView = function(){
		if(loc_viewReady==false){
			loc_fragmentDocument = $(document.createDocumentFragment());
			
			if(loc_attrs==undefined)  loc_attrs = {};
			loc_attrs[node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID] = loc_id;
//			loc_attrs.style = 'display:contents;';
			loc_parentView = $("<div>" + node_UICommonUtility.createPlaceHolderHtml("nosliw_wrapper", loc_attrs, node_UICommonUtility.createStartPlaceHolderWithId(loc_id) + (loc_html==undefined?"":loc_html) + node_UICommonUtility.createEndPlaceHolderWithId(loc_id)) + "</div>");
			loc_fragmentDocument.append(loc_parentView);
			
			loc_wrapperView = loc_parentView.find("nosliw_wrapper"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(loc_id)+"']"); 
			loc_startEle = node_UICommonUtility.findStartPlaceHolderView(loc_parentView, loc_id); 
			loc_endEle = node_UICommonUtility.findEndPlaceHolderView(loc_parentView, loc_id); 
			
			loc_viewReady = true; 
		}
	};
	
	var loc_out = {

		getViews : function(){	
			loc_prepareView();
//			return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle); 
			return loc_wrapperView; 
		},
		
		getStartElement : function(){  
			loc_prepareView();
			return loc_startEle;   
		},
		getEndElement : function(){    
			loc_prepareView();
			return loc_endEle;    
		},
		
		//append this views to some element as child
		appendTo : function(ele){
			this.getViews().appendTo($(ele).last());   
		},
		//insert this resource view after some element
		insertAfter : function(ele){	this.getViews().insertAfter($(ele).last());		},

		//remove all elements from outsiders parents and put them back under parentView
		detachViews : function(){	
			loc_parentView.append(this.getViews());
		},

		append : function(views){  views.insertBefore(this.getEndElement());   },
		
		findElement : function(select){    
			loc_prepareView();
			return loc_startEle.nextUntil(loc_endEle.next()).find(select).addBack(select);   
		},
	};
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("uicommon.utility", function(){node_UICommonUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createViewContainer", node_createViewContainer); 

})(packageObj);