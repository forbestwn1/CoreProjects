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
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_moduleName = "resourceService";
	
	var loc_resourceManager = resourceManager;

	var loc_getLoadResourceRequest = function(resourceIds, handlers, requestInfo){
		var out = node_createServiceRequestInfoExecutor(new node_ServiceInfo("LoadResources", {"resourceId":resourceIds}), function(requestInfo){
			aaaa.loadResources(resourceIds, function(){
				var resourceResult = loc_findResources(resourceIds);
				nosliw.logging.info("Loaded Resources  ", resourceResult);
				requestInfo.executeSuccessHandler(resourceResult.found);
			});
		}, handlers, requestInfo);
		return out;
		
//		aaaa.loadResources('aa', {});
		
//		nosliw.runtime.javaInterface.loadResources(resourceIds, function(){
//			nosliw.logging.info("");
//		});
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
		
		executeGetResourcesRequest : function(resourceIds, handlers, requestInfo){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		}
			
	};
	
	loc_out = node_buildServiceProvider(loc_out, loc_moduleName);
	
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
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
