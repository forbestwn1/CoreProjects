//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;

//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var node_utility = function(){

	var loc_out = {
		
		getDesignStages : function(design){
			var info = design[node_COMMONATRIBUTECONSTANT.ENTITYINFO_INFO];
			return info[node_COMMONCONSTANT.STORYDESIGN_INFO_STAGES];
		},

		getStepStage : function(step){
			var info = step.info;
			return info[node_COMMONCONSTANT.STORYDESIGN_CHANGE_INFO_STAGE];
		},
		
		
	};		
			
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("designUtility", node_utility); 

})(packageObj);
