	var resourceIds = [];
	resourceIds.push(
		{
			type : "operation",
			id : "core.url;1.1.0;url_normal3"
		}
//		{
//			type : "operation",
//			id : "base.string;1.0.0;subString"
//		}

	);
	
	var resourceService = nosliw.runtime.getResourceService();
	resourceService.executeGetResourcesRequest(resourceIds, {
		success : function(requestInfo, resourcesTree){
			var resourceManager = nosliw.runtime.getResourceManager();
			nosliw.logging.info("Get resource : ", resourcesTree);
		}
	}, undefined);
