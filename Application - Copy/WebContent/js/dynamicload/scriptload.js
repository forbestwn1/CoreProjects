/**
 * 
 */
var nosliwCreateLoadScriptService = function(){
	//sync task name for remote call 
	var loc_moduleName = "loadScript";

	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	var loc_loadedScripts = {};
	
	/*
	 * get requester according to req
	 * 		undefined : use default one
	 */
	var loc_getRequesterParent = function(req){
		if(req==undefined)   return loc_requester;
		else return req;
	};

	var loc_getRequestServiceLoadScript = function(type, info){
		return new NosliwServiceInfo("loadScript", {"type":type, "info":JSON.stringify(info)}); 
	};
	
	var loc_loadScript = function(requestInfo){
		var reqInfo = requestInfo;
		var scriptType = requestInfo.getParmData('type');
		var scriptInfo = encodeURI(requestInfo.getParmData('info'));
		
		var url = "http://localhost:8080/Application/loadScript?type="+scriptType+"&info="+scriptInfo;
		
		  var script = document.createElement('script');
		  script.setAttribute('src', url);
		  script.setAttribute('type', 'text/javascript');

		  var loadFunction = function () {
			  reqInfo.executeSuccessHandler(undefined, this);
		  };
		  script.onload = loadFunction;
		  script.onreadystatechange = loadFunction;
		  document.getElementsByTagName("head")[0].appendChild(script);
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};
	
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},
		
		getRequestInfoLoadUIResourceScript : function(name, handlers, requester_parent){
			var reqInfo = nosliwCreateServiceRequestInfoService(loc_getRequestServiceLoadScript(NOSLIWCOMMONCONSTANT.CONS_SCRIPTTYPE_UIRESOURCE, {"name":name}), handlers, loc_getRequesterParent(requester_parent));
			reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_loadScript, this));
			return reqInfo;
		},	
		
		requestLoadUIResourceScript : function(name, handlers, requester_parent){
			var requestInfo = this.getRequestInfoLoadUIResourceScript(name, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true);
		},
		
		getRequestInfoLoadDataTypeOperationScript : function(dataTypeInfoArray, handlers, requester_parent){
			var reqInfo = nosliwCreateServiceRequestInfoService(loc_getRequestServiceLoadScript(NOSLIWCOMMONCONSTANT.CONS_SCRIPTTYPE_DATAOPERATIONS, {"requestArray":dataTypeInfoArray}), handlers, loc_getRequesterParent(requester_parent));
			reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_loadScript, this));
			return reqInfo;
		},	
		
		requestLoadDataTypeOperationScript : function(dataTypeInfoArray, handlers, requester_parent){
			var requestInfo = this.getRequestInfoLoadDataTypeOperationScript(dataTypeInfoArray, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true);
		},

		
		getRequestInfoLoadUITagScript : function(uiTagsArray, handlers, requester_parent){
			var reqInfo = nosliwCreateServiceRequestInfoService(loc_getRequestServiceLoadScript(NOSLIWCOMMONCONSTANT.CONS_SCRIPTTYPE_UITAGS, {"requestArray":uiTagsArray}), handlers, loc_getRequesterParent(requester_parent));
			reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_loadScript, this));
			return reqInfo;
		},	
		
		requestLoadUITagScript : function(uiTagsArray, handlers, requester_parent){
			var requestInfo = this.getRequestInfoLoadUITagScript(uiTagsArray, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true);
		},
		
	};
	
	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);

	return loc_out;
};
