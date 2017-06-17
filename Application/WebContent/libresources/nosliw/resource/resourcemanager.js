//get/create package
var packageObj = library.getChildPackage("resourcemanager");    

(function(packageObj){
	//get used node
//*******************************************   Start Node Definition  ************************************** 	

/**
 * Create Resource Manager
 * It manage resources
 * It does not do the job of loading resources, it is the job of resource service
 */
var node_createResourceManager = function(){
	
	var loc_resources = {};

	var loc_getResource = function(resourceId){
		var typeResources = loc_resources[resourceId.type];
		if(typeResources==undefined)  return undefined;
		return typeResources[resourceId.id];
	}
	
	var loc_out = {
		
		/**
		 * Add resource to resourc manager 
		 */
		addResource : function(resourceInfo, resourceData, info){
			var resourceId = resourceInfo.id;
			var typeResources = loc_resources[resourceId.type];
			if(typeResources==undefined){
				typeResources = {};
				loc_resources[resourceId.type] = typeResources;
			}
			typeResources[resourceId.id] = {
				data : resourceData,	
				info : info,
			};
		},	
	
		/**
		 * Same as get resources
		 * It also mark the resource as using by user
		 */
		useResource : function(resourceId, userId){
			return loc_getResource(resourceId);
		},
		
		/**
		 * It mark the resource as not using by user
		 */
		dismissResource : function(resourceId, userId){
			
		},
	};
	
	return loc_out;
};	

//*******************************************   End Node Definition  ************************************** 	
//Register Node by Name
packageObj.createNode("createResourceManager", node_createResourceManager); 

	var module = {
		start : function(packageObj){
		}
	};
	nosliw.registerModule(module, packageObj);

})(packageObj);
