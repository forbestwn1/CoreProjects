package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataTypeCriteria;

public class HAPDataTypeCriteriaImp extends HAPStringableValueEntity implements HAPDataTypeCriteria{

	@Override
	public String getCriteria() {
		return this.getAtomicAncestorValueString(HAPDataTypeCriteria.CRITERIA);
	}

}
