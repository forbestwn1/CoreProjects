package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPInfoValuePort;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveResult;
import com.nosliw.data.core.interactive.HAPValuePortInteractiveResult;

public class HAPValuePortServiceResult extends HAPValuePortInteractiveResult{

	public HAPValuePortServiceResult(String entityId, HAPDefinitionInteractiveResult result) {
		super(new HAPIdValuePort(entityId, HAPConstantShared.VALUEPORT_TYPE_SERVICE_RESULT, result.getName()), new HAPInfoValuePort(), result);
	}

}
