//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_UICommonUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createViewContainer = function(id, styleId, attrs){
	var loc_id = id;
	var loc_styleId = styleId;
	var loc_attrs = attrs;
	
	//render html to temporary document fragment
	var loc_fragmentDocument;
	var loc_parentView;
	
//	var loc_wrapperView;
	var loc_startEle;
	var loc_endEle;

	var loc_viewReady = false;
	
	var loc_setRootView = function(rootView){
		if (typeof value === 'string') 	rootView = $(rootView);
		loc_startEle.after(rootView);
	};
	
	var loc_prepareView = function(view){
		if(loc_viewReady==false){
			loc_fragmentDocument = $(document.createDocumentFragment());
			
			loc_parentView = $("<div>" + node_UICommonUtility.createStartPlaceHolderWithId(loc_id) + node_UICommonUtility.createEndPlaceHolderWithId(loc_id) + "</div>");
			loc_fragmentDocument.append(loc_parentView);
			loc_startEle = node_UICommonUtility.findStartPlaceHolderView(loc_parentView, loc_id); 
			loc_endEle = node_UICommonUtility.findEndPlaceHolderView(loc_parentView, loc_id); 
		
			if(view!=undefined)  loc_setRootView(view);
			
			loc_updateStyleId();

			loc_viewReady = true; 
		}
	};
	
	var loc_getViews = function(){
		return loc_startEle.add(loc_startEle.nextUntil(loc_endEle)).add(loc_endEle);
	};
	
	var loc_updateStyleId = function(){
		$(loc_getViews()).attr(node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_STYLEID,loc_styleId);
		
	};
	
	var loc_out = {

		getViews : function(){	
			loc_prepareView();
			return loc_getViews(); 
		},
		
		setContentView : function(view){
			loc_prepareView(view);
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
