	var resourceIds = [];
	resourceIds.push(
		{
			type : "test",
			id : "testId"
		}
	);
	
	
	var resourceService = nosliw.runtime.getResourceService();
	resourceService.executeGetResourcesRequest(resourceIds, {
		success : function(){
			nosliw.loggine.info("Ha Ha Ha Ha!!!!!");
		}
	}, undefined);
