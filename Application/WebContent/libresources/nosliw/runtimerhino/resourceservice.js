//get/create package
var packageObj = library.getChildPackage("resourceservice");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * Create Resource Service
 * This service response to request from user
 * Load resource to resource manager if needed
 */
var node_createResourceService = function(){
	
	var loc_resources = {};

	var loc_getResource = function(resourceId){
		var typeResources = loc_resources[resourceId.type];
		if(typeResources==undefined)  return undefined;
		return typeResources[resourceId.id];
	}
	
	var loc_out = {
		
		getGetResourcesRequest : function(resourceIds, handlers, requestInfo){
			
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
