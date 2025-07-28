package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.resource.HAPRuntimeTaskLoadResources;
import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskLoadResourcesRhino extends HAPRuntimeTaskLoadResources{

	public HAPRuntimeTaskLoadResourcesRhino(List<HAPResourceInfo> resourcesInfo) {
		super(resourcesInfo);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		try{
			if(this.getResourcesInfo()==null || this.getResourcesInfo().isEmpty()){
				//if no resource required
				this.finish(HAPServiceData.createSuccessData());
			}
			else{
				HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
				HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForLoadResourceTask(this, rhinoRuntime);
				rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
			}
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
		}
		return null;
	}
}
