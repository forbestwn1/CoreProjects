package com.nosliw.data.core.domain.entity.expression.script;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityExpressionProcessConfigure;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskScriptExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionGroup;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptEntity;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptGroup;
import com.nosliw.data.core.script.expression1.HAPProcessorScript;

public class HAPUtilityScriptExpression {

	public static String solidateLiterate(String literate, Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv){
		HAPExecutableScriptGroup groupExe = HAPProcessorScript.processSimpleScript(literate, null, null, constants, HAPUtilityExpressionProcessConfigure.setDoDiscovery(null), runtimeEnv, new HAPProcessTracker());
		HAPExecutableScriptEntity scriptExe = groupExe.getScript(null);
		
		String scriptType = scriptExe.getScriptType();
		//if pure data
		if(HAPConstantShared.EXPRESSION_TYPE_TEXT.equals(scriptType))  return literate;
		
		//execute script expression
		HAPRuntimeTaskExecuteRhinoScriptExpressionGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionGroup(new HAPInfoRuntimeTaskScriptExpressionGroup(groupExe, null, null, null), runtimeEnv);
		HAPServiceData out = runtimeEnv.getRuntime().executeTaskSync(task);
		return (String)out.getData();
	}
}
