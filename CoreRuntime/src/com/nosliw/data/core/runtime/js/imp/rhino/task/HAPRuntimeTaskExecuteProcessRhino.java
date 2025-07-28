package com.nosliw.data.core.runtime.js.imp.rhino.task;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.application.common.structure.data.HAPContextData;
import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcess;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteProcessRhino extends HAPRuntimeTaskExecuteProcess{

	private HAPManagerResource m_resourceManager;

	public HAPRuntimeTaskExecuteProcessRhino(HAPExecutableProcess process, HAPContextData input, HAPManagerResource resourceManager) {
		super(process, input);
		this.m_resourceManager = resourceManager;
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			if(false) {
//			if(!HAPRuntime.isDemo) {
				//prepare resources for process in the runtime (resource and dependency)
				//execute process after load required resources
				List<HAPResourceDependency> dependencys = this.getProcess().getResourceDependency(runtime.getRuntimeInfo(), this.m_resourceManager);
				
				List<HAPResourceInfo> resourcesId = new ArrayList<HAPResourceInfo>();
				for(HAPResourceDependency dependency : dependencys) {
					resourcesId.add(new HAPResourceInfo(dependency.getId()));
				}
				
				HAPRuntimeTask loadResourcesTask = new HAPRuntimeTaskLoadResourcesRhino(resourcesId);
				loadResourcesTask.registerListener(new HAPRunTaskEventListenerInner(this, rhinoRuntime));
				return loadResourcesTask;
			}
			else {
				//pppp			
				HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteProcessTask(this, rhinoRuntime);
				rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
			}
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
			e.printStackTrace();
		}
		return null;
	}
	
	class HAPRunTaskEventListenerInner implements HAPRunTaskEventListener{
		private HAPRuntimeTaskExecuteProcessRhino m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteProcessRhino parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteProcessTask(this.m_parent, this.m_runtime);
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
