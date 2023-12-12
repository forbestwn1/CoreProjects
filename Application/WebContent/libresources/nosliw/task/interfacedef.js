//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createEventObject;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_ServiceInfo;

//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskContainerInterface = function(rawInterfaceObj){
	var interfaceDef = {
		getAllItemIds : function(){	},
		
		getItemVariableInfos : function(itemId){},
		
		getItemRequirement : function(itemId){},
		
		getExecuteItemRequest : function(itemId, taskInput, handlers, request){}
	};
	return _.extend({}, interfaceDef, rawInterfaceObj);
};

var node_createTaskInterface = function(rawInterfaceObj){
	var interfaceDef = {
		
		getVariableInfos : function(){},
		
		getRequirement : function(){},
		
		getExecuteRequest : function(taskInput, handlers, request){}
	};
	return _.extend({}, interfaceDef, rawInterfaceObj);
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.event.createEventObject", function(){node_createEventObject = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});

//Register Node by Name
packageObj.createChildNode("createTaskContainerInterface", node_createTaskContainerInterface); 
packageObj.createChildNode("createTaskInterface", node_createTaskInterface); 

})(packageObj);
