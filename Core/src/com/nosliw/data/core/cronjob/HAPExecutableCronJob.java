package com.nosliw.data.core.cronjob;

import java.util.List;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPExecutableCronJob extends HAPEntityInfoImpWrapper implements HAPExecutable{

	public HAPExecutableCronJob(HAPEntityInfo entityInfo) {
		super(entityInfo);
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		return null;
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}
}
