var nosliwServiceManager = function(){
	var manager = {

			
			
			
			
			
		
		
		
		
		getServiceTaskByRequest : function(requestInfo){
			var service = requestInfo.service;
			var serviceRequest = {
					command : service.command,
					data : service.data,
					handler : requestInfo.handler,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},	
		
		invokeServieRequest : function(requestInfo){
			var request = getServiceRequest(requestInfo);
			return NosliwRemoteServiceManager.addServiceRequest(request);
		},
		
		
		
		
		
		
/****************************     Get Information    **********************************/
		getGetEntityWrapersRequest : function(ids, handler, requestInfo){
			var serviceRequest = {
					command : "getEntityWrapers",
					data : {"IDs":ids},
					success : handler,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},

		getEntityWrapers : function(ids){
			var request = this.getGetEntityWrapersRequest(ids, handler, requestInfo);
			return NosliwRemoteServiceManager.addServiceCall(request);
		},

		
		
		getGetAllDataTypesRequest : function(requestInfo){
			var serviceRequest = {
					command : "getAllDataTypes",
					data : {},
					success : requestInfo.success,
					error : requestInfo.error,
					exception : requestInfo.exception,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},
		
		getAllDataTypes : function(handler, requestInfo){
			var request = getAllDataTypesRequest(handler, requestInfo);
			return NosliwRemoteServiceManager.addServiceCall(request);
		},
			
		getGetAllEntityDefinitionsRequest : function(requestInfo){
			var serviceRequest = {
					command : "getAllEntityDefinitions",
					data : {},
					success : requestInfo.success,
					error : requestInfo.error,
					exception : requestInfo.exception,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},

		getAllEntityDefinitions : function(){
			var request = getAllEntityDefinitionsRequest(handler, requestInfo);
			return NosliwRemoteServiceManager.addServiceCall(request);
		},
		
		getGetUIResourceRequest : function(name, handler, requestInfo){
			var serviceRequest = {
					command : "getUIResource",
					data : {"name":name},
					success : handler,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},

		getUIResource : function(name){
			var request = getGetUIResourceRequest(name, handler, requestInfo);
			return NosliwRemoteServiceManager.addServiceCall(request);
		},

		getGetAttributeOptionsRequest : function(ID, attrPath, handler, requestInfo){
			var serviceRequest = {
					command : "getAttributeOptions",
					data : {"ID":ID, "attrPath":attrPath},
					success : handler,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},

		getAttributeOptions : function(ID, attrPath){
//			var request = getGetAttributeOptionsRequest(ID, attrPath, handler, requestInfo);
//			return NosliwRemoteServiceManager.addServiceCall(request);
		},

		getEntityDefinitionsByGroup : function(id){
		},

		/****************************     Query    **********************************/
		getQueryRequest : function(query, handler, requestInfo){
			var serviceRequest = {
					command : "query",
					data : {"query":query},
					handler : handler,
					requestInfo : requestInfo,
			};
			return serviceRequest;
		},
		query : function(query, requestInfo){
			var request = getQueryRequest(handler, requestInfo);
			return NosliwRemoteServiceManager.addServiceCall(request);
		},	
		
	};
	return manager;
	
}();
