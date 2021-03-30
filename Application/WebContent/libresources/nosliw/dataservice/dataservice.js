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
	var node_IOTaskResult;
	var node_taskUtility;
	var node_IOTaskInfo;
	
//*******************************************   Start Node Definition  ************************************** 	
var node_createDataService = function(){

	var loc_out = {

		getExecuteDataServiceUseRequest : function(serviceUse, ioEndpoint, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {}), handlers, requester_parent);
			var serviceMapping = serviceUse[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_SERVICEUSE];
			var serviceId = serviceUse[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_PROVIDERID];
			out.addRequest(node_taskUtility.getExecuteWrappedTaskRequest(
				ioEndpoint, undefined, serviceMapping,
				new node_IOTaskInfo(function(input, handlers, request){
					var serviceRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
					serviceRequest.addRequest(loc_out.getExecuteDataServiceRequest(serviceId, input, {
						success : function(request, serviceResult){
							return new node_IOTaskResult(serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTNAME], serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTVALUE]);
						}
					}));
					return serviceRequest;
				})
			));
			return out;
			
		},
			
			
		getExecuteEmbededDataServiceByNameRequest : function(serviceName, serviceProviders, serviceUse, ioEndpoint, handlers, requester_parent){
			var serviceProvider = serviceProviders[serviceName];
			return loc_out.getExecuteEmbededDataServiceByProviderRequest(serviceProvider, serviceUse, ioEndpoint, handlers, requester_parent);
		},
			
		getExecuteEmbededDataServiceByProviderRequest : function(serviceProvider, serviceUse, ioEndpoint, handlers, requester_parent){
			return loc_out.getExecuteEmbededDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], serviceUse, ioEndpoint, handlers, requester_parent);
		},			
			
		getExecuteEmbededDataServiceRequest : function(serviceId, serviceUse, ioEndpoint, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {}), handlers, requester_parent);
			var serviceMapping = serviceUse[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_SERVICEMAPPING];
			out.addRequest(node_taskUtility.getExecuteWrappedTaskRequest(
				ioEndpoint, undefined, serviceMapping,
				new node_IOTaskInfo(function(input, handlers, request){
					var serviceRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
					serviceRequest.addRequest(loc_out.getExecuteDataServiceRequest(serviceId, input, {
						success : function(request, serviceResult){
							return new node_IOTaskResult(serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTNAME], serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTVALUE]);
						}
					}));
					return serviceRequest;
				})
			));
			return out;
		},
		
		getExecuteDataServiceByNameRequest : function(serviceName, serviceProviders, parms, handlers, requester_parent){
			var serviceProvider = serviceProviders[serviceName];
			return this.getExecuteDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], parms, handlers, requester_parent);
		},
			
		getExecuteDataServiceByProviderRequest : function(serviceProvider, parms, handlers, requester_parent){
			return this.getExecuteDataServiceRequest(serviceProvider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID], parms, handlers, requester_parent);
		},
		
		//directly invoke data service
		getExecuteDataServiceRequest : function(serviceId, parms, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExectueDataService", {"serviceId":serviceId, "parms":parms}), handlers, requestInfo);

			var gatewayParm = {};
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_QUERY] = {}
			gatewayParm[node_COMMONATRIBUTECONSTANT.GATEWAYSERVICE_COMMAND_REQUEST_QUERY][node_COMMONATRIBUTECONSTANT.QUERYSERVICE_SERVICEID] = serviceId;
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
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskResult", function(){node_IOTaskResult = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.taskUtility", function(){node_taskUtility = this.getData();});
nosliw.registerSetNodeDataEvent("iotask.entity.IOTaskInfo", function(){node_IOTaskInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createDataService", node_createDataService); 

})(packageObj);
