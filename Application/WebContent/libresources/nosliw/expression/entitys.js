//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONCONSTANT;
	var node_resourceUtility;
	var node_expressionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_Data = function(dataTypeId, value){
	this.dataTypeId = dataTypeId;
	this.value = value;
};	
	
var node_OperationParm = function(value, name, isBase){
	this.value = value;
	this.name = name;
	this.isBase = isBase==undefined?false:isBase;
};	
	
var node_OperationParms = function(parmsArray){
	this.pri_parmsArray = parmsArray;
	this.pri_parmsMap = {};
	_.each(parmsArray, function(parm, index, list){
		var parmName = parm.name;
		if(parmName==undefined)		parmName = node_COMMONCONSTANT.DATAOPERATION_PARM_BASENAME;
		this.pri_parmsMap[parmName] = parm.value;
		if(parm.isBase===true)   this.baseParm = parmName;
	}, this);
};

node_OperationParms.prototype = {
	getParm : function(name){
		if(name===undefined)  return this.getBase();
		else return this.pri_parmsMap[name];
	},
	
	getBase : function(){
		return this.pri_parmsMap[this.baseParm];
	} 
};

var node_OperationContext = function(resourcesTree, aliases){
	this.pri_resourcesTree = resourcesTree;
	this.pri_aliases = aliases;
	this.logging = nosliw.logging;
}

node_OperationContext.prototype = {
	getResourceById : function(resourceId){
		return node_resourceUtility.getResourceFromTree(this.pri_resourcesTree, resourceId);
	},
	
	getResourceByName : function(alias){
		var resourceId = this.pri_aliases[alias];
		return this.getResourceById(resourceId);
	},

	getResourceDataByName : function(alias){
		var resourceId = this.pri_aliases[alias];
		return this.getResourceById(resourceId).resourceData;
	},
	
	operation : function(dataTypeId, operation, parmArray){
		var dataOperationResourceId = node_resourceUtility.createOperationResourceId(dataTypeId, operation);
		return node_expressionUtility.executeOperationResource(dataOperationResourceId, parmArray, this.pri_resourcesTree);
	},
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});


//Register Node by Name
packageObj.createChildNode("Data", node_Data); 
packageObj.createChildNode("OperationParm", node_OperationParm); 
packageObj.createChildNode("OperationParms", node_OperationParms); 
packageObj.createChildNode("OperationContext", node_OperationContext); 

})(packageObj);
