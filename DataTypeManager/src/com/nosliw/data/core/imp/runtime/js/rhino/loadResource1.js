	var resourceIds = [];
	resourceIds.push(
		{
			type : "operation",
			id : "core.url;1.1.0;url_normal2"
		}
	);
	
	var resourceService = nosliw.runtime.getResourceService();
	resourceService.executeGetResourcesRequest(resourceIds, {
		success : function(requestInfo, resources){
			var resourceManager = nosliw.runtime.getResourceManager();
			nosliw.logging.info("Get resource : ", resources);
		}
	}, undefined);
