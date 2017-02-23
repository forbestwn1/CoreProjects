package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.HAPDataTypeId;
import com.nosliw.data.HAPDataTypeOperation;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPRelationshipPath;
import com.nosliw.data.HAPOperation;

public class HAPDataTypeOperationImp extends HAPOperationImp implements HAPRelationship, HAPDataTypeOperation{

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TARGETDATATYPENAME = "targetDataTypeName";

	public HAPDataTypeOperationImp(HAPOperationImp op){
		
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
}
