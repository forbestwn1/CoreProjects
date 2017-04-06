/**
 * 
 */
var nosliwCreateRemoteServiceManager = function(){
	
	var loc_resources = {};
	
	var loc_out = {
		
		getResource : function(resourceId){
			var type = resourceId[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCEID_ID];
			var typeResources = loc_resources[type];
			var resourceWrapper = typeResources[id];
			var resource = resourceWrapper.resource;
			return resource;
		},
		
		getResourceData : function(resourceId){
			var resource = this.getResource(resourceId);
			return resource[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCE_RESORUCEDATA];
		},
		
		resourceExists : function(resourceId){
			if(this.getResource===undefined)  return false;
			else return true;
		},
		
		findMissingResources : function(resourcesIdArray){
			var out = [];
			for(var index in resourcesIdArray){
				var resourceId = resourcesIdArray[index];
				if(!this.resoruceExists(resourceId)){
					out.push(resourceId);
				}
			}
			return out;
		},
		
		addResource : function(resource){
			var resourceId = resource[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCE_ID];
			var type = resourceId[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCEID_TYPE];
			var id = resourceId[NOSLIWATCOMMONTRIBUTECONSTANT.RESOURCEID_ID];
			
			var typeResources = loc_resources[type];
			if(typeResources===undefined){
				typeResources = {};
				loc_resources[type] = typeResources;
			}
			
			var resourceWrapper = new NosliwResourceWrapper(resource);
			typeResources[id] = resourceWrapper;
		},
		
		removeResource : function(resourceId){
			
		},
			
	};

	return loc_out;

};
