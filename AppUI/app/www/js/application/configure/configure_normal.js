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
nosliw.createNode("miniapp.configure", createApplicationConfigure(modulesInfo, "js/application/main.html", data));



/*
get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSet;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createMiniAppService;
	var node_createModuleUserApps;
	var node_miniAppUtility;
	var node_createLoginService;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "minApp";

var node_createApplicationConfigure = function(modulesConfigure, layout, data){
	var loc_modulesConfigure = modulesConfigure;
	var loc_layout = layout;
	var loc_data = data;
	
	var loc_out = {
			
	};
	return loc_out;
};

var node_createApplication = function(rootNode, framework7App){

	var loc_miniAppService = node_createMiniAppService();

	var loc_loginService = node_createLoginService(loc_miniAppService);
	
	var loc_framework7App;
	
	var loc_vue;
	
	var loc_modulesInfo = [
		{
			name : "user-apps",
			initParm : function(env){ 
				return $("#userInfoDiv").get(0); 
			},
			factory : "miniapp.module.userapps.createModuleUserApps",
			init : function(module, env, request){
				module.registerEventListener(undefined, function(eventName, eventData, request){
					if(eventName=="selectMiniApp"){
						loc_modules["mini-app"].executeRefreshRequest(eventData);
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
					framework7App : loc_framework7App
				}; 
			},
			factory : "miniapp.module.miniapp.createModuleMiniApp",
			init : function(module, env, request){
			}
		}
	];
	var loc_modules = {};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(rootNode, framework7App, handlers, request){

		loc_framework7App = framework7App;
		
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		var mainHtml = "js/application/main.html";
		out.addRequest(node_miniAppUtility.getLoadFilesRequest([mainHtml], {
			success : function(request, mainSource){
				$(mainSource[mainHtml]).appendTo(rootNode);
				
				if(loc_framework7App==undefined){
					loc_framework7App = new Framework7({
						  // App root element
						  root: $("#appDiv").get(),
						  name: 'My App',
						  id: 'com.myapp.test',
						  panel: {
							    swipe: 'both',
						  },				
					});
				}

				_.each(loc_modulesInfo, function(moduleInfo, index){
					var module = nosliw.getNodeData(moduleInfo.factory)(moduleInfo.initParm());
					loc_modules[moduleInfo.name] = module;
					module.interfaceObjectLifecycle.init();
					moduleInfo.init(module, request);
					
				});
			}
		}));	

		
		return out;
	};

	var loc_refreshRequest = function(userInfo, handlers, request){
		return loc_modules["user-apps"].refreshRequest(userInfo, handlers, request);
	};
	
	var loc_out = {
		getMiniAppService(){  return loc_miniAppService; },

		getLoginRequest(userInfo, handlers, request){  
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_loginService.getLoginRequest(userInfo, {
				success : function(request, userInfo){
					return loc_refreshRequest(userInfo, {
						success : function(requestInfo){
							return userInfo;
						}
					});
				}
			}));
			return out;
		},
		executeLoginRequest(userInfo, handlers, requestInfo){	node_requestServiceProcessor.processRequest(this.getLoginRequest(userInfo, handlers, requestInfo));	},
		
		getRefreshRequest(userInfo){},
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.createMiniAppService", function(){node_createMiniAppService = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createModuleUserApps", function(){node_createModuleUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.utility", function(){node_miniAppUtility = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.createLoginService", function(){node_createLoginService = this.getData();});


//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
*/
