package com.nosliw.core.application.common.datarule;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;

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
