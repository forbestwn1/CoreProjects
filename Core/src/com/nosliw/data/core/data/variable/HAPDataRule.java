package com.nosliw.data.core.data.variable;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public interface HAPDataRule extends HAPSerializable{

	@HAPAttribute
	public static String RULETYPE = "ruleType";

	String getRuleType();
	
	//apply the rule and verify if the data is valid
	HAPServiceData verify(HAPData data, HAPRuntimeEnvironment runtimeEnv);
	
}
