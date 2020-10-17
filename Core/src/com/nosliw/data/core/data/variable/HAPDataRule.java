package com.nosliw.data.core.data.variable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.data.HAPData;

public interface HAPDataRule {

	//apply the rule and verify if the data is valid
	HAPServiceData verify(HAPData data);
	
}
