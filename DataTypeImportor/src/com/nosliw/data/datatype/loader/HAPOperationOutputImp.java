package com.nosliw.data.datatype.loader;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataOperationOutInfo;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPOperationOutputImp extends HAPStringableValueEntity implements HAPDataOperationOutInfo{

	@Override
	public String getDescription() {		return this.getBasicAncestorValueString(DESCRIPTION);	}

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {		return (HAPDataTypeInfo)this.getEntityAncestorByPath(DATATYPE);	}

}
