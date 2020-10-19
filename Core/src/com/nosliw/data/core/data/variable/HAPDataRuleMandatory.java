package com.nosliw.data.core.data.variable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPData;

public class HAPDataRuleMandatory extends HAPDataRuleImp{

	public HAPDataRuleMandatory() {
		super(HAPConstant.DATARULE_TYPE_MANDATORY);
	}

	@Override
	public HAPServiceData verify(HAPData data) {
		// TODO Auto-generated method stub
		return null;
	}

}
