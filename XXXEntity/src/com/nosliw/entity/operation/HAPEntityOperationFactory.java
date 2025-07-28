package com.nosliw.entity.operation;

import com.nosliw.data.HAPData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryEntityWraper;

public class HAPEntityOperationFactory {

	static public HAPEntityOperationInfo getReverseOperation(HAPEntityOperationInfo operationInfo){
		HAPEntityOperationInfo out = null;
		switch(operationInfo.getOperation()){
			case ENTITYOPERATION_ENTITY_NEW:
			{
//				HAPEntityWraper entity = (HAPEntityWraper)operationInfo.getWraper(); 
				out = HAPEntityOperationFactory.createEntityDeleteOperation(operationInfo.getEntityID());
				break;
			}
			case ENTITYOPERATION_ATTR_ATOM_SET:
			{
				HAPData extra = (HAPData)operationInfo.getExtra(); 
				out = HAPEntityOperationFactory.createAttributeAtomSetOperationByData(operationInfo.getEntityID(), operationInfo.getAttributePath(), extra);
				break;
			}
			default:
				break;
		}
		return out;
	}
	
	static public HAPEntityOperationInfo createEntityAttributeAddOperation(HAPEntityID entityID, HAPAttributeDefinition attrDef){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITYATTR_ADD);
		out.setEntityID(entityID);
		out.setAttributeDefinition(attrDef);
		return out;
	}

	static public HAPEntityOperationInfo createTransactionStartOperation(String transactionId){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_TRANSACTION_START);
		out.setTransactionId(transactionId);
		return out;
	}

	static public HAPEntityOperationInfo createTransactionCommitOperation(String transactionId){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_TRANSACTION_COMMIT);
		out.setTransactionId(transactionId);
		return out;
	}

	static public HAPEntityOperationInfo createTransactionRollbackOperation(String transactionId){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_TRANSACTION_ROLLBACK);
		out.setTransactionId(transactionId);
		return out;
	}
	
	static public HAPEntityOperationInfo createEntityNewOperation(String entityType, String id, boolean autoCommit){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITY_NEW);
		out.setEntityID(new HAPEntityID(null, entityType, id));
		out.setEntityType(entityType);
		out.setIsAutoCommit(autoCommit);
		return out;
	}

	static public HAPEntityOperationInfo createContainerElementNewOperation(HAPEntityID entityID, String attrPath, String id, boolean autoCommit){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_ELEMENT_NEW);
		out.setEntityID(entityID);
		out.setAttributePath(attrPath);
		out.setElementId(id);
		out.setIsAutoCommit(autoCommit);
		return out;
	}
	
	static public HAPEntityOperationInfo createEntityDeleteOperation(HAPEntityID entityID){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITY_DELETE);
		out.setEntityID(entityID);
		return out;
	}
	
	static public HAPEntityOperationInfo createAttributeAtomSetOperationByString(HAPEntityID entityID, String attrPath, String value){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setEntityID(entityID);
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_ATOM_SET);
		out.setValue(value);
		out.setAttributePath(attrPath);
		return out;
	}

	static public HAPEntityOperationInfo createAttributeAtomSetOperationByData(HAPEntityID entityID, String attrPath, HAPData data){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setEntityID(entityID);
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_ATOM_SET);
		out.setData(data);
		out.setAttributePath(attrPath);
		return out;
	}
	
	static public HAPEntityOperationInfo createAttributeReferenceSetOperation(HAPEntityID entityID, String attrPath, HAPEntityID refID){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setEntityID(entityID);
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_REFERENCE_SET);
		out.setRefEntityID(refID);
		out.setAttributePath(attrPath);
		return out;
	}

	static public HAPEntityOperationInfo createQueryUpdateOperation(HAPQueryComponent queryComponent){
		return null;
	}

	static public HAPEntityOperationInfo createQueryDeleteOperation(String query){
		return null;
	}
	
	static public HAPEntityOperationInfo createQueryAddEntityOperation(String query, int position, HAPQueryEntityWraper queryEntityWraper){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_QUERY_ENTITY_ADD);
		out.setIsSubmitable(false);
		out.setQueryPosition(position);
		out.setQueryName(query);
		out.setQueryEntityWraper(queryEntityWraper);
		return out;
	}
	
	static public HAPEntityOperationInfo createQueryRemoveEntityOperation(String query, String queryEntityId){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_QUERY_ENTITY_REMOVE);
		out.setIsSubmitable(false);
		out.setQueryEntityId(queryEntityId);
		out.setQueryName(query);
		return out;
	}

	static public HAPEntityOperationInfo createQueryModifyEntityOperation(String query, String id, int oldIndex, int newIndex, HAPQueryEntityWraper queryEntityWraper){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_QUERY_ENTITY_MODIFY);
		out.setIsSubmitable(false);
		out.setQueryName(query);
		out.setQueryEntityWraper(queryEntityWraper);
		return out;
	}
	
	static public HAPEntityOperationInfo createEntityAttributeRemoveOperation(HAPEntityID entityID, String attr){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITYATTR_REMOVE);
		out.setEntityID(entityID);
		out.setAttributePath(attr);
		return out;
	}
	
	static public HAPEntityOperationInfo createReferenceAddOperation(HAPEntityID entityID, HAPReferenceInfoAbsolute referencePath){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_REFERENCE_ADD);
		out.setEntityID(entityID);
		out.setReferencePath(referencePath);
		return out;
	}

	static public HAPEntityOperationInfo createReferenceRemoveOperation(HAPEntityID entityID, HAPReferenceInfoAbsolute referencePath){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_REFERENCE_REMOVE);
		out.setEntityID(entityID);
		out.setReferencePath(referencePath);
		return out;
	}
	
	static public HAPEntityOperationInfo createAttributeReferenceClearOperation(HAPEntityID entityID, String attrPath){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setEntityID(entityID);
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_REFERENCE_CLEAR);
		out.setAttributePath(attrPath);
		return out;
	}
	
	
	
