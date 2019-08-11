//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

var node_utility = function(){
	
	var loc_out = {

		buildDataAssociationName : function(sourceType, sourceName, targetType, targetName){
			return sourceType+"_"+sourceName+"---"+targetType+"_"+targetName;
		},
	};
		
	return loc_out;
}();

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("dataAssociationUtility", node_utility); 

})(packageObj);
