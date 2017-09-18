package com.nosliw.uiresource;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

public class HAPJSScriptUtility {

	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptExpressionTask(HAPRuntimeTaskExecuteScriptExpression task){
		HAPScriptExpression scriptExpression = task.getScriptExpression();
		Map<String, HAPData> variableValue = task.getVariablesValue();

		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(variableValue==null?new LinkedHashMap<String, HAPData>() : variableValue, HAPSerializationFormat.JSON)));

		StringBuffer funScript = new StringBuffer();
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		for(Object ele : scriptExpression.getProcessedElements()){
			if(ele instanceof HAPExpression){
				HAPExpression expression = (HAPExpression)ele;
				String expressionId = expression.getId();
				expressions.put(expressionId, expression);
				funScript.append("expressionData["+expressionId+"]");
			}
			else if(ele instanceof String){
				funScript.append(ele.toString());
			}
		}
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("expressions", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(expressions, HAPSerializationFormat.JSON)));
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("gatewayPath", HAPConstant.RUNTIME_LANGUAGE_JS_GATEWAY);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteScriptExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}
}
