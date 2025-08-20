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

var node_DecorationInfo = function(name, type, version, id, configure){
	this.name = name;
	this.type = type;
	this.version = version;
	this.id = id;
	this.configure = configure;
};

//interface exposed by entity for either internal or external
var node_InterfaceInfo = function(name, description, owner, visibility, isasync){
	this.name = name;
	this.description = description;
	this.owner = owner;
	this.visibility = visibility;
	this.isAsync = isasync;
};

//executable with source of function
var node_createInterfaceExecutableFunction = function(fuc, thisContext){
	var loc_function = fun;
	var loc_thisContext = thisContext;
	
	var loc_out = {
		execute : function(){
			return loc_function.apply(loc_thisContext, arguments);
		}
	};
	return loc_out;
};



//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.makeObjectWithType", function(){node_makeObjectWithType = this.getData();});
nosliw.registerSetNodeDataEvent("common.interfacedef.getObjectType", function(){node_getObjectType = this.getData();});
nosliw.registerSetNodeDataEvent("common.utility.objectOperationUtility", function(){node_objectOperationUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("CommandResult", node_CommandResult); 
packageObj.createChildNode("DecorationInfo", node_DecorationInfo); 
packageObj.createChildNode("InterfaceInfo", node_InterfaceInfo); 

})(packageObj);
