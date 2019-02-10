/**
 * 
 */
{
	type : "Service_request",
	
	processor : "com.nosliw.data.core.process.activity.HAPServiceActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPServiceActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var loc_expressionService = nosliw.runtime.getExpressionService(); 
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
			var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
			var node_IOTaskResult = nosliw.getNodeData("iotask.entity.IOTaskResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");

			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {"serviceName":serviceName}), handlers, request);

					var service = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SERVICE];
					var provider = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_PROVIDER];
					var serviceId = provider[node_COMMONATRIBUTECONSTANT.DEFINITIONSERVICEPROVIDER_SERVICEID];;
					
					var output = {};
					return node_ioTaskUtility.getExecuteIOTaskRequest(
							input, 
							service[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_PARMMAPPING], 
							function(input, handlers, request){
								var serviceRequest = node_createServiceRequestInfoSequence(new node_ServiceInfo("", {}), handlers, request);
								serviceRequest.addRequest(nosliw.runtime.getDataService().getExecuteDataServiceRequest(serviceId, input, {
									success : function(request, serviceResult){
										return new node_IOTaskResult(serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_RESULTNAME], serviceResult[node_COMMONATRIBUTECONSTANT.RESULTSERVICE_OUTPUT]);
									}
								}));
								return serviceRequest;
							}, 
							service[node_COMMONATRIBUTECONSTANT.EXECUTABLESERVICEUSE_RESULTMAPPING],
							output, 
							{
								success : function(request, taskResult){
									return loc_context.getUpdateContextRequest(taskResult.resultValue);
								}
							}); 
					
					return out;
				}
			};
			return loc_out;
		}
	} 
}
