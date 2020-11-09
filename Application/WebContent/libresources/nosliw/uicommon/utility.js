//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = {
		createWrapperWithId : function(id, html){
			return "<nosliw_wrapper nosliwid=\"" + id + "\">"+ html +"</nosliw_wrapper>";
		},
		findWrapperView : function(parentView, id){
			return parentView.find("nosliw_wrapper"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(id)+"']");
		},

		
		/*
		 * create place holder html with special ui id 
		 */
		createPlaceHolderWithId : function(id){
			return "<nosliw style=\"display:none;\" nosliwid=\"" + id + "\"></nosliw>";
		},
		
		createStartPlaceHolderWithId : function(id){
			return "<nosliw_start style=\"display:none;\" "+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\"" + id + "\"></nosliw_start>";
		},

		createEndPlaceHolderWithId : function(id){
			return "<nosliw_end style=\"display:none;\" "+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"=\"" + id + "\"></nosliw_end>";
		},
		
		findStartPlaceHolderView : function(parentView, id){
			return parentView.find("nosliw_start"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(id)+"']");
		},

		findEndPlaceHolderView : function(parentView, id){
			return parentView.find("nosliw_end"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(id)+"']");
		},
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
