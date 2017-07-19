var OPERATION_ATTR_ATOM_SET = "ATTR_ATOM_SET";
var OPERATION_ENTITY_NEW = 'ENTITY_NEW';
var OPERATION_ENTITY_DELETE = 'ENTITY_DELETE';
var OPERATION_TRANSACTION_START = 'TRANSACTION_START';
var OPERATION_TRANSACTION_COMMIT = 'TRANSACTION_COMMIT';
var OPERATION_TRANSACTION_ROLLBACK = 'TRANSACTION_ROLLBACK';

var OPERATION_QUERY_ENTITY_ADD = 'QUERY_ENTITY_ADD';
var OPERATION_QUERY_ENTITY_REMOVE = 'QUERY_ENTITY_REMOVE';
var OPERATION_QUERY_ENTITY_MODIFY = 'QUERY_ENTITY_MODIFY';

var OPERATION_ATTR_REFERENCE_SET = "ATTR_REFERENCE_SET";

var OPERATION_ATTR_CRITICAL_SET = "ATTR_CRITICAL_SET";
var OPERATION_ENTITYATTR_ADD = 'ENTITYATTR_ADD';
var OPERATION_ATTR_ELEMENT_NEW = "ATTR_ELEMENT_NEW";


var OPERATION_ATTR_REFERENCE_CLEAR = "30";
//var OPERATION_ATTR_ELEMENT_SET = 5;
var OPERATION_ATTR_ELEMENT_DELETE = "6";
var OPERATION_ENTITY_SYN = "46";
var OPERATION_ENTITYATTR_REMOVE = '12';
var OPERATION_CONTAINER_INDEX_INCREASE = '14';
var OPERATION_ENTITY_SETDEFINITION = '16';
var OPERATION_REFERENCE_ADD = '20';
var OPERATION_REFERENCE_REMOVE = '21';


var createOperationEventName = function(operation){
	var out;
	var opCode = operation.operation;
	switch(opCode)
	{
	case OPERATION_ATTR_CRITICAL_SET:
	case OPERATION_ATTR_ATOM_SET:
	case OPERATION_ATTR_REFERENCE_SET:
	case OPERATION_ENTITY_NEW:
	case OPERATION_ENTITY_DELETE:
	case OPERATION_QUERY_ENTITY_ADD:
	case OPERATION_QUERY_ENTITY_REMOVE:
	case OPERATION_QUERY_ENTITY_MODIFY:
	case OPERATION_ENTITYATTR_ADD:
		out = EVENT_DATA_CHANGE+':'+operation.attributePath;
		break;
	case OPERATION_ATTR_ELEMENT_NEW:
		out = EVENT_ELEMENT_ADD+':'+operation.attributePath;
		break;
	
	}
	return out;
};

var createOperationEventData = function(operation){
	return operation;
};

/*
 * handle operation result
 */

