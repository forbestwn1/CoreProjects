package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPRuntimeTaskLoadResources;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

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
				HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildRequestScriptForLoadResourceTask(this);
				rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
			}
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
		}
		return null;
	}
}
