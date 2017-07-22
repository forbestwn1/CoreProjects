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
	var node_runtimeGateway;
	var node_createRemoteService;
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
	
	var loc_remoteService;
	
	var loc_out = {
		
		start : function(){
			
		},
		
		getIdService(){
			return loc_idService;
		},
		
		getResourceService(){
			return loc_resourceService;
		},
		
		getExpressionService(){
			return loc_expressionService;
		},
			
		getName(){
			return loc_name;
		},
		
		getGateway(){
			return node_runtimeGateway;
		},
		
		getRemoteService(){
			return loc_remoteService;
		}
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[node_CONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = node_createIdService();
		loc_resourceManager = node_createResourceManager();
		loc_resourceService = node_createResourceService(loc_resourceManager);
		loc_expressionService = node_createExpressionService();
		loc_remoteService = node_createRemoteService();

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
//Register Node by Name
packageObj.createChildNode("createRuntime", node_createRuntime); 

var module = {
		start : function(packageObj){
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_makeObjectWithName = packageObj.getNodeData("common.objectwithname.makeObjectWithName");
			node_makeObjectWithLifecycle = packageObj.getNodeData("common.lifecycle.makeObjectWithLifecycle");
			node_createIdService = packageObj.getNodeData("service.idservice.createIdService");
			node_createResourceManager = packageObj.getNodeData("resource.createResourceManager");
			node_createExpressionService = packageObj.getNodeData("expression.service.createExpressionService");
			node_createResourceService = packageObj.getNodeData("resource.createResourceService");
			node_createRemoteService = packageObj.getNodeData("remote.createRemoteService");
			
			node_runtimeGateway = packageObj.getNodeData(node_COMMONCONSTANT.RUNTIME_LANGUAGE_JS_GATEWAY);
		}
};
nosliw.registerModule(module, packageObj);
})(packageObj);
