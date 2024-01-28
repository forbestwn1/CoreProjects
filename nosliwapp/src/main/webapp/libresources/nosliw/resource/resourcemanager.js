//get/create package
var packageObj = library;    

(function(packageObj){
	//get used node
	var node_Resource;
	var node_resourceUtility;
//*******************************************   Start Node Definition  ************************************** 	

/**
 * Create Resource Manager
 * It manage resources
 * It does not do the job of loading resources, it is the job of resource service
 */
var node_createResourceManager = function(){
	
	var loc_resources = {};

	var loc_getResource = function(resourceId){
		return node_resourceUtility.getResourceFromTree(loc_resources, resourceId);
	};
	
	var loc_out = {
		
		/**
		 * Add resource to resourc manager 
		 */
		addResource : function(resourceInfo, resourceData, info){
			var resource = new node_Resource(resourceInfo, resourceData, info);
			node_resourceUtility.buildResourceTree(loc_resources, resource);
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

//populate dependency node data
nosliw.registerSetNodeDataEvent("resource.entity.Resource", function(){node_Resource = this.getData();});
nosliw.registerSetNodeDataEvent("resource.utility", function(){node_resourceUtility = this.getData();});

//Register Node by Name
packageObj.createChildNode("createResourceManager", node_createResourceManager); 

})(packageObj);
