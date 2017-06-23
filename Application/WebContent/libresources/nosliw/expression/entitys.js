//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
//get used node
//*******************************************   Start Node Definition  ************************************** 	

var node_Parm = function(value, name, isBase){
	this.value = value;
	this.name = name;
	this.isBase = isBase==undefined?false:isBase
};	
	
var node_Parms = function(parmsArray){
	this.pri_parmsArray = parmsArray;
	this.pri_parmsMap = {};
	_.each(parmsArray, function(parm, index, list){
		var parmName = parm.name;
		if(parmName==undefined)		parmName = node_COMMONCONSTANT.DATAOPERATION_PARM_BASENAME;
		this.pri_parmsMap[parmName] = parm.value;
		if(isBase===true)   this.baseParm = parmName;
	}, this);
};

node_Parms.prototype = {
	getParm : function(name){
		if(name===undefined)  return this.getBase();
		else return this.pri_parmsMap[this.name];
	},
	
	getBase : function(){
		return this.pri_parmsMap[this.baseParm];
	} 
};

var node_OperationContext = function(resourcesTree, aliases){

}
	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("Requester", node_Requester); 
packageObj.createNode("ServiceRequestExecuteInfo", node_ServiceRequestExecuteInfo); 
packageObj.createNode("DependentServiceRequestInfo", node_DependentServiceRequestInfo); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
