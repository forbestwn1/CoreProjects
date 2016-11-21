package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataTypeCriteria;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPResourceDataOperation;

public class HAPResourceDataOperationImp extends HAPStringableValueEntity implements HAPResourceDataOperation{

	public HAPResourceDataOperationImp(String dataType, String version, String operation){
		
	}

	@Override
	public HAPDataTypeCriteria getDataTypeCriteria() {
		return null;
	}

	@Override
	public HAPOperationInfo getOperation() {
		return null;
	}

}
