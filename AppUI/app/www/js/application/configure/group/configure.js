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
					eventData.groupId = env.getData("groupId");
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
nosliw.createNode("miniapp.configure", createApplicationConfigure(modulesInfo, "js/application/configure/group/main.html", data));
