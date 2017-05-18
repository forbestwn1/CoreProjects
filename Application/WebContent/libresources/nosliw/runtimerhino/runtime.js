//get/create package
var packageObj = library.getChildPackage("rhino");    

nosliw.registerModule((function(packageObj){
	//get used node
	var loc_makeObjectWithLifecycle;
	var loc_createIdService;
	var loc_createLoggingService;
	var loc_createResourceService;
	var loc_createExpressionService;
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
			return loc_resoruceService;
		},
		
		getExpressionService(){
			return loc_expressionService;
		},
			
		
	};
	
	var lifecycleCallback = {};
	lifecycleCallback[NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT] = function(){
		loc_idService = createIdServiceNode.getData()();
		loc_loggingService = createLoggingService.getData()();
		loc_resourceService = createResourceService.getData()();
		loc_expressionService = createExpressionService.getData()();
	};
	
	makeObjectWithLifecycleNode.getData()(loc_out, lifecycleCallback);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createRuntime", createRuntime); 

return {
	start : function(packageObj){
		loc_makeObjectWithLifecycle = packageObj.getNodeData("common.lifecycle.makeObjectWithLifecycle");
		loc_createIdService = packageObj.getNodeData("common.idservice.createIdService");
		loc_createLoggingService = packageObj.getNodeData("common.loggingservice.createLoggingService");
		loc_createResourceService = packageObj.getNodeData("common.resourceservice.createResoruceService");
		loc_createExpressionService = packageObj.getNodeData("common.resourceservice.createExpressionService");
	}
}

})(packageObj), packageObj);
