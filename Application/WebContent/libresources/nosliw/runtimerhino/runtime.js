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
	var node_NOSLIWCOMMONCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

	var loc_mduleName = "runtime";
	
/**
 * 
 */
var createRuntime = function(){
	
	var loc_idService;
	
	var loc_resourceService;
	
	var loc_resourceManager;
	
	var loc_expressionService;
	
	var loc_out = {
		
		start : function(){
			
		},
		
		getIdService(){
			return loc_idService;
		},
		
		getResourceManager(){
			return loc_resourceManager;
		},
		
		getResourceService(){
			return loc_resourceService;
		},
		
		getExpressionService(){
			return loc_expressionService;
		},
			
		getName(){
			return node_NOSLIWCOMMONCONSTANT.RUNTIME_ENVIRONMENT_RHINO;
		}
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = node_createIdService();
		loc_resourceManager = node_createResourceManager();
		loc_resourceService = node_createResourceService(loc_resourceManager);
		loc_expressionService = node_createExpressionService();
		return true;
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	node_makeObjectWithName(loc_out, loc_mduleName);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createRuntime", createRuntime); 

var module = {
		start : function(packageObj){
			node_CONSTANT = packageObj.getNodeData("constant.NOSLIWCONSTANT");
			node_NOSLIWCOMMONCONSTANT = packageObj.getNodeData("constant.NOSLIWCOMMONCONSTANT");
			node_makeObjectWithName = packageObj.getNodeData("common.objectwithname.makeObjectWithName");
			node_makeObjectWithLifecycle = packageObj.getNodeData("common.lifecycle.makeObjectWithLifecycle");
			node_createIdService = packageObj.getNodeData("service.idservice.createIdService");
			node_createResourceManager = packageObj.getNodeData("service.resourcemanager.createResourceManager");
			node_createResourceService = packageObj.getNodeData("runtime.rhino.createResourceService");
			node_createExpressionService = packageObj.getNodeData("service.expressionservice.createExpressionService");
		}
};
nosliw.registerModule(module, packageObj);
})(packageObj);
