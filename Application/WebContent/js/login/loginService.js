/**
 * 
 */

var nosliwCreateLoginService = function(){

	var loc_moduleName = "login";
	
	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	/*
	 * get requester according to req
	 * 		undefined : use default one
	 */
	loc_getRequester = function(req){
		if(req==undefined)   return loc_requester;
		else return req;
	};
	
	/*
	 * get request service object for Login 
	 */
	loc_getRequestServiceLogin = function(loginInfo){
		return new NosliwServiceInfo(NOSLIWCOMMONCONSTANT.CONS_SERVICENAME_LOGIN, {"info":loginInfo}); 
	};
	
	loc_requestInfoLogin = function(requestInfo){
		var task = nosliwRequestUtility.getRemoteServiceTask(loc_moduleName, requestInfo);
		return task;
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
		var remoteServiceMan = nosliw.getRemoteServiceManager();
		remoteServiceMan.registerSyncTaskConfigure(loc_moduleName, nosliwRemoteServiceUtility.createRemoteServiceConfigures(NOSLIWCOMMONCONSTANT.CONS_SERVICENAME_LOGIN, NOSLIWCOMMONCONSTANT.CONS_SERVICENAME_LOGIN)); 
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){
	};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		getRequestInfoLogin : function(loginInfo, handlers, requester){
			var reqInfo = nosliwCreateServiceRequestInfoService(loc_getRequestServiceLogin(loginInfo), handlers, loc_getRequester(requester));
			reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_requestInfoLogin, this));
			return reqInfo;
		},	
		
		requestLogin : function(loginInfo, handlers, requester, processRemote){
			var requestInfo = this.getRequestInfoLogin(loginInfo, handlers, requester);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, processRemote);
		},
		
	};
	
	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	
	return loc_out;
};

