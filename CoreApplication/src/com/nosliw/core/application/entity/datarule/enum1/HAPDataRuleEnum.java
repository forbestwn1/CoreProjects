package com.nosliw.core.application.entity.datarule.enum1;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

@HAPEntityWithAttribute
public abstract class HAPDataRuleEnum extends HAPDataRule{

	public HAPDataRuleEnum() {
		super(HAPConstantShared.DATARULE_TYPE_ENUM);
	}

}
