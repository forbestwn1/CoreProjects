package com.nosliw.core.application.common.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPEndPointInTunnelRuntime extends HAPEndpointInTunnel{

	private HAPIdBrickInBundle m_brickId;
	
	public HAPEndPointInTunnelRuntime() {
		super(HAPConstantShared.TUNNELENDPOINT_TYPE_RUNTIME);
	}

}
