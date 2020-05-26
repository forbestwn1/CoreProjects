var modulesInfo = [
	{
		name : "group",
		initParm : function(env){ 
			return $("#groupDiv").get(0); 
		},
		factory : "miniapp.module.appgroup.createModuleAppGroup",
		init : function(module, env, request){
			module.registerEventListener(undefined, function(eventName, eventData, request){
				if(eventName=="selectMiniApp"){
					eventData.setGroupId(env.getData("groupId"));
					env.getModule("mini-app").executeRefreshRequest(eventData, undefined, request);
				}
			});
		}
	},
	{
		name : "mini-app",
		initParm : function(env){ 
			return {
				settingName : "application",
				configureParms : {
					mainModuleRoot : $('#miniAppMainDiv').get(),
					settingModuleRoot : $('#miniAppSettingDiv').get(),
					storeService : nosliw.runtime.getStoreService(),
					dataService : nosliw.getNodeData("uiapp.appDataService"),
					framework7App : env.getData("framework7App")
				}
			}
		},
		factory : "miniapp.module.miniapp.createModuleMiniApp",
		init : function(module, env, request){
		}
	}
];

var data = {
};

var createApplicationConfigure = nosliw.getNodeData("miniapp.createApplicationConfigure");
nosliw.createNode("miniapp.configure", createApplicationConfigure(
	modulesInfo, 
	"js/application/configure/group/main.html",
	function(env){
		var createMiniAppInfo = nosliw.getNodeData("miniapp.module.miniapp.createMiniAppInfo");
		var miniAppInfo = createMiniAppInfo(env.getModule("group").getMiniApp(env.getData("app")), env.getData("groupId"));
		env.getModule("mini-app").executeRefreshRequest(miniAppInfo);
	},
	data));
