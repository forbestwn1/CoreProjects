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
			var loc_expressionService = nosliw.runtime.getExpressionService(); 
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
			var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
			var node_IOTaskResult = nosliw.getNodeData("iovalue.entity.IOTaskResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_objectOperationUtility = nosliw.getNodeData("common.utility.objectOperationUtility");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");

			var loc_out = {
				
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteSwitchActivity", {"activity":activity, "input":input}), handlers, request);
					
					//execute script expression 
					var scriptExpression = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SCRIPTEXPRESSION]; 
					var expressions = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_EXPRESSIONS];
					var scriptFunction = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SCRIPTEXPRESSIONSCRIPT]; 

					var varNames = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_VARIABLENAMES];
					var varInputs = {};
					_.each(varNames, function(varName, index){
						varInputs[varName] = node_objectOperationUtility.getObjectAttributeByPath(input, varName);
					});

					 out.addRequest(loc_expressionService.getExecuteScriptRequest(scriptFunction, expressions, varInputs, input, {
						success:function(requestInfo, scriptExpressionOut){
							var activityOutput = {};
							activityOutput[env.buildOutputVarialbeName(node_COMMONCONSTANT.ACTIVITY_OUTPUTVARIABLE_OUTPUT)] = scriptExpressionOut;
							return new node_IOTaskResult(node_COMMONCONSTANT.ACTIVITY_RESULT_SUCCESS, activityOutput);
						}
					}));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
