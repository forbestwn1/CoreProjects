package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRunTaskEventListener;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPExpressionTaskRhino extends HAPExecuteExpressionTask{

	public HAPExpressionTaskRhino(HAPExpression expression, Map<String, HAPData> variablesValue) {
		super(expression, variablesValue);
	}

	@Override
	public HAPRuntimeTask execute(HAPRuntime runtime){
		HAPRuntimeImpRhino rhinoRuntime = (HAPRuntimeImpRhino)runtime;
		
		//init rhino runtime, init scope
		Scriptable scope = rhinoRuntime.initExecuteExpression(this.getTaskId());
		
		//prepare resources for expression in the runtime (resource and dependency)
		//execute expression after load required resources
		List<HAPResourceInfo> resourcesId = rhinoRuntime.getRuntimeEnvironment().getResourceDiscovery().discoverResourceRequirement(this.getExpression());
		
		HAPExpressionTaskRhino that = this;
		HAPRuntimeTask loadResourcesTask = new HAPLoadResourcesTaskRhino(resourcesId);
		loadResourcesTask.registerListener(new HAPRunTaskEventListener(){
			@Override
			public void success(HAPRuntimeTask task) {
				//after resource loaded, execute expression
				HAPJSScriptInfo scriptInfo = HAPRuntimeJSScriptUtility.buildRequestScriptForExecuteExpressionTask(that);
				rhinoRuntime.loadTaskScript(scriptInfo, getTaskId());
			}});
		
		return loadResourcesTask;
	}
}
