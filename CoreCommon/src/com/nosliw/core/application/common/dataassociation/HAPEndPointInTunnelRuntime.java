package com.nosliw.core.application.common.dataassociation;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPEndPointInTunnelRuntime extends HAPEndpointInTunnel{

	private List<HAPInfoElementRuntime> m_element;
	
	private String m_handler;
	
	public HAPEndPointInTunnelRuntime() {
		super(HAPConstantShared.TUNNELENDPOINT_TYPE_RUNTIME);
	}

}
