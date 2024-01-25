package com.nosliw.data.core.domain.entity.service.provider;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPInfoValuePort;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveRequestParm;
import com.nosliw.data.core.interactive.HAPValuePortInteractiveRequest;

public class HAPValuePortServiceRequest extends HAPValuePortInteractiveRequest{

	public HAPValuePortServiceRequest(String entityId, List<HAPDefinitionInteractiveRequestParm> requestParms) {
		super(new HAPIdValuePort(entityId, HAPConstantShared.VALUEPORT_TYPE_SERVICE_REQUEST, HAPConstantShared.NAME_DEFAULT), new HAPInfoValuePort(), requestParms);
	}

}
