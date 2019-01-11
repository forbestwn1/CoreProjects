/**
 * 
 */
{
	type : "expression",
	
	processor : "com.nosliw.data.core.process.activity.HAPExpressionActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPExpressionActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var loc_expressionService = nosliw.runtime.getExpressionService(); 
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_createServiceRequestInfoService = nosliw.getNodeData("request.request.createServiceRequestInfoService");
			var node_DependentServiceRequestInfo = nosliw.getNodeData("request.request.entity.DependentServiceRequestInfo");
			var node_NormalActivityResult = nosliw.getNodeData("process.entity.NormalActivityResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");

			var loc_out = {
				
				getExecuteRequest : function(activity, input, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpressionActivity", {"activity":activity, "input":input}), handlers, request);
					
					//execute script expression 
					var scriptExpression = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SCRIPTEXPRESSION]; 
					var expressions = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_EXPRESSIONS];
					var scriptFunction = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SCRIPTEXPRESSIONSCRIPT]; 

					var varNames = scriptExpression[node_COMMONATRIBUTECONSTANT.SCRIPTEXPRESSION_VARIABLENAMES];
					
					 out.addRequest(loc_expressionService.getExecuteScriptRequest(scriptFunction, expressions, input, input, {
						success:function(requestInfo, scriptExpressionOut){
							var activityOutput = {};
							activityOutput[loc_env.buildOutputVarialbeName(node_COMMONCONSTANT.ACTIVITY_OUTPUTVARIABLE_OUTPUT)] = scriptExpressionOut;
							return new node_NormalActivityResult(node_COMMONCONSTANT.ACTIVITY_RESULT_SUCCESS, activityOutput);
						}
					}));
					return out;
				}
			};
			return loc_out;
		}
	} 
}
