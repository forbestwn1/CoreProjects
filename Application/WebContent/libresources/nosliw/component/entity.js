//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_makeObjectWithType;
	var node_getObjectType;
	var node_objectOperationUtility;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_CommandResult = function(requestResult, commandInfo){
	this.requestResult = requestResult;
	this.commandInfo = commandInfo;
};

var node_CommandInfo = function(commandName, commandParm){
	this.commandName = commandName;
	this.commandParm = commandParm;
};

var node_DecorationInfo = function(type, id, name, resource, configure){
	this.id = id;
	this.name = name;
	this.configure = configure;
	this.resource = resource;        //resource
	this.decoration = undefined;		//object build from resource
	if(this.name==undefined)   this.name = this.id;   
};

var node_DecorationInfo = function(name, type, id, configure){
	this.name = name;
	this.type = type;
	this.id = id;
	this.configure = configure;
};

//interface exposed by entity for either internal or external
var node_InterfaceInfo = function(name, description, visibility, isasync){
	this.name = name;
	this.description = description;
	this.visibility = visibility;
	this.isAsync = isasync;
};

var node_interfaceExecutable = function(){
//	execute : function(args){
//		
//	}
};



//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithtype.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("CommandResult", node_CommandResult); 
packageObj.createChildNode("DecorationInfo", node_DecorationInfo); 
packageObj.createChildNode("InterfaceInfo", node_InterfaceInfo); 

})(packageObj);
