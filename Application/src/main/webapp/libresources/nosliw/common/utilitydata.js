//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_basicUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_dataUtility = 
{
		isDataEqual : function(data1, data2){
			if(data1==undefined&&data2==undefined)   return true;
			if(data1==undefined)   return false;
			if(data2==undefined)   return false;
			
			var dataType1 = data1[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
			var dataType2 = data2[node_COMMONATRIBUTECONSTANT.DATA_DATATYPEID];
			
			if(dataType1!=dataType2)  return false;
			return node_basicUtility.isValueEqual(data1[node_COMMONATRIBUTECONSTANT.DATA_VALUE], data2[node_COMMONATRIBUTECONSTANT.DATA_VALUE]);
		},

};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("dataUtility", node_dataUtility); 
	
})(packageObj);
