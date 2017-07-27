if(nosliwDataTypeOperations==undefined){
	var nosliwDataTypeOperations = {};
}
var nosliwCreateDataTypeManager = function(){
	//sync task name for remote call 
	var loc_moduleName = "dataTypeManager";
	
	var loc_dataTypes = {};
	
	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
	
	var loc_getDataType = function(dataTypeInfo){		
		var dataTypes = loc_dataTypes[dataTypeInfo.key];
		if(dataTypes==undefined)   return;
		else  return dataTypes[0];
	};
	
	var loc_getDataTypeWithAllVersions = function(dataTypeInfo){		
		var dataTypes = loc_dataTypes[dataTypeInfo.key];
		return dataTypes;
	};
	
	
	/*
	 * get requester according to req
	 * 		undefined : use default one
	 */
	var loc_getRequesterParent = function(req){
		if(req==undefined)   return loc_requester;
		else return req;
	};
	
	var loc_getRequestServiceGetDataTypes = function(dataTypeInfoArray){
		return new NosliwServiceInfo(NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_GETDATATYPES, 
				createParms().addParm(NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_REQUEST_GETDATATYPES_REQUESTARRAY,dataTypeInfoArray).getParmObj());
	};

	var loc_getRequestServiceExecuteDataOperation = function(baseDataTypeInfo, operation, parms){
		return new NosliwServiceInfo("executeDataOperation", {"baseDataTypeInfo":baseDataTypeInfo, "operation":operation, "parms":parms}); 
	};

	/*
	 * execute method : getDataTypes
	 */
	var loc_requestInfoGetDataTypes = function(requestInfo){
		var requestArray = requestInfo.getParmData(NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_REQUEST_GETDATATYPES_REQUESTARRAY);

		//find which data type is existing, which data type is new that need remote request
		var newArray = [];
		var existingArray = [];
		for(var i in requestArray){
			var dataTypeWithAllVersions = loc_getDataTypeWithAllVersions(requestArray[i]);
			if(dataTypeWithAllVersions!=undefined){
				existingArray.push(dataTypeWithAllVersions);
			}
			else{
				newArray.push(requestArray[i]);
			}
		}
		
		if(newArray.length==0){
			requestInfo.executeSuccessHandler(existingArray, this);
		}
		else{
			requestInfo.setRequestProcessors({
				success : function(reqInfo, dataTypeMap){
					_.each(dataTypeMap, function(dataType, key, list){
						existingArray.push(dataType);
						loc_dataTypes[key] = dataType;
					}, this);
					return existingArray;
				}, 
			});
			requestInfo.setParmData(NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_REQUEST_GETDATATYPES_REQUESTARRAY, newArray);
			var remoteTask = nosliwRequestUtility.getRemoteServiceTask(loc_moduleName, requestInfo);
			return remoteTask;
		}
	};

	var loc_executeNewOperationLocally = function(dataTypeInfo, operation, parms){
		var version = dataTypeInfo.version;
		var opFunction = loc_getDataTypeOperationsScriptVersion(dataTypeInfo, version).newFunctions[operation];
		return opFunction.call(this, parms);
	};
	
	var loc_executeDataOperationLocally = function(dataTypeInfo, operation, parms){
		var version = dataTypeInfo.version;
		var opFunction = loc_getDataTypeOperationsScriptVersion(dataTypeInfo, version).opFunctions[operation];
		return opFunction.call(this, parms);
	};

	/*
	 * get data type operations script of all versions
	 */
	var loc_getDataTypeOperationsScriptAllVersions = function(dataTypeInfo){
		return nosliwDataTypeOperations[dataTypeInfo.key];
	};

	var loc_getDataTypeOperationsScriptVersion = function(dataTypeInfo, version){
		return loc_getDataTypeOperationsScriptAllVersions(dataTypeInfo)[version];
	};
	
	var loc_isNewOpeation = function(operation){
		return operation.startsWith(NOSLIWCOMMONCONSTANT.CONS_DATAOPERATION_NEWDATA);
	};
	
	/*
	 * execute data operation
	 */
	var loc_executeDataOperation = function(dataTypeInfo, operation, parms){
		var oldParms = [];
		for(var i=0; i<parms.length; i++)		oldParms.push(parms[i]);
		
		var dataTypeAllVersionsArray = loc_dataTypes[dataTypeInfo.key];
		var dataTypeLatestVersion = dataTypeAllVersionsArray[0];
		var parentDataTypeInfo = dataTypeLatestVersion[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_DATATYPE_PARENT];
		
		var dataTypeOpInfo = dataTypeLatestVersion[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_DATATYPE_OPERATIONINFOS][operation];
		var convertPath = dataTypeOpInfo[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_DATAOPERATIONINFO_CONVERTPATH];
		
		if(loc_isNewOpeation(operation)==true){
			//new operation
			return loc_executeNewOperationLocally(dataTypeLatestVersion.dataTypeInfo, operation, parms);
		}
		else if(convertPath==undefined){
			//if no convert path, then just run it 
			return loc_executeDataOperationLocally(dataTypeLatestVersion.dataTypeInfo, operation, parms);
		}
		else{
			var convertPathSegs = nosliwNamingConversionUtility.parsePathInfos(convertPath);
			var newDataTypeInfo = dataTypeInfo;
			var newDataType = dataTypeLatestVersion;
			for(var i in convertPathSegs){
				var newParms = [];
				var convertPathSeg = convertPathSegs[i];
				var convertPathDetailSegs = nosliwNamingConversionUtility.parseDetailInfos(convertPathSeg);
				if(convertPathDetailSegs[0]==NOSLIWCOMMONCONSTANT.CONS_OPERATIONDEF_PATH_PARENT){
					//convert to operation on parent data type
					//convert parms first
					for(var j in oldParms){
						var d = oldParms[j];
						if(d.dataTypeInfo.equalsWithoutVersion(newDataTypeInfo)){
							//if parm's data type equals to base data type, then conver it to parent data type
							var toParentParms = [];
							toParentParms.push(d);
							var s = loc_executeDataOperation(parentDataTypeInfo, HAPConstant.CONS_DATAOPERATION_TOPARENTTYPE, toParentParms);
							if(nosliwErrorUtility.isScuss(s))   newParms.push(s.data);
							else  return s;
						}
						else{
							newParms.push(d);
						}
					}
					newDataTypeInfo = newDataType.parent;
					newDataType = loc_out.getDataType(newDataTypeInfo);
				}
				else if(convertPathDetailSegs[0]==NOSLIWCOMMONCONSTANT.HAPConstant.CONS_OPERATIONDEF_PATH_VERSION){
					var newVersion = convertPathDetailSegs[0];

					for(var j in oldParms){
						var d = oldParms[j];
						if(d.dataTypeInfo.equalsWithoutVersion(newDataTypeInfo)){
							var toVersionParms = [];
							toVersionParms.push(d);
							toVersionParms.push(newVersion);
							var s = loc_executeDataOperation(parentDataTypeInfo, HAPConstant.CONS_DATAOPERATION_TOVERSION, toVersionParms);
							if(nosliwErrorUtility.isScuss(s))   newParms.push(s.data);
							else  return s;
						}						
						else{
							newParms.push(d);
						}
					}
					newDataTypeInfo = newDataType.parent;
					newDataType = loc_out.getDataType(newDataTypeInfo);
				}
				oldParms = newParms;
			}

			var out = loc_executeDataOperationLocally(newDataTypeInfo, operation, oldParms);
			if(nosliwErrorUtility.isSuccess(out) && out.data!=undefined){
				var outData = out.data;
				if(outData.dataTypeInfo.equalsWithoutVersion(dataTypeInfo)){
					//convert the result back to current version
					var fromVersionParms = [];
					fromVersionParms.push(outData);
					var s = loc_executeDataOperation(dataTypeInfo, HAPConstant.CONS_DATAOPERATION_FROMVERSION, fromVersionParms);
					if(nosliwErrorUtility.isScuss(s))   out.data = s.data;
					else  return s;
				}
			}
		}
	};
	
	var loc_getOperationDependentDataTypeInfo = function(dataType, operation){
		var out = dataType.operationInfos[operation].dependentDataTypeInfos;
		if(out==undefined)  out = [];
		return out;
	};
	
	var loc_resourceLifecycleObj = {};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_SUSPEND"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_RESUME"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DESTROY"] = function(){};
	loc_resourceLifecycleObj["NOSLIWCONSTANT.LIFECYCLE_RESOURCE_EVENT_DEACTIVE"] = function(){};
	
	var loc_out = {
		ovr_getResourceLifecycleObject : function(){	return loc_resourceLifecycleObj;	},

		/*
		 * request to get data type object
		 * in dataTypeInfosArray:  an array of data type info 
		 * out data : 
		 * 		an array of data types
		 * 		it includes request data types + parent data types
		 */
		getRequestInfoGetDataTypes : function(dataTypeInfoArray, handlers, requester_parent){
			var service = loc_getRequestServiceGetDataTypes(dataTypeInfoArray);

			var reqInfo = nosliwCreateRequestSet(service, handlers, loc_getRequesterParent(requester_parent));
			
			//request for loading data type info
			var getDataTypeReqInfo = nosliwCreateServiceRequestInfoService(service, {});
			getDataTypeReqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_requestInfoGetDataTypes, this));

			//request for loading data operation script for data type
			var dataTypeInfoArrayForScript = [];
			for(var i in dataTypeInfoArray){
				var dataTypeInfo = dataTypeInfoArray[i];
				if(loc_getDataTypeOperationsScriptAllVersions(dataTypeInfo)==undefined){
					dataTypeInfoArrayForScript.push(dataTypeInfo);
				}
			}
			if(dataTypeInfoArrayForScript.length>0){
				var scriptReqInfo = nosliw.getLoadScriptManager().getRequestInfoLoadDataTypeOperationScript(dataTypeInfoArrayForScript, {});
				reqInfo.addRequest("loadScript", scriptReqInfo);
			}
			
			reqInfo.addRequest("loadDataTypes", getDataTypeReqInfo);
			
			reqInfo.setRequestProcessors({
				success : function(reqInfo, result){
					var dataTypeArrays = result.getResult('loadDataTypes');
					return dataTypeArrays;
				}, 
			});
			
			return reqInfo;
		},	
		
		requestGetDataTypes : function(dataTypeInfoArray, handlers, requester_parent){
			var requestInfo = this.getRequestInfoGetDataTypes(dataTypeInfoArray, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true); 
		},

		/*
		 * request to execute data operation
		 * out data : operation result data
		 */
		getRequestInfoExecuteDataOperation : function(baseDataTypeInfo, operation, parms, handlers, requester_parent){
			var service = loc_getRequestServiceExecuteDataOperation(baseDataTypeInfo, operation, parms)
			var seqRequest = nosliwCreateRequestSequence(service, handlers, loc_getRequesterParent(requester_parent));
			
			var baseDataType = loc_getDataType(baseDataTypeInfo);
			if(baseDataType!=undefined){
				//baseDataType is loaded already, check operation dependent data type 
				var dependentDataTypesArray = loc_getOperationDependentDataTypeInfo(baseDataType, operation);
				var req1 = this.getRequestInfoGetDataTypes(dependentDataTypesArray, {
					success : function(requestInfo, dataTypeArray){
						var out = loc_executeDataOperation(baseDataTypeInfo, operation, parms);
						if(out==undefined)  nosliwDataUtility.createEmptyData();
						return out;
					},
				});
				seqRequest.addRequest(req1);
			}
			else{
				var dataTypeInfoArray = [];
				dataTypeInfoArray.push(baseDataTypeInfo);
				//request to get data type for base data type
				var req1 = this.getRequestInfoGetDataTypes(dataTypeInfoArray, {
					success : function(requestInfo, dataTypeArray){
						//after get base data type, request to get operation dependent data type
						var baseDataType = loc_getDataType(baseDataTypeInfo);
						
						var dependentDataTypesArray = loc_getOperationDependentDataTypeInfo(baseDataType, operation);

						var req2 = loc_out.getRequestInfoGetDataTypes(dependentDataTypesArray, {
							success : function(requestInfo, dataTypeArray){
								//after get operation dependent data type, request to execute data operation
								var out = loc_executeDataOperation(baseDataTypeInfo, operation, parms);
								if(out==undefined)  nosliwDataUtility.createEmptyData();
								return out;
							},
						});
						return req2;
					},
				});
				seqRequest.addRequest(req1);
			}
			
			return seqRequest;
		},	

		requestExecuteDataOperation : function(baseDataTypeInfo, operation, parms, handlers, requester_parent){
			var requestInfo = this.getRequestInfoExecuteDataOperation(baseDataTypeInfo, operation, parms, handlers, requester_parent);
			return nosliw.getRequestServiceManager().processRequest(requestInfo, true); 
		},
				
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	
	return loc_out;
};
