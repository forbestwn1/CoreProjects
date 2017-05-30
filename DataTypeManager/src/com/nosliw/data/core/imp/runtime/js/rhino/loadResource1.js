	var resourceIds = [];
	resourceIds.push(
		{
			type : "operation",
			id : "core.url;1.1.0;url_normal2"
		}
	);
	
	
	var resourceService = nosliw.runtime.getResourceService();
	resourceService.executeGetResourcesRequest(resourceIds, {
		success : function(){
			var resourceManager = nosliw.runtime.getResourceManager();
			var resource = resourceManager.useResource(resourceIds[0]);
			nosliw.logging.info("Get resource : ", resource);
		}
	}, undefined);
