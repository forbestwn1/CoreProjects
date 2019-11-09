/**
 * 
 */
{
	type : "debug",
	
	processor : "com.nosliw.data.core.process.activity.HAPDebugActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPDebugActivityDefinition",
	
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
			var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
			var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
			var node_dataOperationUtility = nosliw.getNodeData("common.utility.dataOperationUtility");
			var node_createIODataSet = nosliw.getNodeData("iotask.entity.createIODataSet");
			
			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					console.log(JSON.stringify(input));

					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteLoopActivity", undefined), handlers, request);
					out.addRequest(node_createServiceRequestInfoSimple(undefined, 
						function(request){
							return new node_IOTaskResult(node_COMMONCONSTANT.ACTIVITY_RESULT_SUCCESS, undefined);
						}
					));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
