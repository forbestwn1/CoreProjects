//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_out = {

		getContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE 
			];
		},

		getReversedContextTypes : function(){
			return [ 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PRIVATE, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PUBLIC, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_PROTECTED, 
				node_COMMONCONSTANT.UIRESOURCE_CONTEXTTYPE_INTERNAL, 
			];
		},
		
		mergeContext : function(source, target, isFlat){
			if(target==undefined)   target = {};
			if(isFlat==true){
				_.each(source, function(value, name){
					target[name] = value;
				});
			}
			else{
				_.each(source, function(c, categary){
					var cc = target[categary];
					if(cc==undefined){
						cc = {};
						target[categary] = cc;
					}
					_.each(c, function(ele, name){
						cc[name] = ele;
					});
				});
			}
			return target;
		}
		

	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ioTaskUtility", node_utility); 

})(packageObj);
