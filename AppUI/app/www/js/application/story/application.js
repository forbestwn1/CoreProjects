//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSet;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_applicationUtility;
	var node_createMiniAppService;
	var node_createModuleUserApps;
	var node_createLoginService;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "minApp";

var node_createApplication = function(){

	var loc_storyService = node_createStoryService();

	var loc_loginService = node_createLoginService(loc_miniAppService);
	
	var loc_appConfigure;
	
	var loc_modules = {};
	
	var loc_env = {
		getModule : function(name){  return loc_modules[name];   },
		getData : function(name){   return loc_appConfigure.getData()[name];  }
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(handlers, request){
		loc_appConfigure = nosliwApplication.info.application.appConfigure;
		
		//set web page title
		var configureData = loc_appConfigure.getData();

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_miniAppUtility.getLoadFilesRequest([loc_appConfigure.getLayout()], {
			success : function(request, mainSource){
				$(mainSource[loc_appConfigure.getLayout()]).appendTo(nosliwApplication.info.application.rootNode);
				_.each(loc_appConfigure.getModulesConfigure(), function(moduleInfo, index){
					var module = nosliw.getNodeData(moduleInfo.factory)(moduleInfo.initParm(loc_env));
					loc_modules[moduleInfo.name] = module;
					module.interfaceObjectLifecycle.init();
					moduleInfo.init(module, loc_env, request);
				});
			}
		}));	

		return out;
	};

	var loc_refreshRequest = function(userInfo, configureData, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		_.each(loc_modules, function(module, name){
			if(module.refreshRequest!=undefined)  out.addRequest(module.refreshRequest(userInfo, configureData));
		});
		
		out.addRequest(node_createServiceRequestInfoSimple(undefined, function(request){
			loc_appConfigure.getInitFun()(loc_env);
		}));
		
		out.addRequest(node_createStoryService(getNewDesignRequest(undefined, page_minimum, undefined)));
		
		return out;
	};
	
	var loc_out = {
		getMiniAppService(){  return loc_miniAppService; },

		getLoginRequest(userInfo, handlers, request){  
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(loc_loginService.getLoginRequest(userInfo, {
				success : function(request, userInfo){
					return loc_refreshRequest(userInfo, loc_appConfigure.getData(), {
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
		
		getStartRequest(handlers, request){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
			out.addRequest(this.interfaceObjectLifecycle.initRequest());
			out.addRequest(loc_refreshRequest(loc_appConfigure.getData()));
			return out;
		},
		
		executeStartRequest(handlers, request){
			node_requestServiceProcessor.processRequest(this.getStartRequest(handlers, request));
		},
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};	
	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("application.utility", function(){node_applicationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.createMiniAppService", function(){node_createMiniAppService = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.module.userapps.createModuleUserApps", function(){node_createModuleUserApps = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.createLoginService", function(){node_createLoginService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
