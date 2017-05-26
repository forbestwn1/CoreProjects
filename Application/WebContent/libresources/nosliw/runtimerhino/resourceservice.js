//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_createServiceRequestInfoSequence;
	var node_createServiceRequestInfoSimple;
	var node_requestServiceProcessor;
//*******************************************   Start Node Definition  ************************************** 	
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_resourceManager = resourceManager;

	var loc_getLoadResourceRequest = function(resourceIds, handlers, requestInfo){
		
		aaaa.loadResources('aa', 'bb');
		
//		aaaa.loadResources(resourceIds, function(){
//			nosliw.logging.info("AAAAAAAAAAAA");
//		});
		
//		nosliw.runtime.javaInterface.loadResources(resourceIds, function(){
//			nosliw.logging.info("");
//		});
	}
	
	var loc_out = {
		
		getGetResourcesRequest : function(resourceIds, handlers, requestInfo){
			var out = node_createServiceRequestInfoSequence(undefined, handlers, requestInfo);
			
			var resources = [];
			var missingResourceIds = [];
			
			out.addRequest(node_createServiceRequestInfoSimple(undefined, function(requestInfo){
				_.each(resourceIds, function(resourceId, index, list){
					var resource = loc_resourceManager.useResource(resourceId);
					if(resource!=undefined){
						resources.push(resource);
					}
					else{
						missingResourceIds.push(resourceId);
					}
				});
				
				if(missingResourceIds.length==0){
					return resources;
				}
				else{
					return loc_getLoadResourceRequest(missingResourceIds, null, out);
				}
			}, null, out));

			return out;
		},
		
		executeGetResourcesRequest : function(resourceIds, handlers, requestInfo){
			var requestInfo = this.getGetResourcesRequest(resourceIds, handlers, requestInfo);
			node_requestServiceProcessor.processRequest(requestInfo, false);
		}
			
	};
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createResourceService", node_createResourceService); 

	var module = {
		start : function(packageObj){
			node_createServiceRequestInfoSequence = packageObj.getNodeData("request.request.createServiceRequestInfoSequence");
			node_createServiceRequestInfoSimple = packageObj.getNodeData("request.request.createServiceRequestInfoSimple");
			node_requestServiceProcessor = packageObj.getNodeData("request.requestServiceProcessor");
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
