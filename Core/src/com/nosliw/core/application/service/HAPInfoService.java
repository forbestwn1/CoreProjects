package com.nosliw.core.application.service;

import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;

public class HAPInfoService {

	public static final String RUNTIME = "runtime";

	public static final String PROFILE = "profile";

	private HAPInfoServiceRuntime m_serviceRuntime;
	
	private HAPBlockServiceProfile m_serviceProfile;
	
	
	public HAPInfoService(HAPBlockServiceProfile serviceProfile, HAPInfoServiceRuntime serviceRuntime) {
		this.m_serviceProfile = serviceProfile;
		this.m_serviceRuntime = serviceRuntime;
	}
	
	public HAPInfoServiceRuntime getServiceRuntimeInfo() {  	return this.m_serviceRuntime;	}
	
	public HAPBlockServiceProfile getServiceProfileInfo() {    return this.m_serviceProfile;     }

}
