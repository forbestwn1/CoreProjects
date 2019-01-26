/**
 * 
 */
{
	type : "UI_executeUICommand",
	
	processor : "com.nosliw.uiresource.module.activity.HAPExecuteUICommandActivityProcessor",
	
	definition : "com.nosliw.uiresource.module.activity.HAPExecuteUICommandActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			
			var loc_out = {
					
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					
					
					
					
					
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("ExecuteExpressionActivity", {"activity":activity, "input":input}), handlers, request);
					
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
