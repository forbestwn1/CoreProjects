/**
 * 
 */
var nosliwServiceUtility = function(){
	var loc_out = {

			
			
			
			
			getGetUIResourceRemoteServiceTask : function(syncTaskName, requestInfo, name){
				//since is building remote service task, this request need remote call
				requestInfo.setIsLocalRequest(false);
				//create remote request handlers based on service request handlers 
				var handlers = nosliwRequestUtility.getRemoteServiceTaskHandlersFromRequestHandlers(requestInfo.getHandlers());
				return new NosliwRemoteServiceTask(syncTaskName, this.getGetUIResourceRemoteService(name), handlers, requestInfo);
			},
			getGetUIResourceRemoteService : function(name){
				return new NosliwServiceInfo(NOSLIWCOMMONCONSTANT.CONS_SERVICENAME_GETUIRESOURCE, {"name":name}); 
			},
			
			
			
			
			
			getGetAllEntityDefinitionsServiceTask : function(requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);
				
				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getGetAllEntityDefinitionsService(), handler, requestInfo);
			},
			getGetAllEntityDefinitionsService : function(){
				return {
					type : 'service',
					command : 'getAllEntityDefinitions',
					data : {},
				};
			},
			
			getGetAllDataTypesServiceTask : function(requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);

				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getGetAllDataTypesService(), handler, requestInfo);
			},
			getGetAllDataTypesService : function(){
				return {
					type : 'service',
					command : 'getAllDataTypes',
					data : {},
				};
			},
				
			getQueryServiceTask : function(query, requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);

				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getQueryService(query), handler, requestInfo);
			},
			getQueryService : function(query){
				return {
					type : 'service',
					command : 'query',
					data : {"query":query},
				};
			},
			
			getGetEntityWrapersServiceTask : function(ids, requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);

				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getGetEntityWrapersService(ids), handler, requestInfo);
			},
			getGetEntityWrapersService : function(ids){
				return {
					type : 'service',
					command : 'getEntityWrapers',
					data : {"IDs":ids},
				};
			},
			
			
			
	};
	return loc_out;
}();

