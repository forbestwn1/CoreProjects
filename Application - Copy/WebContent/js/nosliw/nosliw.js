/**
 * 
 */

var nosliw = function(){
	//id creation
	var loc_nextId = 0;
	//current module name, used in logging
	var loc_moduleName = "nosliw";
	//current client id
	var loc_clientId = undefined;
	
	var loc_remoteServiceManager = undefined;
	var loc_requestServiceManager = undefined;
	var loc_loginService = undefined;
	var loc_uiResourceManager = undefined;
	var loc_uiTagManager = undefined;
	var loc_loadScriptManager = undefined;
	var loc_dataTypeManager = undefined;
	var loc_entityDefitionManager = undefined;
	
	var loc_construction = function(){
		//ui tag manager is init when construction so that tag lib can be registered when system loading
		loc_uiTagManager = nosliwCreateUITagManager();
		loc_uiTagManager.init();
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_INIT"] = function(){
		loc_uiResourceManager = nosliwCreateUIResourceManager();
		loc_uiResourceManager.init();
	};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};

	var loc_login = function(loginInfo){
		loc_loginService.requestLogin(loginInfo, {
			success : function(requestInfo, clientId){
				nosliwLogging.info(loc_moduleName, "Success login with client Id : " + clientId);
				loc_clientId = clientId;
				loc_out.init();
			},
			fail : function(requestInfo, serviceData){
			},
			exception : function(requestInfo, serviceData){
			},
		});
	};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		start : function(){
			nosliwLogging.info(loc_moduleName, "Start");

			//init id seed
			var d = new Date();
//			loc_nextId = d.getMilliseconds();
			
			//init remote service
			loc_remoteServiceManager = nosliwCreateRemoteServiceManager();
			loc_remoteServiceManager.init();
			
			loc_requestServiceManager = nosliwCreateRequestServiceManager();
			loc_requestServiceManager.init();
			
			//init login service
			loc_loginService = nosliwCreateLoginService();
			loc_loginService.init();
			
			loc_loadScriptManager = nosliwCreateLoadScriptService();
			loc_loadScriptManager.init();
			
			loc_dataTypeManager = nosliwCreateDataTypeManager();
			loc_dataTypeManager.init();
			
			loc_entityDefinitionManager = nosliwCreateEntityDefinitionManager();
			loc_entityDefinitionManager.init();
			
			//login
			loc_login({});
		},
		
		getRemoteServiceManager : function(){  return loc_remoteServiceManager;  },
		getRequestServiceManager : function(){ return loc_requestServiceManager; },
		getLoginService : function(){  return loc_loginService; },
		getUIResourceManager : function(){ return loc_uiResourceManager; },
		getUITagManager : function(){  return loc_uiTagManager;},
		getLoadScriptManager : function(){ return loc_loadScriptManager; },
		getDataTypeManager : function(){ return loc_dataTypeManager; },
		getEntityDefinitionManager : function(){ return loc_entityDefinitionManager; },
		
		getClientId : function(){  return loc_clientId;  },
		
		generateId : function(){	return loc_nextId++ + ""; }
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);

	loc_construction();
	
	return loc_out;
}();
