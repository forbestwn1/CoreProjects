package com.nosliw.core.application.entity.datarule;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPDataRule extends HAPSerializable{

	@HAPAttribute
	public static String RULETYPE = "ruleType";

	String getRuleType();
	
	HAPDataRuleImplementation getImplementation();
	
}
