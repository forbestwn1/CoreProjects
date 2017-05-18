//get/create package
var packageObj = library.getChildPackage("runtime");    

(function(packageObj){
	//get used node
	var makeObjectWithLifecycleNode = packageObj.requireNode("common.lifecycle.makeObjectWithLifecycle");
	var createIdServiceNode = packageObj.requireNode("common.idservice.createIdService");
	var createLoggingServiceNode = packageObj.requireNode("common.loggingservice.createLoggingService");
	var createResourceServiceNode = packageObj.requireNode("common.resourceservice.createResoruceService");
	var createExpressionServiceNode = packageObj.requireNode("common.resourceservice.createExpressionService");
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

})(packageObj);
