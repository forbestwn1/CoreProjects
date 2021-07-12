/**
 * 
 */
{
	type : "Service_request",
	
	processor : "com.nosliw.data.core.activity.plugin.HAPServiceActivityProcessor",
	
	definition : "com.nosliw.data.core.activity.plugin.HAPServiceActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var loc_expressionService = nosliw.runtime.getExpressionService(); 
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
			var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
			var node_IOTaskResult = nosliw.getNodeData("iovalue.entity.IOTaskResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
			var node_createIODataSet = nosliw.getNodeData("iovalue.entity.createIODataSet");

			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var service = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SERVICE];
//					var provider = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_PROVIDER];

					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteService", {"serviceName":service}), handlers, request);

					out.addRequest(nosliw.runtime.getDataService().getExecuteDataServiceUseRequest(service, node_createIODataSet(input), {
						success : function(request, taskResult){
							var activityOutput = taskResult.resultValue.getData();
							return new node_IOTaskResult(node_COMMONCONSTANT.TASK_RESULT_SUCCESS, activityOutput);
						}
					}));

					return out;
				}
			};
			return loc_out;
		}
	} 
}
