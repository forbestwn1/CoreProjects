/**
 * 
 */
{
	type : "expression",
	
	processor : "com.nosliw.data.core.process.activity.HAPExpressionActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPExpressionActivityDefinition",
	
	script : {
		
		javascript : function(activity, env){
		
			var loc_activity = activity;
		
			var loc_out = {
				
				getExecuteRequest : function(input, handlers, request){
					
				}
			};
			return loc_out;
		}
 
	} 
}