var NosliwOperationManager = function(){
	
	var m_triggerDataOperationEvent = function(entity, operation, request){
		entity.trigger(createOperationEventName(operation), createOperationEventData(operation), request);
	};
	
	var m_resultOperations = function(operationResults, requestInfo){
		for(var index in operationResults.results){
			m_operation(operationResults.results[index], requestInfo);
		}
	};
	
	var m_operation = function(operation, request){
		var opCode = operation.operation;
		switch(opCode)
		{
		case OPERATION_ATTR_CRITICAL_SET:
		case OPERATION_ATTR_ATOM_SET:
			m_setEntityAtomAttribute(operation, request);
			break;
		case OPERATION_ATTR_REFERENCE_SET:
			m_setEntityReferenceAttribute(operation, request);
			break;
		case OPERATION_ENTITY_NEW:
			m_newEntity(operation, request);
			break;
		case OPERATION_ENTITY_DELETE:
			m_deleteEntity(operation, request);
			break;
		case OPERATION_QUERY_ENTITY_ADD:
			m_addQueryEntity(operation.queryName, operation.entityId, operation.queryEntityWraper, request);
			break;
		case OPERATION_QUERY_ENTITY_REMOVE:
			m_removeQueryEntity(operation.queryName, operation.entityId, request);
			break;
		case OPERATION_QUERY_ENTITY_MODIFY:
			m_modifyQueryEntity(operation.queryName, operation.entityId, operation.queryEntityWraper, request);
			break;
		case OPERATION_ENTITYATTR_ADD:
			m_addEntityAttribute(operation, request);
			break;
		case OPERATION_ATTR_ELEMENT_NEW:
			m_addEntityElement(operation, request);
			break;
		}
	};
	
	var m_addEntityAttribute = function(operation, request){
		var entity = NosliwEntityManager.getEntity(operation.entityId);
		var attributeWraper = operation.extra;
		entity.data[attributeWraper.attrPath] = attributeWraper;
	};
	
	var m_setEntityAtomAttribute = function(operation, request){
		setEntityRealPathValue(operation.entityId, operation.attributePath, operation.data);
		var entity = NosliwEntityManager.getEntity(operation.entityId);
		m_triggerDataOperationEvent(entity, operation, request);
	};

	var m_setEntityReferenceAttribute = function(operation, request){
		setEntityRealPathValue(operation.entityId, operation.attributePath, operation.refEntityID);
		var entity = NosliwEntityManager.getEntity(operation.entityId);
		m_triggerDataOperationEvent(entity, operation, request);
	};
	
	var m_addEntityElement = function(operation, request){
		var entity = NosliwEntityManager.getEntity(operation.entityId);
		var container = getEntityAttributeWraperByPath(entity, operation.attributePath);
//		var ids = operation.extra.id.split(":");
//		var id = ids[ids.length-1];
		container.data[operation.extra.id] = operation.extra;

//		entity.trigger(m_createOperationEventName(operation), id, container[id]);
		m_triggerDataOperationEvent(entity, operation, request);
	};
	
	var m_addQueryEntity = function(query, ID, queryEntityWraper, request){
		NosliwQueryManager.addQueryEntity(query, ID, queryEntityWraper);
	};

	var m_removeQueryEntity = function(query, ID, request){
		NosliwQueryManager.removeQueryEntity(query, ID);
	};

	var m_modifyQueryEntity = function(query, ID, queryEntityWraper, request){
		NosliwQueryManager.modifyQueryEntity(query, ID, queryEntityWraper);
	};
	
	var m_newEntity = function(operation, request){
    	var entity = operation.extra;
    	var ID = entity.ID;
    	NosliwEntityManager.addEntity(ID, entity);
	};

	var m_deleteEntity = function(operation, request){
    	var ID = operation.entityId;
    	NosliwEntityManager.deleteEntity(ID);
	};
	
	var manager = {
			resultOperations : function(operationResults, requestId){
				m_resultOperations(operationResults, requestId);
			},			
			
			requestNewEntity : function(type, reqInfo){
				var operation = {
					operation : OPERATION_ENTITY_NEW,
					data : type,
					isAutoCommit : false,
				};
				var serviceData = NosliwServiceManager.operate(operation);
				setTransactionId(serviceData);

				m_resultOperations(serviceData.data);
			},
			
			
			requestSetAttributeAtom : function(request, reqInfo){
				var a = getRealEntityAttribute(request.ID, request.path, request.data);
				var operation = {
					operation : OPERATION_ATTR_ATOM_SET,
					entityId : a.ID,
					attributePath : a.path,
					data : request.value,
				};
				
				var serviceData;

				var wraper = getEntityAttributeWraperByPath(a.data, a.path);
				
				if(wraper.attrConfigure.validation=='true'){
					var rules = wraper.attrConfigure.rules;
					var isValid = true;
					var errorMsg = '';
					var rule;
					if(rules!=undefined){
						for(var i in rules){
							rule = rules[i];
							isValid = processExpression(rule.dataExpression, {'this':request.value});
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
				
				if(NosliwErrorManager.isSuccess(serviceData)){
					serviceData = NosliwServiceManager.operate(operation);
					m_resultOperations(serviceData.data);
				}
				
				if(NosliwErrorManager.isFail(serviceData)){
					NosliwErrorManager.createErrorMessage(reqInfo, serviceData);
				}
				else{
					NosliwErrorManager.clearErrorMessage(reqInfo);
				}
				return serviceData;
				
			},

			requestOperations : function(operations){
				for(var i in operations){
					
				}
			},
			

		startTransaction : function(){
			var serviceData = NosliwServiceManager.startTransaction();
			setTransactionId(serviceData);
			
		},
			
		commitTransaction : function(){
			var serviceData = NosliwServiceManager.commit();
			setTransactionId(serviceData);
			m_resultOperations(serviceData.data);
		},
			
		rollbackTransaction : function(){
			var serviceData = NosliwServiceManager.rollBack();
			setTransactionId(serviceData);
			m_resultOperations(serviceData.data);
		},
		
		newEntityElement : function(ID, attrPath){
			var operation = {
					operation : OPERATION_ATTR_ELEMENT_NEW,
					entityId : ID,
					attributePath : attrPath,
					isAutoCommit : false,
			};
			var serviceData = NosliwServiceManager.operate(operation);
			setTransactionId(serviceData);

			m_resultOperations(serviceData.data);
		},
			
		newEntity : function(type){
			var operation = {
				operation : OPERATION_ENTITY_NEW,
				data : type,
				isAutoCommit : false,
			};
			var serviceData = NosliwServiceManager.operate(operation);
			setTransactionId(serviceData);

			m_resultOperations(serviceData.data);
		},
			
		deleteEntity : function(ID){
			var operation = {
					operation : OPERATION_ENTITY_DELETE,
					entityId : ID,
					isAutoCommit : false,
			};
			var serviceData = NosliwServiceManager.operate(operation);
			setTransactionId(serviceData);

			m_resultOperations(serviceData.data);
		},
		
		deleteContainerElement : function(ID, path, id, data){
			var operation = {
				operation : OPERATION_ATTR_ELEMENT_DELETE,
				entityId : ID,
				attributePath : path,
				data : id,
			};
			NosliwServiceManager.operate(operation);
				
			if(data==undefined){
				data = NosliwEntityManager.getEntity(ID);
			}

			data.trigger('eleremove:'+path, id);
			
			var container = getProperty(data, path);
			delete container[id];
		},
	};
	return manager;
}();
