//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_buildServiceProvider;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoExecutor;
	var node_requestServiceProcessor;
	var node_ServiceInfo;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_runtimeGateway;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_resourceManager = resourceManager;

	var loc_getLoadResourceRequest = function(resourceIds, handlers, requestInfo){
		var out = node_createServiceRequestInfoExecutor(new node_ServiceInfo("LoadResources", {"resourceId":resourceIds}), function(requestInfo){
			node_runtimeGateway.loadResources(resourceIds, function(){
				var resourceResult = loc_findResources(resourceIds);
				nosliw.logging.info("Loaded Resources  ", resourceResult);
				requestInfo.executeSuccessHandler(resourceResult.found);
			});
		}, handlers, requestInfo);
		return out;
	};
	
	var loc_getFindResourcesRequest = function(resourceIds, handlers, requestInfo){
		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("FindResources", {"resourceId":resourceIds}), function(requestInfo){
			return loc_findResources(resourceIds);
		}, handlers, requestInfo);
		return out;
	};

	var loc_findResources = function(resourceIds){
		var resources = [];
		var missingResourceIds = [];
		_.each(resourceIds, function(resourceId, index, list){
			var resource = loc_resourceManager.useResource(resourceId);
			if(resource!=undefined){
				resources.push(resource);
			}
			else{
				missingResourceIds.push(resourceId);
			}
		});
		return {
			found : resources,
			missed : missingResourceIds
		};
	}
	
	var loc_out = {

		getDiscoverAndGetResourcesRequest : function(resourceIds, handlers, requestInfo){
				
		},
			
		getDiscoverResourcesRequest : function(resourceIds, handlers, requestInfo){
			
		},
			
		getGetResourcesRequest : function(resourceIds, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetResources", {"resourceId":resourceIds}), handlers, this.getRequestInfo(requestInfo));
			
			out.addRequest(loc_getFindResourcesRequest(resourceIds, {
				success : function(requestInfo, data){
					if(data.missed.length==0){
						//all found
						return data.found;
					}
					else{
						//need load resource
						var loadResourceRequest = loc_getLoadResourceRequest(data.missed, {
							success : function(requestInfo, data){
								return requestInfo.getData("resources").concat(data);
							}
						}, requestInfo);
						loadResourceRequest.setData("resources", data.found);
						return loadResourceRequest;
					}
				}
			}, requestInfo));
			return out;
		},
		
		executeGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		}
			
	};
	
	loc_out = node_buildServiceProvider(loc_out, "resourceService");
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createResourceService", node_createResourceService); 

	var module = {
		start : function(packageObj){
			node_requestUtility = packageObj.getNodeData("request.utility");
			node_buildServiceProvider = packageObj.getNodeData("request.buildServiceProvider");
			node_createServiceRequestInfoSequence = packageObj.getNodeData("request.request.createServiceRequestInfoSequence");
			node_createServiceRequestInfoSimple = packageObj.getNodeData("request.request.createServiceRequestInfoSimple");
			node_createServiceRequestInfoExecutor = packageObj.getNodeData("request.request.createServiceRequestInfoExecutor");
			node_requestServiceProcessor = packageObj.getNodeData("request.requestServiceProcessor");
			node_ServiceInfo = packageObj.getNodeData("common.service.ServiceInfo");
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
			node_CONSTANT = packageObj.getNodeData("constant.CONSTANT");
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_runtimeGateway = packageObj.getNodeData(node_COMMONCONSTANT.RUNTIME_LANGUAGE_JS_GATEWAY);
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
