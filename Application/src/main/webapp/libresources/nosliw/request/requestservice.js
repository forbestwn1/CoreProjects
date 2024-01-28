//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
	var node_ServiceRequestExecuteInfo;
	var node_createServiceRequestInfoCommon;
	var node_requestProcessor;
	var node_CONSTANT;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * This type of request is for service that is depend on another service 
 * Another service would do the most job
 * Once another service done its job, this service's handler will be called
 */
var node_createServiceRequestInfoService = function(service, handlers, requester_parent){
	service = node_requestUtility.buildService(service);
	
	var out = node_createServiceRequestInfoSequence(service, handlers, requester_parent);
	
	out.setDependentService = function(dependentService){
		var dependentRequest = node_createServiceRequestInfoSequence(undefined, dependentService.processors);
		dependentRequest.addRequest(dependentService.requestInfo);
		out.addRequest(dependentRequest);
	};
	
	return out;
};

/*
 * information about child service
 * child service and parent have the same reqeuster
 * 		requestInfo : 	request infor for child service
 * 		processor: 		do something after child request return
 */
var node_DependentServiceRequestInfo = function(requestInfo, processors){
	this.requestInfo = requestInfo;
	this.processors = processors;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.entity.ServiceRequestExecuteInfo", function(){node_ServiceRequestExecuteInfo = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoCommon", function(){node_createServiceRequestInfoCommon = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});


//Register Node by Name
packageObj.createChildNode("createServiceRequestInfoService", node_createServiceRequestInfoService); 
packageObj.createChildNode("entity.DependentServiceRequestInfo", node_DependentServiceRequestInfo); 

})(packageObj);
