//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
//*******************************************   Start Node Definition  ************************************** 	

//activity plug in execute result
//  resultName : name of the result
//  result: result value map (value name / value)
var node_NormalActivityResult = function(resultName, resultValue){
	this.resultName = resultName;
	this.resultValue = resultValue; 
};

//normal activity output (next activity + context)
var node_NormalActivityOutput = function(next, context){
	this.next = next;
	this.context = context;
};

//end activity output (result name + context)
var node_EndActivityOutput = function(resultName, context){
	this.resultName = resultName;
	this.context = context;
};

//process output
var node_ProcessResult = function(resultName, value){
	this.resultName = resultName;
	this.value = value;
}

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});

//Register Node by Name
packageObj.createChildNode("NormalActivityResult", node_NormalActivityResult); 

})(packageObj);
