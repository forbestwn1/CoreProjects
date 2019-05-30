//get/create package
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
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "minApp";

var node_createApplication = function(rootNode){

	var loc_miniAppService;

	var loc_framwork7App;
	
	var loc_vue;
	
	var loc_modulesInfo = [{
		name : "user-apps",
		root : function(){ return $("#userInfoDiv").get(0); },
		factory : "miniapp.module.userapps.createModuleUserApps",
		init : {
			success : function(requestInfo, view){
			}
		}
	}];
	var loc_modules = {};
	
	var loc_userappsModule;
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(rootNode){

		loc_miniAppService = node_createMiniAppService();

		var out = node_createServiceRequestInfoSequence(undefined);
		var mainHtml = "js/miniapp/main.html";
		out.addRequest(node_miniAppUtility.getLoadFilesRequest([mainHtml], {
			success : function(request, mainSource){
				$(mainSource[mainHtml]).appendTo(rootNode);
				
				loc_framwork7App = new Framework7({
					  // App root element
					  root: rootNode,
					  name: 'My App',
					  id: 'com.myapp.test',
					  panel: {
						    swipe: 'both',
					  },				
				});
			
				var initAppModules = node_createServiceRequestInfoSet(new node_ServiceInfo("InitMiniAppModules"), {
					success : function(request){
					}
				}); 
				_.each(loc_modulesInfo, function(moduleInfo, index){
					var module = nosliw.getNodeData(moduleInfo.factory)(moduleInfo.root());
					loc_modules[moduleInfo.name] = module;
					initAppModules.addRequest(moduleInfo.name, module.interfaceObjectLifecycle.initRequest(moduleInfo.init, undefined));
				});
				return initAppModules;
			}
		}));	

		
		return out;
	};

	var loc_refreshRequest = function(userInfo){
		return loc_modules["user-apps"].refreshRequest(userInfo);
	};
	
	var loc_out = {

		getMiniAppService(){  return loc_miniAppService; },

		getLoginRequest(userInfo, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("MiniAppLogin", {"userInf":userInfo}), handlers, requestInfo);
			if(userInfo==undefined){
				userInfo = {};
				var userId = localStorage.userId;
				if(userId!=undefined){
					userInfo.user = {};
					userInfo.user.id = userId;
				}
			}
			out.addRequest(loc_miniAppService.getLoginRequest(userInfo, {
				success : function(requestInfo, userInfo){
					localStorage.userId = userInfo.user.id;
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
		
		getRefreshRequest(userInfo){
			
		},
		
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


//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
