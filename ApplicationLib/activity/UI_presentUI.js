/**
 * 
 */
{
	type : "UI_presentUI",
	
	processor : "com.nosliw.uiresource.module.activity.HAPPresentUIActivityProcessor",
	
	definition : "com.nosliw.uiresource.module.activity.HAPPresentUIActivityDefinition",
	
	script : {
		
		javascript : function(nosliw, env){
			var loc_env = env;
			var node_COMMONATRIBUTECONSTANT = nosliw.getNodeData("constant.COMMONATRIBUTECONSTANT");
			var node_COMMONCONSTANT = nosliw.getNodeData("constant.COMMONCONSTANT");
			var node_IOTaskResult = nosliw.getNodeData("iovalue.entity.IOTaskResult");
			var node_createServiceRequestInfoSequence = nosliw.getNodeData("request.request.createServiceRequestInfoSequence");
			var node_ServiceInfo = nosliw.getNodeData("common.service.ServiceInfo");
			var node_createSettingConfigure = nosliw.getNodeData("common.setting.createSettingConfigure");

			var loc_settingConfigure = node_createSettingConfigure({
				updateData : {
					defaultValue : true
				}
			});
			
			var loc_out = {
				getExecuteActivityRequest : function(activity, input, env, handlers, request){
					var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("PresentUIActivity", {}), handlers, request);
					var presentUISetting = activity[node_COMMONATRIBUTECONSTANT.EXECUTABLEACTIVITY_SETTING];
					out.addRequest(env.getPresentUIRequest(activity.ui, loc_settingConfigure.createSetting(presentUISetting), {
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
