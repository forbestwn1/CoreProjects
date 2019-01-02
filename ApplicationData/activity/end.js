/**
 * 
 */
{
	type : "end",
	
	processor : "com.nosliw.data.core.process.activity.HAPEndActivityProcessor",
	
	definition : "com.nosliw.data.core.process.activity.HAPEndActivityDefinition",
	
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
