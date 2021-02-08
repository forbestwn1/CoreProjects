package com.nosliw.data.core.data.variable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPDataRuleMandatory extends HAPDataRuleImp{

	public HAPDataRuleMandatory() {
		super(HAPConstantShared.DATARULE_TYPE_MANDATORY);
	}

	@Override
	public HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

}
