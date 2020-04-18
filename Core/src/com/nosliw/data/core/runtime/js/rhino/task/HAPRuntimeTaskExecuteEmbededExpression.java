package com.nosliw.data.core.runtime.js.rhino.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.expression.HAPUtilityExpressionResource;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJSScript;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.script.expression.literate.HAPEmbededScriptExpression;

public class HAPRuntimeTaskExecuteEmbededExpression extends HAPRuntimeTaskExecuteScriptExpressionAbstract{

	final public static String TASK = "ExecuteEmbededExpression"; 
	
	@HAPAttribute
	public static String EMBEDEDEXPRESSION = "embededExpression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";
	
	private HAPEmbededScriptExpression m_embededExpression;
	
	//variable value can be data or other object
	Map<String, Object> m_variablesValue;
	
	Map<String, Object> m_scriptConstants;
	
	public HAPRuntimeTaskExecuteEmbededExpression(
			HAPEmbededScriptExpression embededExpression, 
			Map<String, Object> variablesValue, 
			Map<String, Object> scriptConstants){
		this.m_embededExpression = embededExpression;
		this.m_variablesValue = variablesValue; 
		this.m_scriptConstants = scriptConstants;
	}
	
	@Override
	public String getTaskType() {		return TASK;	}

	public HAPEmbededScriptExpression getEmbededScriptExpression(){ return this.m_embededExpression;  }
	@Override
	public Map<String, Object> getVariablesValue(){  return this.m_variablesValue;  }
	@Override
	public Map<String, Object> getConstantsValue(){  return this.m_scriptConstants;  }
	@Override
	public Map<String, HAPExecutableExpressionGroup> getExpressions(){	return this.m_embededExpression.getExpressions();	}
	
	@Override
	public String getScriptFunction() {		return HAPUtilityRuntimeJSScript.buildEmbedScriptExpressionJSFunction(this.m_embededExpression);  }
	
	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			//prepare resources for expression in the runtime (resource and dependency)
			//execute expression after load required resources
			List<HAPExecutableExpressionGroup> expressions = new ArrayList(this.m_embededExpression.getExpressions().values());
			List<HAPResourceInfo> resourcesId =  HAPUtilityExpressionResource.discoverResourceRequirement(expressions, rhinoRuntime.getRuntimeEnvironment().getResourceManager(), runtime.getRuntimeInfo());
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
		private HAPRuntimeTaskExecuteEmbededExpression m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteEmbededExpression parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPUtilityRuntimeJSScript.buildRequestScriptForExecuteScriptExpressionTask(this.m_parent, this.m_runtime);
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
