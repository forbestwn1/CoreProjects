package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteConverter;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;

public class HAPRuntimeTaskExecuteConverterRhino extends HAPRuntimeTaskExecuteConverter{

	public HAPRuntimeTaskExecuteConverterRhino(HAPData data, HAPMatchers matchers) {
		super(data, matchers);
	}

	@Override
	public Class getResultDataType() {	return HAPData.class;	}
	
	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			//prepare resources for data operation in the runtime (resource and dependency)
			//execute expression after load required resources
			List<HAPResourceInfo> resourcesId =	HAPExpressionUtility.discoverResourceRequirement(this.getMatchers(),rhinoRuntime.getRuntimeEnvironment().getResourceManager(), runtime.getRuntimeInfo());
			
			HAPRuntimeTask loadResourcesTask = new HAPRuntimeTaskLoadResourcesRhino(resourcesId);
			loadResourcesTask.registerListener(new HAPRunTaskEventListenerInner(this, rhinoRuntime));
			return loadResourcesTask;
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
			e.printStackTrace();
		}
		return null;
	}
	
	class HAPRunTaskEventListenerInner implements HAPRunTaskEventListener{
		private HAPRuntimeTaskExecuteConverterRhino m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteConverterRhino parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildRequestScriptForExecuteDataConvertTask(this.m_parent, this.m_runtime);
					this.m_runtime.loadTaskScript(scriptInfo, m_parent.getTaskId());
				}
				catch(Exception e){
					this.m_parent.finish(HAPServiceData.createFailureData(e, ""));
				}
			}
			else{
				this.m_parent.finish(resourceTaskResult);
			}
		}
	}
}
