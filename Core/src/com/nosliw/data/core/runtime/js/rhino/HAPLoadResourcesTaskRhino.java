package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import com.nosliw.data.core.runtime.HAPLoadResourcesTask;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPLoadResourcesTaskRhino extends HAPLoadResourcesTask{

	public HAPLoadResourcesTaskRhino(List<HAPResourceInfo> resourcesInfo) {
		super(resourcesInfo);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
		
		HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildRequestScriptForLoadResourceTask(this);
		rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
		return null;
	}
}
