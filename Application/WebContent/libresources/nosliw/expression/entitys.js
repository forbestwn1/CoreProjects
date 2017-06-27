//get/create package
var packageObj = library.getChildPackage("entity");    

(function(packageObj){
	//get used node
	var node_COMMONCONSTANT;
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	

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
		return node_resourceUtility.getResourceFromTree(this.pri_resourcesTree);
	},
	
	getResourceByName : function(alias){
		var resourceId = this.pri_aliases[alias];
		return this.getResourceById(resourceId);
	},

};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("OperationParm", node_OperationParm); 
packageObj.createNode("OperationParms", node_OperationParms); 
packageObj.createNode("OperationContext", node_OperationContext); 

	var module = {
		start : function(packageObj){
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_resourceUtility = packageObj.getNodeData("resource.resourceUtility");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
