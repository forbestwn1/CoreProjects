nosliwApplication.info.application.modulesConfigure = [
	{
		name : "builder",
		initParm : function(env){ 
			return $("#pageDiv").get(0); 
		},
		factory : "application.story.module.builder.createModuleStoryBuilder",
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
