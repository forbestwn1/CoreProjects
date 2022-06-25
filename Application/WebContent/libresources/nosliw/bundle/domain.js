//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_getComponentLifecycleInterface;
	var node_basicUtility;

//*******************************************   Start Node Definition  ************************************** 	

var nod_createExecutablePackage = function(exePackageDefinition){
	
	var loc_complexResourcePackageByResourceId = {};

	var loc_component;
	
	
	
};
	
var node_createComplexResourcePackage = function(entityId, complexResourcePackageDefinition){

	var loc_complexResourcePackageDefinition = complexResourcePackageDefinition;

	var loc_variableDomain;
	
	var loc_component;
	
};

var loc_createComponentObject = function(entityId, complexResourcePackageDefinition, parentComponent, setting, handlers, request){
	
};



//*******************************************   End Node Definition  ************************************** 	
//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("component.getComponentLifecycleInterface", function(){node_getComponentLifecycleInterface = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("componentUtility", node_componentUtility); 

})(packageObj);
