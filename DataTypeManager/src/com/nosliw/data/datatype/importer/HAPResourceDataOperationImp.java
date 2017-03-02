package com.nosliw.data.datatype.importer;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeCriteria;
import com.nosliw.data.core.HAPResourceDataOperation;

public class HAPResourceDataOperationImp extends HAPStringableValueEntity implements HAPResourceDataOperation{

	public HAPResourceDataOperationImp(String dataTypeCriteria, String operation){
		this.setDataTypeCriteriaLiterate(dataTypeCriteria);
		this.setOperationLiterate(operation);
	}

	@Override
	public HAPDataTypeCriteria getDataTypeCriteria() {
		return (HAPDataTypeCriteria)this.getAtomicValueAncestorByPath(DATATYPECRITERIA);
	}

	@Override
	public String getOperation() {
		return this.getAtomicAncestorValueString(OPERATION);
	}

	@Override
	protected String buildLiterate(){
		String criteriaPart = this.getDataTypeCriteria().toStringValue(HAPSerializationFormat.LITERATE);
		String operationPart = this.getOperation();
		return HAPNamingConversionUtility.cascadePart(criteriaPart, operationPart);
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] parts = HAPNamingConversionUtility.parseSegments(literateValue);
		this.setDataTypeCriteriaLiterate(parts[0]);
		this.setOperationLiterate(parts[1]);
		return true;
	}
	
	private void setDataTypeCriteriaLiterate(String dataTypeCriteriaLiterate){
		this.updateAtomicChildStrValue(DATATYPECRITERIA, dataTypeCriteriaLiterate, HAPConstant.STRINGABLE_ATOMICVALUETYPE_OBJECT, HAPDataTypeCriteriaImp.class.getName());
	}
	
	private void setOperationLiterate(String operationLiterate){
		this.updateAtomicChildStrValue(OPERATION, operationLiterate, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING, null);
	}

}
