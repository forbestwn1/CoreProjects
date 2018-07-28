//get/create package
var packageObj = library.getChildPackage("service");    

(function(packageObj){
	//get used node
	var node_resourceUtility;
	var node_requestServiceProcessor;
	var node_buildServiceProvider;
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_basicUtility;
	var node_createServiceRequestInfoSet;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoSimple;
	var node_OperationContext;
	var node_OperationParm;
	var node_OperationParms;
	var node_DependentServiceRequestInfo;
	var node_expressionUtility;
	var node_dataUtility;
	var node_namingConvensionUtility;
//*******************************************   Start Node Definition  ************************************** 	

var node_createTaskFlowService = function(){
	
	var loc_out = {
		
		getExecuteTaskFlowRequest : function(taskFlow, variables, constants, handlers, requester_parent){
			return loc_getExecuteOperationRequest(dataTypeId, operation, parmsArray, handlers, requester_parent);
		},
			
		executeExecuteTaskFlowRequest : function(taskFlow, variables, constants, handlers, requester_parent){
			var requestInfo = this.getExecuteTaskFlowRequest(taskFlow, variables, constants, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},

	};
	
	loc_out = node_buildServiceProvider(loc_out, "taskFlowService");
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("common.utility.basicUtility", function(){node_basicUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationContext", function(){node_OperationContext = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParm", function(){node_OperationParm = this.getData();});
nosliw.registerSetNodeDataEvent("expression.entity.OperationParms", function(){node_OperationParms = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});
nosliw.registerSetNodeDataEvent("expression.utility", function(){node_expressionUtility = this.getData();});
nosliw.registerSetNodeDataEvent("expression.dataUtility", function(){node_dataUtility = this.getData();});
nosliw.registerSetNodeDataEvent("common.namingconvension.namingConvensionUtility", function(){node_namingConvensionUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createTaskFlowService", node_createTaskFlowService); 
	
})(packageObj);
