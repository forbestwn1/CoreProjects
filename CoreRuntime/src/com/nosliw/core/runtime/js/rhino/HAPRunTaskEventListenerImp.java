package com.nosliw.core.runtime.js.rhino;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.core.runtime.execute.HAPRunTaskEventListener;
import com.nosliw.core.runtime.execute.HAPTaskRuntime;

public abstract class HAPRunTaskEventListenerImp  implements HAPRunTaskEventListener{

	private HAPTaskRuntime m_task;
	private HAPExecutorRuntimeImpRhino m_runtime;
	
	public HAPRunTaskEventListenerImp(HAPTaskRuntime task, HAPExecutorRuntimeImpRhino runtime){
		this.m_task = task;
		this.m_runtime = runtime;
	}
	
	@Override
	public void finish(HAPTaskRuntime task) {
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

	protected HAPTaskRuntime getTask() {    return this.m_task;   }
	
	protected HAPExecutorRuntimeImpRhino getRuntime() {    return this.m_runtime;     }
	
}
