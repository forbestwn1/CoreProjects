//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

var node_ApplicationDataInfo = function(dataName, dataId, dataVersion){
	this.dataName = dataName;
	this.dataId = dataId;
	this.dataVersion = dataVersion;
};
	
var node_ModuleInfo = function(moduleDef){
	this.id = undefined;
	this.module = undefined;
	this.moduleDef = moduleDef;
	this.role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];
	this.applicationDataInfo = [];  //application data info for this module
	this.externalIO = undefined; //
	this.inputMapping = {};
	this.inputIO = undefined;
	this.currentInputMapping = undefined;
	this.outputMapping = {};
};
		
var node_ModuleEventData = function(moduleInfo, eventName, eventData){
	this.moduleInfo = moduleInfo;
	this.eventName = eventName;
	this.eventData = eventData;
};
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});

//Register Node by Name
packageObj.createChildNode("ModuleInfo", node_ModuleInfo); 
packageObj.createChildNode("ApplicationDataInfo", node_ApplicationDataInfo); 
packageObj.createChildNode("ModuleEventData", node_ModuleEventData); 

})(packageObj);
