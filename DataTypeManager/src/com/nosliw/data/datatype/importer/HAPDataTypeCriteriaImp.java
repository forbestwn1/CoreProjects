package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDataTypeCriteriaImp extends HAPStringableValueEntity implements HAPDataTypeCriteria{

	@Override
	public String getCriteria() {
		return this.getAtomicAncestorValueString(HAPDataTypeCriteria.CRITERIA);
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){	
		this.updateAtomicChildStrValue(CRITERIA, literateValue);
		return true;
	}

	@Override
	protected String buildLiterate(){
		return this.getCriteria();
	}
}
