//get/create package
var packageObj = library.getChildPackage("rhino");    

(function(packageObj){
	//get used node
	var node_makeObjectWithLifecycle;
	var node_createIdService;
	var node_createLoggingService;
	var node_createResourceService;
	var node_createExpressionService;
	var node_NOSLIWCONSTANT;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * 
 */
var createRuntime = function(){
	
	var loc_idService;
	
	var loc_loggingService;
	
	var loc_resourceService;
	
	var loc_expressionService;
	
	var loc_out = {
		
		start : function(){
			
		},
		
		getIdService(){
			return loc_idService;
		},
		
		getLoggingService(){
			return loc_loggingService;
		},
		
		getResourceService(){
			return loc_resourceService;
		},
		
		getExpressionService(){
			return loc_expressionService;
		},
			
		
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = node_createIdService();
		loc_loggingService = node_createLoggingService();
		loc_resourceService = node_createResourceService();
		loc_expressionService = node_createExpressionService();
	};
	
	node_makeObjectWithLifecycle(loc_out, lifecycleCallback);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createRuntime", createRuntime); 

var module = {
		start : function(packageObj){
			node_NOSLIWCONSTANT = packageObj.getNodeData("constant.NOSLIWCONSTANT");
			node_makeObjectWithLifecycle = packageObj.getNodeData("common.lifecycle.makeObjectWithLifecycle");
			node_createIdService = packageObj.getNodeData("service.idservice.createIdService");
			node_createLoggingService = packageObj.getNodeData("service.loggingservice.createLoggingService");
			node_createResourceService = packageObj.getNodeData("service.resourceservice.createResourceService");
			node_createExpressionService = packageObj.getNodeData("service.expressionservice.createExpressionService");
		}
};
nosliw.registerModule(module, packageObj);
})(packageObj);
