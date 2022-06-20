//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var node_componentUtility = {
	
		
	createComplexObject : function(complexDefinition, valueDomain, setting, handlers, request){
		
	},
	
	
		
};

//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
