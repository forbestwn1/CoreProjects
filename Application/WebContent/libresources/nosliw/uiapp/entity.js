//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	
//*******************************************   Start Node Definition  ************************************** 	

//information for app data segment. an element in app data array
var node_ApplicationDataSegmentInfo = function(ownerInfo, dataName, dataId, dataVersion, persist){
	this.ownerInfo = ownerInfo;
	this.dataName = dataName;
	this.id = dataId;
	this.version = dataVersion;
	this.persist = persist;
	if(this.persist==undefined)   this.persist = true;
};

//app data segment
//element in app data array
var node_ApplicationDataSegment = function(data, dataId, dataVersion){
	this.data = data;
	this.id = dataId;
	this.version = dataVersion;
};

var node_ModuleInfo = function(moduleDef){
	this.id = undefined;
	this.name = moduleDef[node_COMMONATRIBUTECONSTANT.ENTITYINFO_NAME];
	this.root = undefined;   		//view root
	this.module = undefined;  		//module object
	this.moduleDef = moduleDef;  	//module definition
	this.role = moduleDef[node_COMMONATRIBUTECONSTANT.EXECUTABLEAPPMODULE_ROLE];  //role of module (application, setting)
	this.applicationDataInfo = [];  //application data info for this module
	this.externalIO = undefined; 	//external io dataset(data set from app, data set from DB) 
	this.inputMapping = {};			//a set of mapping from externalIO to context in module, only one works with currentInputMapping
	this.inputIO = undefined;		//input io for module 
	this.currentInputMapping = undefined;	//current input mapping used 
	this.outputMapping = {}; 		//a set of mapping from context in module to externalIO
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
packageObj.createChildNode("ApplicationDataSegmentInfo", node_ApplicationDataSegmentInfo); 
packageObj.createChildNode("ModuleEventData", node_ModuleEventData); 
packageObj.createChildNode("ApplicationDataSegment", node_ApplicationDataSegment); 

})(packageObj);
