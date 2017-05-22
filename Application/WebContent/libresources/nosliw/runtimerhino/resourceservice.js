//get/create package
var packageObj = library.getChildPackage("resourceservice");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	
	var loc_createServiceRequestInfoSequence;
	
/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(resourceManager){
	
	var loc_resourceManager = resourceManager;

	var loc_getLoadResourceRequest = function(resourceIds, handlers, requestInfo){
		nosliw.runtime.javaInterface.loadResources(resourceIds, function(){
			nosliw.logging.info("");
		});
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
			
		}
			
	};
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createResourceService", node_createResourceService); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
