//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createResourceService;
//*******************************************   Start Node Definition  ************************************** 	

var node_createUIResourceService = function(){
	//sync task name for remote call 
	var loc_moduleName = "uiResourceManager";
	
	//all the ui resources
	var loc_uiResources = {};

	//default requester 
	var loc_requester = new NosliwRequester(NOSLIWCONSTANT.REQUESTER_TYPE_SERVICE, loc_moduleName); 
			
	var loc_getResourceViewId = function(){	return nosliw.generateId();	};
	
	/*
	 * get requester according to req
	 * 		undefined : use default one
	 */
	var loc_getRequesterParent = function(req){
		if(req==undefined)   return loc_requester;
		else return req;
	};

	/*
	 * get request service object for GetUIResource 
	 */
	var loc_getRequestServiceGetUIResource = function(name){
		var parms = {};
		parms[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_REQUEST_GETUIRESOURCE_NAME] = name;
		return new NosliwServiceInfo(NOSLIWCOMMONCONSTANT.CONS_REMOTESERVICE_GETUIRESOURCE, parms); 
	};
	
	var loc_getRequestServiceGetUIResourceView = function(name){
		return new NosliwServiceInfo("getUIResourceView", {"name":name}); 
	};
	
	var loc_requestInfoGetUIResource = function(requestInfo){
		var resourceName = requestInfo.getParmData('name');
		var resource = loc_uiResources[resourceName];

		if(resource!=undefined){
			requestInfo.executeSuccessHandler(resource, this);
		}
		else{
			requestInfo.setRequestProcessors({
				success : function(reqInfo, uiresource){
					var name = reqInfo.getParmData('name');
					loc_uiResources[name] = uiresource;
					return uiresource;
				}, 
			});
			var remoteTask = nosliwRequestUtility.getRemoteServiceTask(loc_moduleName, requestInfo);
			return remoteTask;
		}
	};
	
	var loc_buildUIResourceId = function(name){
		
	};
	
	var loc_out = {

			getGetUIResourceRequest : function(names, handlers, requester_parent){
				var requestInfo = loc_out.getRequestInfo(requester_parent);
				var out = node_createServiceRequestInfoService(new node_ServiceInfo("GetUIResource", {"names":names}), handlers, requestInfo)
				
				//get resource request
				var resourceIds = [];
				for(var i in names)		resourceIds.push(loc_buildUIResourceId(names[i]));
				var loadResourceRequest = getGetResourcesRequest(resourceIds);
				
				out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
					success : function(requestInfo, resourceTree){
						//translate tree to resources by id
						return node_resourceUtility.getResourcesByTypeFromTree(resourceTree, node_COMMONCONSTANT.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
					}
				}));
				return out;
			},
			
			
			
			getRequestInfoGetUIResource : function(name, handlers, requester_parent){
				
				//ui resource load child request
				var uiResourceReqInfoSetChild = nosliwCreateServiceRequestInfoService(loc_getRequestServiceGetUIResource(name), {});
				uiResourceReqInfoSetChild.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(loc_requestInfoGetUIResource, this));
				
				//ui resource script load child request
				var scriptReqInfoSetChild = nosliw.getLoadScriptManager().getRequestInfoLoadUIResourceScript(name, {});
				
				
				var uiResourceReqInfoSet = nosliwCreateRequestSet(new NosliwServiceInfo("loadUIResourceSet", {"name":name}), {
					success : function(request, result){
						var uiResource = result.getResult('getUIResource');
						request.getParentRequest().setData("uiResource", uiResource);
						return nosliwCommonUtility.createEmptyValue();
					},
				});
				uiResourceReqInfoSet.addRequest("getUIResource", uiResourceReqInfoSetChild);
				uiResourceReqInfoSet.addRequest("loadScript", scriptReqInfoSetChild);
				
				var uiResourceReqInfo = nosliwCreateRequestSequence(new NosliwServiceInfo("loadUIResource", {"name":name}), handlers, loc_getRequesterParent(requester_parent));
				uiResourceReqInfo.addRequest(uiResourceReqInfoSet);
				uiResourceReqInfo.addRequest(function(parentRequest){
					var absentTagNamesArray = [];
					var uiResource = parentRequest.getData("uiResource");
					var uiTagNamesArray = uiResource[NOSLIWATCOMMONATRIBUTECONSTANT.ATTR_UIRESOURCE_UITAGLIBS];
					for(var i in uiTagNamesArray){
						var tagName = uiTagNamesArray[i];
						var tagFacFun = nosliw.getUITagManager().getUITagFactoryFunction(tagName);
						if(tagFacFun==undefined){
							//tag does not exsits
							absentTagNamesArray.push(tagName);
						}
					}
					//ui tag load request
					if(absentTagNamesArray.length>0){
						var uiTagReqInfoSeqChild = nosliw.getLoadScriptManager().getRequestInfoLoadUITagScript(absentTagNamesArray, {});
						return uiTagReqInfoSeqChild;
					}
				});
				
				uiResourceReqInfo.setRequestProcessors({
					success : function(reqInfo, data){
						return reqInfo.getData("uiResource");
					}, 
				});

				return uiResourceReqInfo;
			},	
			
			requestGetUIResource : function(name, handlers, requester_parent){
				var requestInfo = this.getRequestInfoGetUIResource(name, handlers, requester_parent);
				return nosliw.getRequestServiceManager().processRequest(requestInfo); 
			},
			
			getRequestInfoCreateUIResourceView : function(name, handlers, requester_parent){
				var reqInfo = nosliwCreateServiceRequestInfoService(loc_getRequestServiceGetUIResourceView(name), handlers, loc_getRequesterParent(requester_parent));

				//set child service for this request : getUISource
				var depentService = new NosliwDependentServiceRequestInfo(this.getRequestInfoGetUIResource(name), {
					success : function(reqInfo, uiresource){
						return nosliwCreateUIResourceView(uiresource, loc_getResourceViewId(), undefined, undefined, depentService.requestInfo);
					} 
				});
				reqInfo.setDependentService(depentService);
				return reqInfo;
			},	
			
			requestCreateUIResourceView : function(name, handlers, requester_parent){
				var requestInfo = this.getRequestInfoCreateUIResourceView(name, handlers, requester_parent);
				return nosliw.getRequestServiceManager().processRequest(requestInfo);
			},
			
			getRequestInfoExecuteUIExpression : function(expressionObjsArray, uiExpFunction, handlers, reqer){
				var requester = loc_getRequester(reqer);

				var reqInfo = nosliwCreateRequestSet(new NosliwServiceInfo("executeUIExpression", {"expressionObjsArray":expressionObjsArray, "uiExpFunction":uiExpFunction}), handlers, requester);

				for(var i in expressionObjsArray){
					
				}
				
				reqInfo.addRequest("getUIResource", uiResourceReqInfo);
				reqInfo.addRequest("loadScript", scriptReqInfo);
			},
	};

	//append resource life cycle method to out obj
	loc_out = nosliwLifecycleUtility.makeResourceObject(loc_out, loc_moduleName);
	
	return loc_out;
};

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data

//Register Node by Name
packageObj.createChildNode("createUIResourceService", node_createUIResourceService); 

})(packageObj);
