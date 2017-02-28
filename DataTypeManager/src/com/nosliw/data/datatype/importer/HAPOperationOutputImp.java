package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPDataTypeCriteria;
import com.nosliw.data.core.HAPOperationOutInfo;

public class HAPOperationOutputImp extends HAPStringableValueEntity implements HAPOperationOutInfo{

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);	}

	@Override
	public HAPDataTypeCriteria getCriteria() {		return (HAPDataTypeCriteria)this.getAtomicValueAncestorByPath(CRITERIA);	}

}
