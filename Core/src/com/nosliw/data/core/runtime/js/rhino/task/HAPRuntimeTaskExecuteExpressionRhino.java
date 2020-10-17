package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteExpression;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.util.expression.HAPUtilityRuntimeJSScript;

public class HAPRuntimeTaskExecuteExpressionRhino extends HAPRuntimeTaskExecuteExpression{
	private HAPResourceManagerRoot m_resourceManager;
	
	public HAPRuntimeTaskExecuteExpressionRhino(
			HAPExecutableExpressionGroup expression, 
			String itemName,
			Map<String, HAPData> variablesValue, 
			Map<String, HAPData> referencesValue,
			HAPResourceManagerRoot resourceManager) {
		super(expression, itemName, (Map)variablesValue, referencesValue);
		this.m_resourceManager = resourceManager;
	}

	@Override
	public Class getResultDataType() {	return HAPData.class;	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			if(!HAPRuntime.isDemo) {
				//prepare resources for expression in the runtime (resource and dependency)
				//execute expression after load required resources
				List<HAPResourceDependency> dependencys = this.getExpression().getResourceDependency(runtime.getRuntimeInfo(), this.m_resourceManager);
				
				List<HAPResourceInfo> resourcesId = new ArrayList<HAPResourceInfo>();
				for(HAPResourceDependency dependency : dependencys) {
					resourcesId.add(new HAPResourceInfo(dependency.getId()));
				}

//				List<HAPExecutableExpression> expressions = new ArrayList<HAPExecutableExpression>();
//				expressions.add(this.getExpression());
//				List<HAPResourceInfo> resourcesId =	HAPUtilityExpressionResource.discoverResourceRequirement(expressions, rhinoRuntime.getRuntimeEnvironment().getResourceManager(), runtime.getRuntimeInfo());
				
				HAPRuntimeTask loadResourcesTask = new HAPRuntimeTaskLoadResourcesRhino(resourcesId);
				loadResourcesTask.registerListener(new HAPRunTaskEventListenerInner(this, rhinoRuntime));
				return loadResourcesTask;
			}
			else {
				//pppp			
				HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteExpressionTask(this, rhinoRuntime);
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
		private HAPRuntimeTaskExecuteExpressionRhino m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteExpressionRhino parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteExpressionTask(this.m_parent, this.m_runtime);
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
