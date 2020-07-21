//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_storyUtility;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_appChangeNew = function(story, changeItem){
		node_storyUtility.addStoryElement(story, changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_TARGETCATEGARY], changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_ELEMENT]);
	};
	
	var loc_out = {
		
		applyChange : function(story, changeItem){
			var changeType = changeItem[node_COMMONATRIBUTECONSTANT.CHANGEITEM_CHANGETYPE];
			if(changeType==node_COMMONCONSTANT.STORYDESIGN_CHANGETYPE_NEW){
				loc_appChangeNew(story, changeItem);
			}
		},
		
		createChangeItemPatch : function(element, path, value){
			
		},
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.storyUtility", function(){node_storyUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("storyChangeUtility", node_utility); 

})(packageObj);
