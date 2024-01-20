package com.nosliw.data.core.domain.valuecontext;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;

public class HAPUtilityValuePort {

	public static HAPIdValuePort createValuePortIdValueContext(HAPExecutableEntity complexEntity) {
		return new HAPIdValuePort(complexEntity.getId(), HAPConstantShared.VALUEPORT_TYPE_VALUECONTEXT, HAPConstantShared.NAME_DEFAULT);
		
	}
	
}
