package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPOperationOutputImp extends HAPStringableValueEntity implements HAPOperationOutInfo{

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);	}

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {		return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(DATATYPE);	}

}
