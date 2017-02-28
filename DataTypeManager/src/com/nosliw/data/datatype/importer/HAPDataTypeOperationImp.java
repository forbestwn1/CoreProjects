package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataTypeOperation;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPRelationship;
import com.nosliw.data.core.HAPRelationshipPath;
import com.nosliw.data.core.HAPRelationshipPathSegment;

public class HAPDataTypeOperationImp extends HAPOperationImp implements HAPRelationship, HAPDataTypeOperation{

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TARGETDATATYPENAME = "targetDataTypeName";

	@HAPAttribute
	public static String SOURCEDATATYPENAME = "sourceDataTypeName";

	public HAPDataTypeOperationImp(){}
	
	public HAPDataTypeOperationImp(HAPOperationImp targetOperation, HAPRelationshipImp relationship){
		this.init(targetOperation, relationship);
	}
	
	public HAPDataTypeOperationImp(HAPOperationImp targetOperation){
		this.init(targetOperation, null);
	}
	
	private void init(HAPOperationImp targetOperation, HAPRelationshipImp relationship){
		this.cloneFrom(targetOperation);
		this.setOperationId(targetOperation.getId());
		this.setTargetDataTypeName(targetOperation.getDataTypeName());
		if(relationship!=null){
			this.setSourceDataTypeName(relationship.getSource());
			this.updateAtomicChildObjectValue(PATH, relationship.getPath());
		}
		else{
			this.setSourceDataTypeName(targetOperation.getDataTypeName());
			this.updateAtomicChildObjectValue(PATH, new HAPRelationshipPathImp());
		}
	}
	
	//methods from HAPDataTypeOperation
	@Override
	public HAPOperation getOperationInfo() {  return this;  }
	@Override
	public HAPRelationship getTargetDataType() {		return this;	}

	//methods from HAPRelationship
	@Override
	public HAPDataTypeId getTargetDataTypeName() {	return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(TARGETDATATYPENAME, HAPDataTypeIdImp.class);	}
	@Override
	public HAPRelationshipPath getPath() {		return (HAPRelationshipPathImp)this.getAtomicAncestorValueObject(PATH, HAPRelationshipPathImp.class);	}

	public void setTargetDataTypeName(HAPDataTypeIdImp dataTypeName){ this.updateAtomicChildObjectValue(TARGETDATATYPENAME, dataTypeName); }
	
	public HAPDataTypeIdImp getSourceDataTypeName(){   return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(SOURCEDATATYPENAME, HAPDataTypeIdImp.class);  }
	public void setSourceDataTypeName(HAPDataTypeIdImp dataTypeName){  this.updateAtomicChildObjectValue(SOURCEDATATYPENAME, dataTypeName);  }
	
	public String getOperationId(){ return this.getAtomicAncestorValueString(OPERATIONID);  }
	public void setOperationId(String opId){ this.updateAtomicChildStrValue(OPERATIONID, opId);  }
	
	public HAPDataTypeOperationImp extendPathSegment(HAPRelationshipPathSegment segment, HAPDataTypeIdImp sourceId){
		HAPDataTypeOperationImp out = this.clone(HAPDataTypeOperationImp.class);
		out.getPath().insert(segment);
		out.setDataTypeName(sourceId);
		return out;
	}
}
