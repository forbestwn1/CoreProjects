package com.nosliw.core.application.brick.service.provider;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPRequestParmInInteractiveInterface;
import com.nosliw.core.application.common.interactive1.HAPValuePortInteractiveRequest;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;

public class HAPValuePortServiceRequest extends HAPValuePortInteractiveRequest{

	public HAPValuePortServiceRequest(String entityId, List<HAPRequestParmInInteractiveInterface> requestParms) {
		super(new HAPIdValuePort(entityId, HAPConstantShared.VALUEPORT_TYPE_SERVICE_REQUEST, HAPConstantShared.NAME_DEFAULT), new HAPInfoValuePort(), requestParms);
	}

}
