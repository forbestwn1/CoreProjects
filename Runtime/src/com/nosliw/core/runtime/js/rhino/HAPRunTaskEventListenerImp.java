package com.nosliw.core.runtime.js.rhino;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.core.runtime.HAPRunTaskEventListener;
import com.nosliw.core.runtime.HAPRuntimeTask;

public abstract class HAPRunTaskEventListenerImp  implements HAPRunTaskEventListener{

	private HAPRuntimeTask m_task;
	private HAPRuntimeImpRhino m_runtime;
	
	public HAPRunTaskEventListenerImp(HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
		this.m_task = task;
		this.m_runtime = runtime;
	}
	
	@Override
	public void finish(HAPRuntimeTask task) {
		HAPServiceData resourceTaskResult = task.getResult();
		if(resourceTaskResult.isSuccess()){
			//after resource loaded, execute expression
			try{
				HAPJSScriptInfo scriptInfo = this.buildRuntimeScript();
				this.m_runtime.loadTaskScript(scriptInfo, m_task.getTaskId());
			}
			catch(Exception e){
				e.printStackTrace();
				this.m_task.finish(HAPServiceData.createFailureData(e, ""));
			}
		}
		else{
			this.m_task.finish(resourceTaskResult);
		}
	}

	abstract protected HAPJSScriptInfo buildRuntimeScript();

	protected HAPRuntimeTask getTask() {    return this.m_task;   }
	
	protected HAPRuntimeImpRhino getRuntime() {    return this.m_runtime;     }
	
}
