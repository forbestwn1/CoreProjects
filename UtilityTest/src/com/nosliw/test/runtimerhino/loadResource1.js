	var resourceIds = [];
	resourceIds.push(
		{
			type : "operation",
			id : "test.url;1.1.0;url_normal3"
		}
//		{
//			type : "operation",
//			id : "test.string;1.0.0;subString"
//		}

	);
	
//	var resourceService = nosliw.runtime.getResourceService();
//	resourceService.executeGetResourcesRequest(resourceIds, {
//		success : function(requestInfo, resourcesTree){
//			var resourceManager = nosliw.runtime.getResourceManager();
//			nosliw.logging.info("Get resource : ", resourcesTree);
//		}
//	}, undefined);

	
	var expressionService = nosliw.runtime.getExpressionService();
	var parms = [];
	var parm1 = {
		value : {
			dataTypeId : "test.string;1.0.0",
			value : "Hello World"
		} 
	};
	var parm2 = {
		name : "from",
		value : {
			dataTypeId : "test.integer;1.0.0",
			value : 1
		} 
	};
	var parm3 = {
		name : "to",
		value : {
			dataTypeId : "test.integer;1.0.0",
			value : 5
		} 
	};
	parms.push(parm1);
	parms.push(parm2);
	parms.push(parm3);
	
	
//	expressionService.executeExecuteOperationRequest("test.string;1.0.0", "subString", parms, {
//		success : function(requestInfo, data){
//			nosliw.logging.info("Operation Result : ", data);
//		}
//	}, undefined);
	