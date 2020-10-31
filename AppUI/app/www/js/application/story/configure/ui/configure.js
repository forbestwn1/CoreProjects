nosliwApplication.info.application.modulesConfigure = [
	{
		name : "ui",
		initParm : function(env){
			return $("#pageDiv").get(0); 
		},
		factory : "application.story.module.ui.createModuleUI",
		init : function(module, env, request){
//			module.registerEventListener(undefined, function(eventName, eventData, request){
//				if(eventName=="selectMiniApp"){
//					env.getModule("mini-app").executeRefreshRequest(eventData);
//				}
//			});
		}
	},
];

var createApplicationConfigure = nosliw.getNodeData("application.common.entity.createApplicationConfigure");
nosliwApplication.info.application.appConfigure = createApplicationConfigure(
		nosliwApplication.info.application.modulesConfigure, 
		nosliwApplication.info.application.configureFolder+"main.html", 
		function(){}, {});
