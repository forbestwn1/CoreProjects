package com.nosliw.data.core.cronjob;

import java.util.List;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerCronJob  extends HAPResourceManagerImp{

	private HAPManagerCronJob m_cronJobMan;
	
	public HAPResourceManagerCronJob(HAPManagerCronJob cronJobMan, HAPManagerResource rootResourceMan){
		super(rootResourceMan);
		this.m_cronJobMan = cronJobMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableCronJob cronJob = m_cronJobMan.getCronJob(resourceId);
		if(cronJob==null)  return null;
		return new HAPResource(resourceId, cronJob.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		return null;
	}
}
