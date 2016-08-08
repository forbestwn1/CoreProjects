/*
 * data operation request utility
 */
var NosliwOperationRequestManager = function(){

	var manager = {
		
		getRequestInfoNewEntity : function(entitytype, parms, handler, requester, options){
			var requestInfo = new NosliwOperationRequestInfo(requester, handler);
			requestInfo.addRequestOperation($.extend({
				'operation' : 'newEntity',
				'data' : entitytype,
				'parms' : parms,
			},options));
			requestInfo.setRequestExecuteInfo({
				method : this.requestInfoNewEntity,
				context : this,
			});
			requestInfo.setMetaData('entitytype', entitytype);
			return requestInfo;
		},	
			
		requestNewEntity : function(entitytype, parms, handler, requester, options){
			var requestInfo = this.getRequestInfoNewEntity(entitytype, parms, handler, requester, options);
			return this.requestInfoNewEntity(requestInfo);
		},
		
		requestInfoNewEntity : function(requestInfo){
			NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(requestInfo);
		},
		

		getRequestInfoDeleteEntity : function(ID, handler, requester, options){
			var requestInfo = new NosliwOperationRequestInfo(requester, handler);
			requestInfo.addRequestOperation($.extend({
				'operation' : 'deleteEntity',
				'data' : ID,
			},options));
			requestInfo.setRequestExecuteInfo({
				method : this.requestInfoDeleteEntity,
				context : this,
			});
			requestInfo.setMetaData('ID', ID);
			return requestInfo;
		},	
			
		requestDeleteEntity : function(ID, handler, requester, options){
			var requestInfo = this.getRequestInfoDeleteEntity(ID, handler, requester, options);
			return this.requestInfoDeleteEntity(requestInfo);
		},
		
		requestInfoDeleteEntity : function(requestInfo){
			NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(requestInfo);
		},
		
		
		getRequestInfoStartTransaction : function(handler, requester){
			var requestInfo = new NosliwOperationRequestInfo(requester, handler);
			requestInfo.addRequestOperation({
				'operation' : 'startTransaction',
			});
			requestInfo.setRequestExecuteInfo({
				method : this.requestInfoStartTransaction,
				context : this,
			});
			return requestInfo;
		},	
			
		requestStartTransaction : function(handler, requester){
			var requestInfo = this.getRequestInfoStartTransaction(handler, requester);
			return this.requestInfoStartTransaction(requestInfo);
		},
		
		requestInfoStartTransaction : function(requestInfo){
			NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(requestInfo);
		},
		
		getRequestInfoCommit : function(handler, requester){
			var requestInfo = new NosliwOperationRequestInfo(requester, handler);
			requestInfo.addRequestOperation({
				'operation' : 'commit',
			});
			requestInfo.setRequestExecuteInfo({
				method : this.requestInfoCommit,
				context : this,
			});
			return requestInfo;
		},	
			
		requestCommit : function(handler, requester){
			var requestInfo = this.getRequestInfoCommit(handler, requester);
			return this.requestInfoCommit(requestInfo);
		},
		
		requestInfoCommit : function(requestInfo){
			NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(requestInfo);
		},
		
		getRequestInfoRollback : function(handler, requester){
			var requestInfo = new NosliwOperationRequestInfo(requester, handler);
			requestInfo.addRequestOperation({
				'operation' : 'rollback',
			});
			requestInfo.setRequestExecuteInfo({
				method : this.requestInfoRollback,
				context : this,
			});
			return requestInfo;
		},	
			
		requestRollback : function(handler, requester){
			var requestInfo = this.getRequestInfoRollback(handler, requester);
			return this.requestInfoRollback(requestInfo);
		},
		
		requestInfoRollback : function(requestInfo){
			NosliwEntityOperationRequestManager.requestOperateEntityContextPathValue(requestInfo);
		},

		
		query : function(query){
		},	
		
		operate : function(operation){
			serviceCall("operate", {"operation":operation});
			return successData;
		},
		
		entityOperate : function(request){
			serviceCall("operateEntity", {"operations":request.entityOperations, 'requestId':request.id});
			return successData;
		},
	};
	return manager;
	
}();
