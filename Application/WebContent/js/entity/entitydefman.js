/**
 * 
 */
var nosliwCreateEntityDefinitionManager = function(){
	//sync task name for remote call 
	var loc_moduleName = "entityDefinitionManager";
	
	var loc_entityDefinitions = {};
	
	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 

	var loc_getEntityDefinitionByName = function(name){		
		return loc_entityDefinitions[name];
	};
	
	var loc_addEntityDefinition = function(name, entityDef){
		loc_entityDefinitions[name] = entityDef;
	};
	
	var loc_getRequestServiceGetEntityDefinitionsByNames = function(entityNamesArray){
		return new NosliwServiceInfo(NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_GETALLENTITYDEFINITIONS, 
				createParms().addParm(NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_REQUEST_GETENTITYDEFINITIONBYNAMES_NAMES,entityNamesArray).getParmObj());
	};
	
	/*
	 * execute method : getEntityDefinitionByNames
	 */
	var loc_requestInfoGetEntityDefinitionByNames = function(requestInfo){
		var requestArray = requestInfo.getParmData(NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_REQUEST_GETENTITYDEFINITIONBYNAMES_NAMES);

		//find which data type is existing, which data type is new that need remote request
		var newArray = [];
		var existingEntityDefs = {};
		for(var i in requestArray){
			var enityName = requestArray[i];
			var entityDef = loc_getEntityDefinitionByName(entityName);
			if(entityDef!=undefined)	existingEntityDefs[entityName] = entityDef;
			else		newArray.push(entityName);
		}
		
		if(newArray.length==0){
			requestInfo.executeSuccessHandler(existingEntityDefs, this);
		}
		else{
			requestInfo.setRequestProcessors({
				success : function(reqInfo, entityDefMap){
					_.each(entityDefMap, function(entityDef, key, list){
						existingEntityDefs[key] = entityDef;
						loc_addEntityDefinition(key, dataType);
					}, this);
					return existingEntityDefs;
				}, 
			});
			requestInfo.setParmData(NOSLIWATCOMMONTRIBUTECONSTANT.ATTR_REQUEST_GETENTITYDEFINITIONBYNAMES_NAMES, newArray);
			var remoteTask = nosliwRequestUtility.getRemoteServiceTask(loc_moduleName, requestInfo);
			return remoteTask;
		}
	};
	
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		getRequestInfoGetEntityDefinitionsByNames : function(namesArray, handlers, requester_parent){
			var service = loc_getRequestServiceGetEntityDefinitionsByNames(dataTypeInfoArray);

			var reqInfo = nosliwCreateServiceRequestInfoService(service, {});
			reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_requestInfoGetEntityDefinitionByNames, this));

			return reqInfo;
			
		},

		requestGetEntityDefinitionsByNames : function(namesArray, handlers, requester_parent){
			var requestInfo = this.getRequestInfoGetEntityDefinitionsByNames(namesArray, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true); 
		},
		
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	
	return loc_out;
};
