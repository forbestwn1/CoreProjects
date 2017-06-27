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
		name : "base",
		value : {
			dataTypeId : "base.string;1.0.0",
			value : "Hello World"
		} 
	};
	var parm2 = {
		name : "from",
		value : {
			dataTypeId : "base.integer;1.0.0",
			value : 1
		} 
	};
	var parm3 = {
		name : "to",
		value : {
			dataTypeId : "base.integer;1.0.0",
			value : 5
		} 
	};
	parms.push(parm1);
	parms.push(parm2);
	parms.push(parm3);
	
	
	expressionService.executeExecuteOperationRequest("base.string;1.0.0", "subString", parms, {
		success : function(requestInfo, data){
			nosliw.logging.info("Operation Result : ", data);
		}
	}, undefined)
	