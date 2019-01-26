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
					env.executeUICommand(activity.ui, activity.command, input);
				}
			};
			return loc_out;
			
		}
	} 
}
