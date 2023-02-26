//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_packageUtility = {
	
	getAttributeType : function(attributeObj){
		var embededValue = attributeObj[node_COMMONATRIBUTECONSTANT.ATTRIBUTEENTITY_VALUE];
		if(embededValue[node_COMMONATRIBUTECONSTANT.CONTAINERENTITY_ELEMENTTYPE]!=undefined){
			return node_CONSTANT.ATTRIBUTE_TYPE_CONTAINER;
		}
		else{
			return node_CONSTANT.ATTRIBUTE_TYPE_NORMAL;
		}
	}

	
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("packageUtility", node_packageUtility); 

})(packageObj);
