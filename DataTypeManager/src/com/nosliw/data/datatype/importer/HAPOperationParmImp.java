package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPOperationParmInfo;
import com.nosliw.data.HAPDataTypeCriteria;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPOperationParmImp extends HAPStringableValueEntity implements HAPOperationParmInfo{

	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	@Override
	public HAPDataTypeCriteria getCriteria() {		return (HAPDataTypeCriteria)this.getAtomicValueAncestorByPath(CRITERIA);	}

}
