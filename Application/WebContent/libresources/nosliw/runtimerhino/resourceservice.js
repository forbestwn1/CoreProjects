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
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_resourceManager = resourceManager;

	var loc_getFindDsicoveredResourcesRequest = function(resourceIds, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoSimple(new node_ServiceInfo("FindDiscoveredResources", {"resourcesId":resourceIds}), function(requestInfo){
			var result = {
				found : {},
				missed : []
			};
			loc_findDiscoveredResources(resourceIds, result);
			return result;
		}, handlers, requestInfo);
		return out;
	};
	
	//find all the resources by id and related resources
	var loc_findDiscoveredResources = function(resourceIds, result){
		var foundResources = result.found;
		var missedResourceIds = result.missed;
		
		_.each(resourceIds, function(resourceId, index, list){
			var resource = loc_resourceManager.useResource(resourceId);
			if(resource!=undefined){
				//resource exist
				node_resourceUtility.buildResourceTree(foundResources, resource);

				//discover related resources (dependency and children)
				var relatedResourceIds = []; 
				var resourceInfo = resource.resourceInfo;
				_.each(resourceInfo[node_COMMONTRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY], function(child, index, list){
					relatedResourceIds.push(child[node_COMMONTRIBUTECONSTANT.RESOURCEDEPENDENT_ID]);
				}, this);

				_.each(resourceInfo[node_COMMONTRIBUTECONSTANT.RESOURCEINFO_CHILDREN], function(child, index, list){
					relatedResourceIds.push(child[node_COMMONTRIBUTECONSTANT.RESOURCEDEPENDENT_ID]);
				}, this);
				
				loc_findDiscoveredResources(relatedResourceIds, result);
			}
			else{
				missedResourceIds.push(resourceId);
			}
		});
	}

	//load resources for runtime
	var loc_getLoadResourcesRequest = function(resourceInfos, handlers, requester_parent){
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoExecutor(new node_ServiceInfo("LoadResources", {"resourcesInfo":resourceInfos}), function(requestInfo){
			node_runtimeGateway.loadResources(resourceInfos, function(){
				out.executeSuccessHandler();
			});
		}, handlers, requestInfo);
		return out;
		
	}
	
	var loc_out = {

		getGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetResources", {"resourcesId":resourceIds}), handlers, loc_out.getRequestInfo(requester_parent));

			//find missing resources
			out.addRequest(loc_getFindDsicoveredResourcesRequest(resourceIds, {
				success : function(requestInfo, data){
					var missedResourceIds = data.missed;
					var foundResoruces = data.found;
					if(missedResourceIds.length==0){
						//all found
						return foundResoruces;
					}
					else{
						//need load resource
						//do discovery first
						var discoverResourcesRequest = loc_out.getDiscoverResourcesRequest(missedResourceIds, {
							success : function(requestInfo, resourceInfos){
								//after discovery, load resources
								var loadResourceRequest = loc_getLoadResourcesRequest(resourceInfos, {
									success : function(requestInfo){
										_.each(resoruceInfos, function(resourceInfo, index, list){
											var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONTRIBUTECONSTANT.RESOURCEINFO_ID]);
											node_resourceUtility.buildResourceTree(foundResources, resource);
										}, this);
										return foundResources;
									}
								}, null);
								return loadResourceRequest;
							}
						}, null);
						return discoverResourcesRequest;
					}
				}
			}, out));
			return out;
		},
			
		executeGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		},
			

		getDiscoverResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			
			var out = node_createServiceRequestInfoExecutor(new node_ServiceInfo("DiscoverResources", {"resourcesId":resourceIds}), function(requestInfo){
				node_runtimeGateway.descoverResources(resourceIds, function(resourcesInfo){
					requestInfo.executeSuccessHandler(resourcesInfo);
				});
			}, handlers, requestInfo);
			return out;
		},
			
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
			node_COMMONCONSTANT = packageObj.getNodeData("constant.COMMONCONSTANT");
			node_runtimeGateway = packageObj.getNodeData(node_COMMONCONSTANT.RUNTIME_LANGUAGE_JS_GATEWAY);
			node_resourceUtility = packageObj.getNodeData("resource.resourceUtility");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
