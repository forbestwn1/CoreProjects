//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_buildServiceProvider;
	var node_requestUtility;
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_createServiceRequestInfoService;
	var node_createServiceRequestInfoExecutor;
	var node_requestServiceProcessor;
	var node_ServiceInfo;
	var node_CONSTANT;
	var node_COMMONCONSTANT;
	var node_COMMONATRIBUTECONSTANT;
	var node_resourceUtility;
	var node_ResourceId;
	var node_DependentServiceRequestInfo;
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
		var foundResourcesTree = result.found;
		var missedResourceIds = result.missed;
		
		_.each(resourceIds, function(resourceId, index, list){
			var resource = loc_resourceManager.useResource(resourceId);
			if(resource!=undefined){
				//resource exist
				node_resourceUtility.buildResourceTree(foundResourcesTree, resource);

				//discover related resources (dependency and children)
				var relatedResourceIds = []; 
				var resourceInfo = resource.resourceInfo;
				_.each(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_DEPENDENCY], function(childResourceId, alias, list){
					relatedResourceIds.push(childResourceId);
				}, this);

				_.each(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_CHILDREN], function(childResourceId, alias, list){
					relatedResourceIds.push(childResourceId);
				}, this);
				
				loc_findDiscoveredResources(relatedResourceIds, result);
			}
			else{
				missedResourceIds.push(resourceId);
			}
		});
	};

	//load resources for runtime
	var loc_getLoadResourcesRequest = function(resourceInfos, handlers, requester_parent){
		//gateway request
		var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_RESOURCE;
		var command = node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_LOADRESOURCES;
		var parms = {};
		parms[node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_LOADRESOURCES_RESOURCEINFOS] = resourceInfos;
		var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
		
		var requestInfo = loc_out.getRequestInfo(requester_parent);
		var out = node_createServiceRequestInfoService(new node_ServiceInfo("LoadResources", {"resourcesInfo":resourceInfos}), handlers, requestInfo);
		out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
		
		return out;
	};
	
	var loc_validateResourceId = function(resourceIds){
		_.each(resourceIds, function(resourceId, i){
			if(resourceId==undefined || resourceId[node_COMMONATRIBUTECONSTANT.RESOURCEID_ID]==undefined){
				var kkkk = 5555;
				kkkk++;
			}
		});
	};
	
	var loc_out = {
		//resource get
		getRequireResourcesRequest : function(resourcesInfo, handlers, requester_parent){
			var serviceInfo = new node_ServiceInfo("RequireResources", {"resourcesInfo":resourcesInfo});
			
			//look for resources in resource manager
			var resourceTree = {};
			var resourceInfos = [];
			_.each(resourcesInfo, function(resourceInfo, index, list){
				var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
				if(resource!=undefined)			node_resourceUtility.buildResourceTree(resourceTree, resource);
				else		resourceInfos.push(resourceInfo);
			}, this);
			
			var out;
			if(resourceInfos.length==0){
				//all exists
				 out = node_createServiceRequestInfoSimple(serviceInfo, function(){ return resourceTree; }, handlers, loc_out.getRequestInfo(requester_parent));
			}
			else{
				out = node_createServiceRequestInfoService(serviceInfo, handlers, loc_out.getRequestInfo(requester_parent));
				
				//load some
				var loadResourceRequest = loc_getLoadResourcesRequest(resourceInfos, {}, null);
				
				out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
					success : function(requestInfo){
						_.each(resourceInfos, function(resourceInfo, index, list){
							var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
							node_resourceUtility.buildResourceTree(resourceTree, resource);
						}, this);
						return resourceTree;
					}}));
			}
			return out;
		},
			
			
		executeRequireResourcesRequest : function(resourcesInfo, handlers, requester_parent){
			var requestInfo = this.getRequireResourcesRequest(resourcesInfo, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
		//resource discovery + get
		getGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			loc_validateResourceId(resourceIds);
			
			var out = node_createServiceRequestInfoSequence(new node_ServiceInfo("GetResources", {"resourcesId":resourceIds}), handlers, loc_out.getRequestInfo(requester_parent));
			//find missing resources
			out.addRequest(loc_getFindDsicoveredResourcesRequest(resourceIds, {
				success : function(requestInfo, data){
					var missedResourceIds = data.missed;
					var foundResourcesTree = data.found;
					if(missedResourceIds.length==0){
						//all found
						return foundResourcesTree;
					}
					else{
						//need load resource
						//do discovery first
						var discoverResourcesRequest = loc_out.getDiscoverResourcesRequest(missedResourceIds, {
							success : function(requestInfo, resourceInfos){
								//after discovery, load resources
								var loadResourceRequest = loc_getLoadResourcesRequest(resourceInfos, {
									success : function(requestInfo){
										_.each(resourceInfos, function(resourceInfo, index, list){
											var resource = loc_resourceManager.useResource(resourceInfo[node_COMMONATRIBUTECONSTANT.RESOURCEINFO_ID]);
											node_resourceUtility.buildResourceTree(foundResourcesTree, resource);
										}, this);
										return foundResourcesTree;
									}
								});
								return loadResourceRequest;
							}
						});
						return discoverResourcesRequest;
					}
				}
			}, out));
			return out;
		},
			
		executeGetResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
			
		//resource discovery
		getDiscoverResourcesRequest : function(resourceIds, handlers, requester_parent){
			loc_validateResourceId(resourceIds);
			
			//gateway request
			var gatewayId = node_COMMONATRIBUTECONSTANT.RUNTIME_GATEWAY_RESOURCE;
			var command = node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES;
			var parms = {};
			parms[node_COMMONATRIBUTECONSTANT.GATEWAYRESOURCE_COMMAND_DISCOVERRESOURCES_RESOURCEIDS] = resourceIds;
			var gatewayRequest = nosliw.runtime.getGatewayService().getExecuteGatewayCommandRequest(gatewayId, command, parms);
			
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("DiscoverResources", {"resourcesId":resourceIds}), handlers, requestInfo);
			out.setDependentService(new node_DependentServiceRequestInfo(gatewayRequest));
			return out;
		},

		executeDiscoverResourcesRequest : function(resourceIds, handlers, requester_parent){
			var requestInfo = this.getDiscoverResourcesRequest(resourceIds, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		//getGetResourcesRequest + return resource data by type
		getGetResourceDataByTypeRequest : function(ids, resourceType, handlers, requester_parent){
			var requestInfo = loc_out.getRequestInfo(requester_parent);
			var out = node_createServiceRequestInfoService(new node_ServiceInfo("GetResourceDataByTypeRequest", {"ids":ids, "resourceType":resourceType}), handlers, requestInfo)
			
			//get resource request
			var resourceIds = [];
			for(var i in ids)		resourceIds.push(new node_ResourceId(resourceType, ids[i]));
			var loadResourceRequest = this.getGetResourcesRequest(resourceIds);
			
			out.setDependentService(new node_DependentServiceRequestInfo(loadResourceRequest, {
				success : function(requestInfo, resourceTree){
					//translate tree to resources by id
					var resourcesData = {};
					
					_.each(resourceIds, function(resourceId, i){
						var resource = node_resourceUtility.getResourceFromTree(resourceTree, resourceId);
						resourcesData[resourceId.name] = resource.resourceData;
					});
					
//					var resources = node_resourceUtility.getResourcesByTypeFromTree(resourceTree, resourceType);
//					_.each(resources, function(resource, id){
//						resourcesData[id] = resource.resourceData;
//					});
					return resourcesData;
				}
			}));
			return out;
		},
		
		executeGetResourceDataByTypeRequest : function(ids, resourceType, handlers, requester_parent){
			var requestInfo = this.getGetResourceDataByTypeRequest(ids, resourceType, handlers, requester_parent);
			node_requestServiceProcessor.processRequest(requestInfo);
		},
		
		/**
		 * Import resource 
		 */
		importResource : function(resourceInfo, resourceData, info){
			loc_resourceManager.addResource(resourceInfo, resourceData, info);
		},
		
		getResource : function(resourceId){
			return loc_resourceManager.useResource(resourceId);
		}
	};
	
	loc_out = node_buildServiceProvider(loc_out, "resourceService");
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	

//populate dependency node data
nosliw.registerSetNodeDataEvent("request.utility", function(){node_requestUtility = this.getData();});
nosliw.registerSetNodeDataEvent("request.buildServiceProvider", function(){node_buildServiceProvider = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSequence", function(){node_createServiceRequestInfoSequence = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoSimple", function(){node_createServiceRequestInfoSimple = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoService", function(){node_createServiceRequestInfoService = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.createServiceRequestInfoExecutor", function(){node_createServiceRequestInfoExecutor = this.getData();});
nosliw.registerSetNodeDataEvent("request.requestServiceProcessor", function(){node_requestServiceProcessor = this.getData();});
nosliw.registerSetNodeDataEvent("common.service.ServiceInfo", function(){node_ServiceInfo = this.getData();});
nosliw.registerSetNodeDataEvent("constant.CONSTANT", function(){node_CONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONCONSTANT", function(){node_COMMONCONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("constant.COMMONATRIBUTECONSTANT", function(){node_COMMONATRIBUTECONSTANT = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});
nosliw.registerSetNodeDataEvent("resource.entity.ResourceId", function(){node_ResourceId = this.getData();});
nosliw.registerSetNodeDataEvent("request.request.entity.DependentServiceRequestInfo", function(){node_DependentServiceRequestInfo = this.getData();});

//Register Node by Name
packageObj.createChildNode("createResourceService", node_createResourceService); 

})(packageObj);
