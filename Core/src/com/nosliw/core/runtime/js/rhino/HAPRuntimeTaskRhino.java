package com.nosliw.core.runtime.js.rhino;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceInfo;
import com.nosliw.core.runtime.HAPRunTaskEventListener;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.js.rhino.task.HAPRuntimeTaskLoadResourcesRhino;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;

public abstract class HAPRuntimeTaskRhino extends HAPRuntimeTask{

	private String m_taskType;
	
	public HAPRuntimeTaskRhino(String taskType, HAPRuntimeEnvironment runtTimeEnv) {
		this.m_taskType = taskType;
	}
	
	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			if(!HAPRuntime.isDemo) {
				//prepare resources for expression in the runtime (resource and dependency)
				//execute expression after load required resources
				List<HAPResourceDependency> dependencys = this.getResourceDependency();
				
				List<HAPResourceInfo> resourcesId = new ArrayList<HAPResourceInfo>();
				if(dependencys!=null) {
					for(HAPResourceDependency dependency : dependencys) {
						resourcesId.add(new HAPResourceInfo(dependency.getId()));
					}
				}

				HAPRuntimeTask loadResourcesTask = new HAPRuntimeTaskLoadResourcesRhino(resourcesId);

				loadResourcesTask.registerListener(new HAPRunTaskEventListener() {
					
					@Override
					public void finish(HAPRuntimeTask task) {
						HAPServiceData resourceTaskResult = task.getResult();
						if(resourceTaskResult.isSuccess()){
							//after resource loaded, execute expression
							try{
								HAPJSScriptInfo scriptInfo = buildRuntimeScript();
								rhinoRuntime.loadTaskScript(scriptInfo, getTask().getTaskId());
							}
							catch(Exception e){
								getTask().finish(HAPServiceData.createFailureData(e, ""));
							}
						}
						else{
							getTask().finish(resourceTaskResult);
						}
					}
				});

				return loadResourcesTask;
			}
			else {
				//pppp			
				HAPJSScriptInfo scriptInfo = this.buildRuntimeScript();
				rhinoRuntime.loadTaskScript(scriptInfo, this.getTaskId());
			}
		}
		catch(Exception e){
			this.finish(HAPServiceData.createFailureData(e, ""));
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getTaskType(){    return this.m_taskType;     }

	protected HAPRuntimeTask getTask() {   return this;   }

	abstract protected List<HAPResourceDependency> getResourceDependency();
	
	abstract protected HAPJSScriptInfo buildRuntimeScript();

}
