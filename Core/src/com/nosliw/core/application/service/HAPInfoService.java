package com.nosliw.core.application.service;

import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;

public class HAPInfoService {

	private HAPInfoServiceRuntime m_serviceRuntime;
	
	private HAPBrickServiceProfile m_serviceProfile;
	
	
	public HAPInfoServiceRuntime getServiceRuntimeInfo() {  	return this.m_serviceRuntime;	}
	
	public HAPBrickServiceProfile getServiceProfileInfo() {    return this.m_serviceProfile;     }

}
