//get/create package
var packageObj = library.getChildPackage("request");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * This type of request is for service that is depend on another service 
 * Another service would do the most job
 * Once another service done its job, this service's handler will be called
 */
var createServiceRequestInfoService = function(service, handlers, requester_parent){
	
	var loc_constructor = function(service, handlers, requester_parent){
		//dependent service
		loc_out.pri_dependentService = undefined;
	};
	
	var loc_buildDependentRequestHandler = function(hanlderName){
			return function(dependentRequestInfo, data){
				var parentHandler = loc_out.getHandlers()[hanlderName];
				var dependentResultProcessor = loc_out.pri_dependentService.processors[hanlderName];
				var parentRequestInfo = dependentRequestInfo.getParentRequest();
				
				var out = undefined;
				if(dependentResultProcessor!=undefined){
					out = dependentResultProcessor.call(dependentRequestInfo, dependentRequestInfo, data);
				}
				if(parentHandler!=undefined){
					out = parentRequestInfo.executeHandler(hanlderName, parentRequestInfo, out);
				}			
				return out;
			};
	};
	
	/*
	 * create handlers for child request
	 */
	var loc_buildDependentRequestHandlers = function(){
		loc_out.pri_dependentService.requestInfo.addPostProcessor({
			start : loc_buildDependentRequestHandler("start"),
			success : loc_buildDependentRequestHandler("success"),
			error : loc_buildDependentRequestHandler("error"),
			exception : loc_buildDependentRequestHandler("exception"),
		});
	};

	/*
	 * process request with child request 
	 */
	var loc_processRequestWithDependentRequest = function(reqeustInfo){
		var dependentRequest = reqeustInfo.getDependentServiceRequestInfo();
		return nosliw.getRequestServiceManager().processRequest(dependentRequest, false);
	};
	
	var loc_out = {
			getDependentServiceRequestInfo : function(){
				var dependentService = this.pri_dependentService;
				if(dependentService!=undefined){
					return dependentService.requestInfo;
				}
			},
			
			/*
			 * when some service need to do sth part of which can be provided by another service
			 * this service can be child service so that it does not need to be implemented again within another service
			 * child service has the its own requestinfo that share the request id and requester with its parent service 
			 */
			setDependentService : function(dependentService){
				this.pri_dependentService = dependentService;
				//set parent request
				this.pri_dependentService.requestInfo.setParentRequest(this);
				
				//create child reqeust handler based on original handlers and processors
				loc_buildDependentRequestHandlers(); 
				
				this.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_processRequestWithDependentRequest, this));
			},
	};

	loc_out = _.extend(nosliwCreateServiceRequestInfoCommon(service, handlers, requester_parent), loc_out);
	
	//request type
	loc_out.setType(NOSLIWCONSTANT.REQUEST_TYPE_SERVICE);
	
	loc_constructor(service, handlers, requester_parent);
	
	return loc_out;
};


//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createServiceRequestInfoService", createServiceRequestInfoService); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
