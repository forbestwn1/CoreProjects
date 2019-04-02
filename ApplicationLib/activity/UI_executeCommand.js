/**
 * 
 */
{
	type : "UI_executeCommand",
	
	processor : "com.nosliw.uiresource.module.activity.HAPExecuteUICommandActivityProcessor",
	
	definition : "com.nosliw.uiresource.module.activity.HAPExecuteUICommandActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoSimple = nosliw.getNodeData("request.request.createServiceRequestInfoSimple");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_IOTaskResult = nosliw.getNodeData("iotask.entity.IOTaskResult");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");

			var loc_out = {
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteUICommandActivity", {}), handlers, request);
					out.addRequest(env.getExecuteComponentCommandRequest(activity.componentId, activity.command, input, {
						success : function(request){
							return new node_IOTaskResult(node_COMMONCONSTANT.ACTIVITY_RESULT_SUCCESS);
						}
					}));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
