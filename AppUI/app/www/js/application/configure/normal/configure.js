var modulesInfo = [
	{
		name : "user-apps",
		initParm : function(env){ 
			return $("#userInfoDiv").get(0); 
		},
		factory : "miniapp.module.userapps.createModuleUserApps",
		init : function(module, env, request){
			module.registerEventListener(undefined, function(eventName, eventData, request){
				if(eventName=="selectMiniApp"){
					env.getModule("mini-app").executeRefreshRequest(eventData);
				}
			});
		}
	},
	{
		name : "mini-app",
		initParm : function(env){ 
			return {
				main : $("#miniAppMainDiv").get(0),
				setting : $("#miniAppSettingDiv").get(0),
				framework7App : env.getData("framework7App")
			}; 
		},
		factory : "miniapp.module.miniapp.createModuleMiniApp",
		init : function(module, env, request){
		}
	}
];

var data = {
};

var createApplicationConfigure = nosliw.getNodeData("miniapp.createApplicationConfigure");
nosliw.createNode("miniapp.configure", createApplicationConfigure(modulesInfo, "js/application/configure/normal/main.html", data));
