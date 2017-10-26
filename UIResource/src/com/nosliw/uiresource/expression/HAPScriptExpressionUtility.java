package com.nosliw.uiresource.expression;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.uiresource.HAPUIResourceIdGenerator;

public class HAPScriptExpressionUtility {

	public static final String UIEXPRESSION_TOKEN_OPEN = "<%=";
	public static final String UIEXPRESSION_TOKEN_CLOSE = "%>";
	
	/**
	 * parse text to discover script expression in it
	 * @param text
	 * @param idGenerator
	 * @param expressionMan
	 * @return a list of text and ui expression object
	 */
	public static List<Object> discoverUIExpressionInText(
			String text, 
			HAPUIResourceIdGenerator idGenerator, 
			HAPExpressionManager expressionMan){
		List<Object> out = new ArrayList<Object>();
		int start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		while(start != -1){
			if(start>0)   out.add(text.substring(0, start));
			int expEnd = text.indexOf(UIEXPRESSION_TOKEN_CLOSE, start);
			int end = expEnd + UIEXPRESSION_TOKEN_CLOSE.length();
			String expression = text.substring(start+UIEXPRESSION_TOKEN_OPEN.length(), expEnd);
			String uiId = idGenerator.createId();
			HAPScriptExpression uiExpression = new HAPScriptExpression(uiId, expression, expressionMan);
			out.add(uiExpression);
			//keep searching the rest
			text=text.substring(end);
			start = text.indexOf(UIEXPRESSION_TOKEN_OPEN);
		}
		if(!HAPBasicUtility.isStringEmpty(text)){
			out.add(text);
		}
		return out;
	}
	
	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptExpressionTask(HAPRuntimeTaskExecuteScriptExpression task, HAPRuntimeImpRhino runtime){
		HAPScriptExpression scriptExpression = task.getScriptExpression();
		Map<String, HAPData> variableValue = task.getVariablesValue();

		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(variableValue==null?new LinkedHashMap<String, HAPData>() : variableValue, HAPSerializationFormat.JSON)));

		//build javascript function to execute the script
		if(scriptExpression.getContent().contains("ccc")){
			int kkkk = 5555;
			kkkk++;
		}
		
		String funScript = buildScriptExpressionJSFunction(scriptExpression);
		templateParms.put("functionScript", funScript);
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("expressions", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(task.getExpressions(), HAPSerializationFormat.JSON)));
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("constants", HAPJsonUtility.buildJson(task.getScriptConstants(), HAPSerializationFormat.JSON));

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPScriptExpressionUtility.class, "ExecuteScriptExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}
	
	//build execute function for script expression
	public static String buildScriptExpressionJSFunction(HAPScriptExpression scriptExpression){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		
		//build javascript function to execute the script
		StringBuffer funScript = new StringBuffer();
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		for(Object ele : scriptExpression.getElements()){
			if(ele instanceof HAPExpressionDefinition){
				HAPExpressionDefinition expression = (HAPExpressionDefinition)ele;
				funScript.append(expressionsDataParmName+"[\""+expression.getName()+"\"]");
			}
			else if(ele instanceof HAPScriptExpressionScriptSegment){
				HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)ele;
				List<Object> scriptSegmentEles = scriptSegment.getElements();
				for(Object scriptSegmentEle : scriptSegmentEles){
					if(scriptSegmentEle instanceof String){
						funScript.append((String)scriptSegmentEle);
					}
					else if(scriptSegmentEle instanceof HAPScriptExpressionScriptConstant){
						funScript.append(constantsDataParmName + "[\"" + ((HAPScriptExpressionScriptConstant)scriptSegmentEle).getConstantName()+"\"]");
					}
					else if(scriptSegmentEle instanceof HAPScriptExpressionScriptVariable){
						funScript.append(variablesDataParmName + "[\"" + ((HAPScriptExpressionScriptVariable)scriptSegmentEle).getVariableName()+"\"]");
					}
				}
			}
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPScriptExpressionUtility.class, "ScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}
	
}
