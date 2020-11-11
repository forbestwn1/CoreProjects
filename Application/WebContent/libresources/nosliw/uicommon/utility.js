//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
		
	var loc_createPlaceHolderHtml = function(tagName, attributes, content){
		var out = "<" + tagName;
		
		var i = 0;
		_.each(attributes, function(value, name){
			out = out + " " + name + "='" + value + "' " 
			i++;
		});
		
		out = out + ">";
		if(content!=undefined)   out = out + content;
		out = out + "</" + tagName + ">";
		
		return out;
	};
	
	var loc_createInvisiblePlaceHolderWithId = function(tagName, id){
		var attrs = {};
		attrs.style = 'display:none;';
		attrs[node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID] = id;
		return loc_createPlaceHolderHtml(tagName, attrs);
	};
	
	var loc_out = {
			attachStyle : function(defScript, id){
//				var sheet = document.createElement('style')
//				sheet.id = id;
//				sheet.innerHTML = _.unescape(defScript);
//				document.style.appendChild(sheet);
				
				$('<style>').text(defScript).appendTo(document.head);
			},
			
			detachStyle : function(id){
				var sheetToBeRemoved = document.getElementById(id);
				var sheetParent = sheetToBeRemoved.parentNode;
				sheetParent.removeChild(sheetToBeRemoved);
			},
			
			/*
			 * create place holder html with special ui id 
			 */
			createPlaceHolderWithId : function(id){
				return loc_createInvisiblePlaceHolderWithId("nosliw", id);
			},
			
			createStartPlaceHolderWithId : function(id){
				return loc_createInvisiblePlaceHolderWithId("nosliw_start", id);
			},

			createEndPlaceHolderWithId : function(id){
				return loc_createInvisiblePlaceHolderWithId("nosliw_end", id);
			},
			
			findStartPlaceHolderView : function(parentView, id){
				return parentView.find("nosliw_start"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(id)+"']");
			},

			findEndPlaceHolderView : function(parentView, id){
				return parentView.find("nosliw_end"+"["+node_COMMONCONSTANT.UIRESOURCE_ATTRIBUTE_UIID+"='"+$.escapeSelector(id)+"']");
			},
			
			createPlaceHolderHtml : function(tagName, attributes, content){
				return loc_createPlaceHolderHtml(tagName, attributes, content);
			},
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("utility", node_utility); 

})(packageObj);
