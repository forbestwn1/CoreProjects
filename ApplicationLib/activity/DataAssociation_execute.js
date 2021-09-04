/**
 * 
 */
{
	type : "DataAssociation_execute",
	
	processor : "com.nosliw.data.core.activity.plugin.HAPDataAssociationActivityProcessor",
	
	definition : "com.nosliw.data.core.activity.plugin.HAPDataAssociationActivityDefinition",
	
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
			var node_createDataAssociation = nosliw.getNodeData("iovalue.createDataAssociation");

			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
					
					var path = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_PATH];
					var dataAssociation = env.getChildElement(path);
					out.addRequest(dataAssociation.getExecuteRequest({
						success : function(request, outputIO){
							return outputIO.getGetDataValueRequest(undefined, {
								success : function(request, outputValue){
									return new node_IOTaskResult(node_COMMONCONSTANT.TASK_RESULT_SUCCESS);
								}
							});
						}
					}));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