/*	
	static public HAPEntityOperationInfo createEntitySetDefinitionOperation(HAPEntityID entityID, HAPEntityDefinitionCritical entityDef){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITY_SETDEFINITION);
		out.setEntityID(entityID);
		out.setEntityDefinition(entityDef);
		return out;
	}

	static public HAPEntityOperationInfo createEntitySynOperation(HAPEntityWraper entityWraper){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ENTITY_SYN);
		out.setEntityID(entityWraper.getID());
		out.setWraper(entityWraper);
		return out;
	}
	
	static public HAPEntityOperationInfo createAttributeElementDeleteOperation(HAPEntityID entityID, String attrPath, String elementId){
		HAPEntityOperationInfo out = new HAPEntityOperationInfo();
		out.setOperation(HAPEntityOperation.ENTITYOPERATION_ATTR_ELEMENT_DELETE);
		out.setEntityID(entityID);
		out.setAttributePath(attrPath);
		out.setElementId(elementId);
		return out;
	}
*/	

//	static public HAPOperationInfo createEntityRemoveAttributeOperation(HAPEntityID entityID, String attrPath){
//	HAPOperationInfo out = new HAPOperationInfo();
//	out.setEntityID(entityID);
//	out.setOperation(HAPOperation.OPERATION_ENTITYATTR_REMOVE);
//	out.setAttributePath(attrPath);
//	return out;
//}

//static public HAPOperationInfo createEntityAddAttributeOperation(HAPEntityID entityID, String attrPath){
//	HAPOperationInfo out = new HAPOperationInfo();
//	out.setEntityID(entityID);
//	out.setOperation(HAPOperation.OPERATION_ENTITYATTR_ADD);
//	out.setAttributePath(attrPath);
//	return out;
//}


}
