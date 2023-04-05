//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_makeObjectWithName;
	var node_makeObjectWithLifecycle;
	var node_createIdService;
	var node_createLoggingService;
	var node_createResourceManager;
	var node_createResourceService;
	var node_createExpressionService;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_createRemoteService;
	var node_createGatewayService;
	var node_createMiniAppService;
	var node_createTaskRuntimeFactory;
	var node_createDataService;
	var node_createUIPageService;
	var node_createUIModuleService;
	var node_createUIAppService;
	var node_createVariableManager;
	var node_createRequestServiceProcessor;
	var node_createErrorManager;
	var node_createStoreService;
	var node_createComplexEntityRuntimeService;
//*******************************************   Start Node Definition  ************************************** 	

	var loc_mduleName = "runtime";
	
/**
 * 
 */

var node_createRuntime = function(name){
	
	var loc_name = name;
	
	var loc_idService;
	
	var loc_resourceService;
	
	var loc_resourceManager;
	
	var loc_expressionService;
	
	var loc_gatewayService;

	var loc_taskRuntimeFactory;
	
	var loc_remoteService;
	
	var loc_dataService;
	
	var loc_uiPageService;

	var loc_uiVariableManager;
	
	var loc_uiModuleService;

	var loc_uiAppService;

	var loc_requestProcessor;
	
	var loc_securityService;
	
	var loc_errorManager;
	
	var loc_storeService;
	
	var loc_complexEntityService;
	
	var loc_out = {
		
		start : function(){	},
		
		getName(){			return loc_name;		},
		
		getIdService(){		return loc_idService;		},
		
		getResourceService(){			return loc_resourceService;		},
		
		getExpressionService(){			return loc_expressionService;		},
			
		getGatewayService(){		return loc_gatewayService;		},
		
		getRemoteService(){			return loc_remoteService;		},

		getDataService(){   return loc_dataService;   },

		getTaskRuntimeFactory(){   return loc_taskRuntimeFactory;  },
		
		getUIPageService(){		return loc_uiPageService;		},
		
		getUIVariableManager(){   return loc_uiVariableManager;   },

		getUIModuleService(){   return loc_uiModuleService; },

		getUIAppService(){   return loc_uiAppService; },

		getRequestProcessor(){   return  loc_requestProcessor;  },

		getSecurityService(){  return  loc_securityService;  },
		
		getErrorManager(){   return loc_errorManager;  },
		
		getStoreService(){   return loc_storeService;   },
		
		getComplexEntityService(){    return loc_complexEntityService;    }
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = node_createIdService();
		loc_resourceManager = node_createResourceManager();
		loc_resourceService = node_createResourceService(loc_resourceManager);
		loc_expressionService = node_createExpressionService();
		if(node_createRemoteService!=undefined)		loc_remoteService = node_createRemoteService();
		loc_remoteService.interfaceObjectLifecycle.init();
		loc_gatewayService = node_createGatewayService();
		loc_taskRuntimeFactory = node_createTaskRuntimeFactory();
		if(node_createUIPageService!=undefined)  loc_uiPageService = node_createUIPageService();
		loc_dataService = node_createDataService();
		loc_securityService = node_createSecurityService();
		
		loc_uiModuleService = node_createUIModuleService();
		loc_uiAppService = node_createUIAppService();
		loc_uiVariableManager = node_createVariableManager();
		loc_errorManager = node_createErrorManager();
		loc_storeService = node_createStoreService();
		loc_complexEntityService = node_createComplexEntityRuntimeService();
		
		loc_requestProcessor = node_createRequestServiceProcessor();
		nosliw.createNode("request.requestServiceProcessor", loc_requestProcessor); 
		
		//set sortcut for object
		 nosliw.runtime = loc_out;
		 nosliw.generateId = loc_out.getIdService().generateId;
		 
		 //create node for runtime object
		 nosliw.createNode("runtime", loc_out);
		 nosliw.triggerNodeEvent("runtime", "active");

		return true;
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("common.objectwithname.makeObjectWithName", function(){node_makeObjectWithName = this.getData();});
nosliw.registerSetNodeDataEvent("common.lifecycle.makeObjectWithLifecycle", function(){node_makeObjectWithLifecycle = this.getData();});
nosliw.registerSetNodeDataEvent("service.idservice.createIdService", function(){node_createIdService = this.getData();});
nosliw.registerSetNodeDataEvent("resource.createResourceManager", function(){node_createResourceManager = this.getData();});
nosliw.registerSetNodeDataEvent("expression.service.createExpressionService", function(){node_createExpressionService = this.getData();});
nosliw.registerSetNodeDataEvent("resource.createResourceService", function(){node_createResourceService = this.getData();});
nosliw.registerSetNodeDataEvent("remote.createRemoteService", function(){node_createRemoteService = this.getData();});
nosliw.registerSetNodeDataEvent("runtime.createGatewayService", function(){node_createGatewayService = this.getData();});
nosliw.registerSetNodeDataEvent("miniapp.service.createMiniAppService", function(){node_createMiniAppService = this.getData();});
nosliw.registerSetNodeDataEvent("task.createTaskRuntimeFactory", function(){node_createTaskRuntimeFactory = this.getData();});
nosliw.registerSetNodeDataEvent("dataservice.createDataService", function(){node_createDataService = this.getData();});
nosliw.registerSetNodeDataEvent("uipage.createUIPageService", function(){node_createUIPageService = this.getData();});
nosliw.registerSetNodeDataEvent("uimodule.service.createUIModuleService", function(){node_createUIModuleService = this.getData();});
nosliw.registerSetNodeDataEvent("uiapp.service.createUIAppService", function(){node_createUIAppService = this.getData();});
nosliw.registerSetNodeDataEvent("uidata.variable.createVariableManager", function(){node_createVariableManager = this.getData();});
nosliw.registerSetNodeDataEvent("request.createRequestServiceProcessor", function(){ node_createRequestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("security.createSecurityService", function(){ node_createSecurityService = this.getData();});
nosliw.registerSetNodeDataEvent("error.createErrorManager", function(){ node_createErrorManager = this.getData();});
nosliw.registerSetNodeDataEvent("common.createStoreService", function(){ node_createStoreService = this.getData();});
nosliw.registerSetNodeDataEvent("complexentity.createComplexEntityRuntimeService", function(){ node_createComplexEntityRuntimeService = this.getData();});

//Register Node by Name
packageObj.createChildNode("createRuntime", node_createRuntime); 

})(packageObj);
