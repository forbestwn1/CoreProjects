/**
 * remote request utility
 */
var nosliwEntityOperationUtility = function(){
	var loc_out = {
		
			getStartTransactionServiceTask : function(requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);
					
				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getStartTransactionService(), handler, requestInfo);
		},
		getStartTransactionService : function(){
			return {
				type : 'service',
				command:'startTransaction', 
			};
		},

			
		getCommitServiceTask : function(requestInfo, taskHandler){
				requestInfo.setIsLocalRequest(false);
					
				var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
				var handler = mergeHandler(metaHandler, taskHandler);
				return new NosliwRemoteServiceTask(this.getCommitService(), handler, requestInfo);
		},
		getCommitService : function(){
			return {
				type : 'service',
				command:'commit', 
			};
		},

		getRollbackServiceTask : function(requestInfo, taskHandler){
			requestInfo.setIsLocalRequest(false);
				
			var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
			var handler = mergeHandler(metaHandler, taskHandler);
			return new NosliwRemoteServiceTask(this.getRollbackService(), handler, requestInfo);
		},
		getRollbackService : function(){
			return {
				type : 'service',
				command:'rollback', 
			};
		},

		
		getOperateServiceTask : function(entityOperations, requestInfo, taskHandler){
			requestInfo.setIsLocalRequest(false);
				
			var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
			var handler = mergeHandler(metaHandler, taskHandler);
			return new NosliwRemoteServiceTask(this.getOperateService(entityOperations), handler, requestInfo);
		},
		getOperateService : function(operation){
			return {
				type : 'service',
				command:'operate', 
				data:{'operation':operation},
			};
		},
			
			
		getOperateEntityServiceTask : function(entityOperations, requestInfo, taskHandler){
			requestInfo.setIsLocalRequest(false);
			
			var metaHandler = getServiceTaskHandlerFromRequestHandler(requestInfo.getMetaHandler());
			var handler = mergeHandler(metaHandler, taskHandler);
			return new NosliwRemoteServiceTask(this.getOperateEntityService(entityOperations), handler, requestInfo);
		},
		getOperateEntityService : function(entityOperations){
			return {
				type : 'service',
				command:'operateEntity', 
				data:{'operations':entityOperations},
			};
		},
			
			
			
	};
	return loc_out;
}();

