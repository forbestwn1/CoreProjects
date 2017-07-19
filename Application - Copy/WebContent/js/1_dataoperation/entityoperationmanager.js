/*
 * process data operation request
 */
var NosliwEntityOperationRequestManager = function(){
	
	var m_tasks = [];
	
	function m_createRequestHandler(requestInfo){
		requestInfo.setMetaHandler(mergeHandler(requestInfo.handler, {
			success : createRequestProcessorHandlerFunction(requestInfo.handler.success, function(results, reqInfo){
				Nosliw.setTransactionId(results.transactionId);
				NosliwOperationManager.resultOperations(results, reqInfo);
				NosliwErrorManager.clearErrorMessage(reqInfo);
				return results;
			}), 	
			error : createRequestProcessorHandlerFunction(requestInfo.handler.error, function(serviceData, reqInfo){
//				NosliwErrorManager.createErrorMessage(requestInfo, serviceData);
				return serviceData;
			}),
		}));
	};
	
	function m_tryLocalOperate(task){
		var serviceData = NosliwErrorManager.createErrorServiceData();
		return serviceData;
	};
	
	function m_createRemoteTask(entityOperations, requestInfo){
		var task;
		if(entityOperations.length>1){
			task = NosliwServiceManager.getOperateEntityServiceTask(entityOperations, requestInfo);
		}
		else{
			var operation = entityOperations[0];
			var opName = operation.operation;
			if(opName==OPERATION_TRANSACTION_COMMIT){
				task = NosliwServiceManager.getCommitServiceTask(requestInfo);
			}
			else if(opName==OPERATION_TRANSACTION_ROLLBACK){
				task = NosliwServiceManager.getRollbackServiceTask(requestInfo);
			}
			else if(opName==OPERATION_TRANSACTION_START){
				task = NosliwServiceManager.getStartTransactionServiceTask(requestInfo);
			}
			else{
				task = NosliwServiceManager.getOperateServiceTask(operation, requestInfo);
			}
		}
		return task;
	};
	
	function m_createEntityOperationInfo(reqOperationInfo){
		var entityOp;
		var opName = reqOperationInfo.operation;
		if(opName=='newEntity'){
			entityOp = {
					operation : OPERATION_ENTITY_NEW,
					entityType : reqOperationInfo.data,
					isAutoCommit : reqOperationInfo.isAutoCommit,
					parms : reqOperationInfo.parms,
				};
		}
		else if(opName=='deleteEntity'){
			entityOp = {
					operation : OPERATION_ENTITY_DELETE,
					entityId : reqOperationInfo.data,
				};
		}
		else if(opName=='startTransaction'){
			entityOp = {
					operation : OPERATION_TRANSACTION_START,
				};
		}
		else if(opName=='commit'){
			entityOp = {
					operation : OPERATION_TRANSACTION_COMMIT,
				};
		}
		else if(opName=='rollback'){
			entityOp = {
					operation : OPERATION_TRANSACTION_ROLLBACK,
				};
		}
		else if(opName=='newElement'){
			var context = reqOperationInfo.context;
			var contextPath = reqOperationInfo.contextPath;

			var eleData = context[contextPath.name];
			var aPath = getAbsolutePathOfContextData(context, contextPath);
			var a = getRealEntityAttribute(eleData.ID, aPath);
			entityOp = {
					operation : OPERATION_ATTR_ELEMENT_NEW,
					entityId : a.ID,
					attributePath : a.path,
					parms : reqOperationInfo.parms,
				};
		}
		else if(opName=='set'){
			var context = reqOperationInfo.context;
			var contextPath = reqOperationInfo.contextPath;

			var eleData = context[contextPath.name];
			var aPath = getAbsolutePathOfContextData(context, contextPath);
			var a = getRealEntityAttribute(eleData.ID, aPath);
			entityOp = {
					operation : OPERATION_ATTR_ATOM_SET,
					entityId : a.ID,
					attributePath : a.path,
					data : reqOperationInfo.data,
//					entity : a.data,
				};
		}
		
		return entityOp;
	};

	function m_validateEntityOperation(entityOperation){
		return;
		var opName = entityOperation.operation;
		if(opName==OPERATION_ATTR_ATOM_SET){
			var wraper = entityOperation.entity;
			
			if(wraper.attrConfigure.validation=='true'){
				var rules = wraper.attrConfigure.rules;
				var isValid = true;
				var errorMsg = '';
				var rule;
				if(rules!=undefined){
					for(var i in rules){
						rule = rules[i];
						isValid = processExpression(rule.dataExpression, {'this':entityOperation.data});
						if(isValid==false){
							errorMsg = rule.errorMsg;
							break;
						}
					}
				}
				if(isValid==false){
					serviceData = NosliwErrorManager.createValidationFailureServiceData(wraper, rule, request.value);
				}
			}
		}
	};

	var m_manager = {
			
			getRequestInfoDataOperate : function(operation, handlers, requester){
				var reqInfo = new NosliwOperationRequestInfo(requester, handlers, operation);
				reqInfo.setRequestExecuteInfo(new NosliwServiceRequestExecuteInfo(this.requestInfoDataOperate, this));
				reqInfo.setMetaData('operation', operation);
				return reqInfo;
			},	
			
			requestDataOperate : function(operation, handlers, requester){
				var requestInfo = this.getRequestInfoDataOperate(operation, handlers, requester);
				return this.requestInfoDataOperate(requestInfo);
			},
			
			requestInfoDataOperate : function(requestInfo){
				m_createRequestHandler(requestInfo);
				
				var serviceData;
				var entityOperations = [];
				var reqOperationInfos = requestInfo.getRequestOperations();
				for(var i in reqOperationInfos){
					var reqOperationInfo = reqOperationInfos[i];
					var entityOperation = m_createEntityOperationInfo(reqOperationInfo);
					serviceData = m_validateEntityOperation(entityOperation);
					if(serviceData==undefined)   entityOperation.isValidation=true;
					else if(NosliwErrorManager.isFail(serviceData))   break;
					else entityOperation.isValidation=false;
					entityOperations.push(entityOperation);
				}
				
				if(NosliwErrorManager.isSuccess(serviceData)){
					var task = m_createRemoteTask(entityOperations, requestInfo);
					serviceData = m_tryLocalOperate(task);
					if(NosliwErrorManager.isSuccess(serviceData)){
						requestInfo.getMetaHandler().success.call(this, requestInfo, serviceData.data);
						task.clearHandler();
						NosliwRemoteServiceTaskManager.addServiceTask(task);
					}
					else{
						NosliwRemoteServiceTaskManager.addServiceTask(task);
					}
					
					
//					requestInfo.entityOperations = entityOperations;
//					serviceData = NosliwServiceManager.entityOperate(requestInfo);
				}
				else{
					requestInfo.getMetaHandler().error.call(this, requestInfo, serviceData);
				}
			},			
	};

	return m_manager;
}();
