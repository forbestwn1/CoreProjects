package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPRelationshipPath;
import com.nosliw.data.HAPRelationshipPathSegment;
import com.nosliw.data.HAPOperation;

public class HAPDataTypeOperationImp extends HAPOperationImp implements HAPRelationship, HAPDataTypeOperation{

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TARGETDATATYPENAME = "targetDataTypeName";

	public HAPDataTypeOperationImp(HAPOperationImp targetOperation, HAPRelationshipImp relationship){
		this.cloneFrom(targetOperation);
		this.updateAtomicChildObjectValue(TARGETDATATYPENAME, relationship.getTargetDataTypeName());
		this.updateAtomicChildObjectValue(PATH, relationship.getPath());
		this.setOperationId(targetOperation.getId());
	}
	
	public HAPDataTypeOperationImp(HAPOperationImp operation){
		
	}
	
	@Override
	public HAPOperation getOperationInfo() {  return this;  }

	@Override
	public HAPRelationship getTargetDataType() {		return this;	}

	@Override
	public HAPDataTypeId getTargetDataTypeName() {	return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(TARGETDATATYPENAME, HAPDataTypeIdImp.class);	}
	
	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public String getOperationId(){ return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String opId){ this.updateAtomicChildStrValue(OPERATIONID, opId);  }
	
	public HAPDataTypeOperationImp extendPathSegment(HAPRelationshipPathSegment segment, HAPDataTypeIdImp sourceId){
		HAPDataTypeOperationImp out = this.clone(HAPDataTypeOperationImp.class);
		out.getPath().insert(segment);
		out.setDataTypeName(sourceId);
		return out;
	}
}
