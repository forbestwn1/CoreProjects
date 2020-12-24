package com.nosliw.data.core.data.variable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPDataRuleJsScript extends HAPDataRuleImp{

	public HAPDataRuleJsScript() {
		super(HAPConstant.DATARULE_TYPE_JSSCRIPT);
	}

	@Override
	public HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv) {
		// TODO Auto-generated method stub
		return null;
	}

}
