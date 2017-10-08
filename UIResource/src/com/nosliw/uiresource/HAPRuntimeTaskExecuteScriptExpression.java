package com.nosliw.uiresource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskLoadResourcesRhino;

public class HAPRuntimeTaskExecuteScriptExpression extends HAPRuntimeTask{

	final public static String TASK = "ExecuteScriptExpression"; 
	
	@HAPAttribute
	public static String SCRIPTEXPRESSION = "scriptExpression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";
	
	private HAPScriptExpression m_scriptExpression;
	
	Map<String, HAPData> m_variablesValue;
	
	Map<String, HAPExpression> m_expressions;
	
	Map<String, Object> m_scriptConstants;
	
	public HAPRuntimeTaskExecuteScriptExpression(
			HAPScriptExpression scriptExpression, 
			Map<String, HAPExpression> expressions, 
			Map<String, HAPData> variablesValue, 
			Map<String, Object> scriptConstants){
		this.m_scriptExpression = scriptExpression;
		this.m_expressions = expressions;
		this.m_variablesValue = variablesValue; 
		this.m_scriptConstants = scriptConstants;
	}
	
	@Override
	public String getTaskType() {		return TASK;	}

	public HAPScriptExpression getScriptExpression(){ return this.m_scriptExpression;  }
	public Map<String, HAPExpression> getExpressions(){  return this.m_expressions; }
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }
	public Map<String, Object> getScriptConstants(){  return this.m_scriptConstants;  }
	
	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime) {
		try{
			HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
			
			//prepare resources for expression in the runtime (resource and dependency)
			//execute expression after load required resources
			List<HAPExpression> expressions = new ArrayList(this.m_expressions.values());
			List<HAPResourceInfo> resourcesId = rhinoRuntime.getRuntimeEnvironment().getResourceDiscovery().discoverResourceRequirement(expressions);
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
		private HAPRuntimeTaskExecuteScriptExpression m_parent;
		private HAPRuntimeImpRhino m_runtime;
		
		public HAPRunTaskEventListenerInner(HAPRuntimeTaskExecuteScriptExpression parent, HAPRuntimeImpRhino runtime){
			this.m_parent = parent;
			this.m_runtime = runtime;
		}
		
		@Override
		public void finish(HAPRuntimeTask task) {
			HAPServiceData resourceTaskResult = task.getResult();
			if(resourceTaskResult.isSuccess()){
				//after resource loaded, execute expression
				try{
					HAPJSScriptInfo scriptInfo = HAPJSScriptUtility.buildRequestScriptForExecuteScriptExpressionTask(this.m_parent);
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