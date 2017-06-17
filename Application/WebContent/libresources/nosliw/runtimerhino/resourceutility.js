//get/create package
var packageObj = library.getChildPackage("utility");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_resourceUtility = 
{
		
		createOperationResourceId : function(dataTypeId, operation){
			
		}
		
};

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("resourceUtility", node_resourceUtility); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
