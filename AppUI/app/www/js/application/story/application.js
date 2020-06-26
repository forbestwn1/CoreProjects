//get/create package
var packageObj = library.getChildPackage();    

(function(packageObj){
	//get used node
	var node_CONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_ServiceInfo;
	var node_requestServiceProcessor;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoSet;
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_applicationUtility;
	var node_createStoryService;
	var node_storyUtility;
//*******************************************   Start Node Definition  ************************************** 	

var loc_mduleName = "minApp";

var node_createApplication = function(){

	var loc_storyService = node_createStoryService();

//	var loc_loginService = node_createLoginService(loc_miniAppService);
	
	var loc_appConfigure;
	
	var loc_modules = {};
	
	var loc_design = {};
	
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
		out.addRequest(node_applicationUtility.getLoadFilesRequest([loc_appConfigure.getLayout()], {
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

	var loc_initRequest = function(handlers, request){
		loc_appConfigure = nosliwApplication.info.application.appConfigure;
		
		//set web page title
		var configureData = loc_appConfigure.getData();

		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(node_applicationUtility.getLoadFilesRequest([loc_appConfigure.getLayout()], {
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
	
	var loc_refreshRequest = function(design, handlers, request){
		var out = node_createServiceRequestInfoSequence(undefined, handlers, request);
		out.addRequest(loc_storyService.getNewDesignRequest(undefined, "page_minimum", undefined, {
			success : function(request, design){
				loc_design = design;
				var story = loc_design[node_COMMONATRIBUTECONSTANT.NEWDESIGN_STORY];
				var pageModule = loc_modules["page"];
				var uiModule = loc_modules["ui"];
				var overviewModule = loc_modules["overview"];
				if(pageModule!=undefined){
					var pageTree = node_storyUtility.buildPageTree(story);
					return pageModule.refreshRequest(pageTree);
				}
				if(uiModule!=undefined){
					return uiModule.refreshRequest("23", story);
				}
				if(overviewModule!=undefined){
					return overviewModule.refreshRequest(story);
				}
			}
		}));


//		_.each(loc_modules, function(module, name){
//			if(module.refreshRequest!=undefined)  out.addRequest(module.refreshRequest(loc_design));
//		});
		
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
			out.addRequest(loc_initRequest());
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
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();	});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){	node_createServiceRequestInfoSequence = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){	node_createServiceRequestInfoSimple = this.getData();	});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSet", function(){node_createServiceRequestInfoSet = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("application.common.utility", function(){node_applicationUtility = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.service.createStoryService", function(){node_createStoryService = this.getData();});
nosliw.registerSetNodeDataEvent("application.instance.story.utility", function(){node_storyUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createApplication", node_createApplication); 

})(packageObj);
