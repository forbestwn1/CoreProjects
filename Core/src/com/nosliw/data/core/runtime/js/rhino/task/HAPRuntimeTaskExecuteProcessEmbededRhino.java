package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcessEmbeded;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.structure.data.HAPContextData;

public class HAPRuntimeTaskExecuteProcessEmbededRhino extends HAPRuntimeTaskExecuteProcessEmbeded{
	private HAPResourceManagerRoot m_resourceManager;
	
	public HAPRuntimeTaskExecuteProcessEmbededRhino(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData parentContextData, HAPResourceManagerRoot resourceManager) {
		super(process, parentContextData);
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
				HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteProcessEmbededTask(this, rhinoRuntime);
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
		private HAPRuntimeTaskExecuteProcessEmbededRhino m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteProcessEmbededRhino parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteProcessEmbededTask(this.m_parent, this.m_runtime);
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
