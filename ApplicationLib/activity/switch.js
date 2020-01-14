/**
 * 
 */
{
	type : "switch",
	
	processor : "com.nosliw.data.core.process.activity.HAPSwitchActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPSwitchActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");

			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteEmbededProcessActivity"), handlers, request);
					out.addRequest(env.getExecuteProcessRequest(activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_PROCESS], input, undefined, {
						success : function(request, processResult){
							return processResult;
						}
					}));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
