/**
 * 
 */
{
	type : "start",
	
	processor : "com.nosliw.data.core.process.activity.HAPStartActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPStartActivityDefinition",
	
	script : {
		
		javascript : function(activity, env){
		
			var loc_activity = activity;
		
			var loc_out = {
				
				getExecuteRequest : function(activity, input, handlers, request){
					
				}
			};
			return loc_out;
		}
 
	} 
}
