//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

//process output
var node_ExecutableResult = function(resultName, value){
	this.resultName = resultName;
	this.resultValue = value;
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("ExecutableResult", node_ExecutableResult); 

})(packageObj);
