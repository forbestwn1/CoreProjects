//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_COMMONATRIBUTECONSTANT;
	var node_COMMONCONSTANT;
	var node_buildServiceProvider;
	var node_createServiceRequestInfoSequence;
	var node_ServiceInfo;
	var node_requestServiceProcessor;

//*******************************************   Start Node Definition  ************************************** 	
var node_createDataService = function(){

	var loc_out = {

		getExecuteDataServiceRequest : function(serviceId, parms, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExectueDataService", {"serviceId":serviceId, "parms":parms}), handlers, requestInfo);

			var gatewayParm = {};
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_ID] = serviceId;
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_PARMS] = parms;
			
			out.addRequest(nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(
					node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_SERVICE, 
					node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST, 
					gatewayParm,
					{
						success : function(requestInfo, serviceResult){
							return serviceResult;
						}
					}
			));
			
			return out;
		},
			
		executeExecuteDataServiceRequest : function(serviceId, parms, handlers, requester_parent){
			var requestInfo = this.getExecuteDataServiceRequest(serviceId, parms, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
	};

	loc_out = node_buildServiceProvider(loc_out, "processService");
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataService", node_createDataService); 

})(packageObj);
