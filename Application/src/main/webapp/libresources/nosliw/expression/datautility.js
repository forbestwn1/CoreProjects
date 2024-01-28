//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_Data;

//*******************************************   Start Node Definition  ************************************** 	

var node_dataUtility = 
{
	createStringData : function(stringValue){
		return new node_Data("test.string;1.0.0", stringValue);
	},
	
		
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("expression.entity.Data", function(){node_Data = this.getData();});

//Register Node by Name
packageObj.createChildNode("dataUtility", node_dataUtility); 

})(packageObj);
