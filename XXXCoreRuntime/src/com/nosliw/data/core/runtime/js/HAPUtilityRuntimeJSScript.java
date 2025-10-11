package com.nosliw.data.core.runtime.js;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcess;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcessEmbeded;

public class HAPUtilityRuntimeJSScript {


	public static HAPJSScriptInfo buildRequestScriptForExecuteProcessTask(HAPRuntimeTaskExecuteProcess task, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("processDef", task.getProcess().toResourceData(runtime.getRuntimeInfo()).toString());
		
		String inputJson = HAPUtilityJson.buildJson(task.getInput(), HAPSerializationFormat.JSON);
		templateParms.put("inputData", inputJson);
		
		templateParms.put("taskId", task.getTaskId());

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteProcessScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}

	public static HAPJSScriptInfo buildRequestScriptForExecuteProcessEmbededTask(HAPRuntimeTaskExecuteProcessEmbeded task, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("processDef", task.getProcess().toResourceData(runtime.getRuntimeInfo()).toString());
		
		String parentContextDataJson = HAPUtilityJson.buildJson(task.getParentContextData(), HAPSerializationFormat.JSON);
		templateParms.put("parentContextData", parentContextDataJson);
		
		templateParms.put("taskId", task.getTaskId());

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteProcessEmbededScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}


	
	
	
	

/*	
	//build execute function for script expression
	public static String buildScriptExpressionJSFunction(HAPScriptExpression111 scriptExpression){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		
		//build javascript function to execute the script
		StringBuffer funScript = new StringBuffer();
		int i=0;
		for(Object ele : scriptExpression.getElements()){
			if(ele instanceof HAPExecutableExpressionGroup){
				funScript.append(expressionsDataParmName+"[\""+scriptExpression.getIdByIndex(i)+"\"]");
			}
			else if(ele instanceof HAPScriptInScriptExpression){
				HAPScriptInScriptExpression scriptSegment = (HAPScriptInScriptExpression)ele;
				List<Object> scriptSegmentEles = scriptSegment.getContainerElements();
				for(Object scriptSegmentEle : scriptSegmentEles){
					if(scriptSegmentEle instanceof String){
						funScript.append((String)scriptSegmentEle);
					}
					else if(scriptSegmentEle instanceof HAPConstantInScript){
						funScript.append(constantsDataParmName + "[\"" + ((HAPConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
					}
					else if(scriptSegmentEle instanceof HAPVariableInScript){
						funScript.append(variablesDataParmName + "[\"" + ((HAPVariableInScript)scriptSegmentEle).getVariableName()+"\"]");
					}
				}
			}
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}

	//build script for execute script expression task 
	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptExpressionTask11(HAPRuntimeTaskExecuteScriptExpressionAbstract task, HAPRuntimeImpRhino runtime){
		Map<String, Object> variableValue = task.getVariablesValue();

		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(variableValue==null?new LinkedHashMap<String, HAPData>() : variableValue, HAPSerializationFormat.JSON)));

		//build javascript function to execute the script
		templateParms.put("functionScript", task.getScriptFunction());
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("expressions", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(task.getExpressionItems(), HAPSerializationFormat.JSON)));
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("constants", HAPJsonUtility.buildJson(task.getConstantsValue(), HAPSerializationFormat.JSON));

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteScriptExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}

	public static String buildEmbedScriptExpressionJSFunction111(HAPEmbededScriptExpression111 embedScriptExpression){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		String scriptExpressionParmName = "scriptExpressionData";

		StringBuffer scriptExpressionFunScript = new StringBuffer();
		Map<String, HAPScriptExpression111> scriptExpressions = embedScriptExpression.getScriptExpressions();
		for(String expId : scriptExpressions.keySet()) {
			scriptExpressionFunScript.append(scriptExpressionParmName +  "["+ expId  + "]="  +buildScriptExpressionJSFunction(scriptExpressions.get(expId)) + "("+expressionsDataParmName+","+constantsDataParmName+","+variablesDataParmName+");");
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "EmbededScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("embededScriptExpressionFunction", buildMainScriptEmbededScriptExpression(embedScriptExpression));
		
		templateParms.put("executeScriptExpressions", scriptExpressionFunScript.toString());
		
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		templateParms.put("scriptExpressionData", scriptExpressionParmName);
		
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}
	
	public static String buildMainScriptEmbededScriptExpression111(HAPEmbededScriptExpression111 embededScriptExp) {
		//build javascript function to execute the script
		String scriptExpressionDataParmName = "scriptExpressionData"; 
		StringBuffer funScript = new StringBuffer();
		int i = 0;
		for(Object ele : embededScriptExp.getElements()){
			if(i>0)  funScript.append("+");
			if(ele instanceof String){
				funScript.append("\""+ele+"\"");
			}
			else if(ele instanceof HAPScriptExpression111){
				funScript.append(scriptExpressionDataParmName+"[\""+embededScriptExp.getScriptExpressionIdByIndex(i)+"\"]");
			}
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "EmbededScriptExpressionSubFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("scriptExpressionData", scriptExpressionDataParmName);
		return HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);

	}

	//build execute function for script expression
	public static String buildScriptJSFunction111(HAPExecutableScriptGroup scriptGroup, Object scriptId){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		
		HAPExecutableScriptExpression script = (HAPExecutableScriptExpression)scriptGroup.getScript(scriptId);
		
		//build javascript function to execute the script
		StringBuffer funScript = new StringBuffer();
		int i=0;
		for(HAPResourceDataConfigure seg : script.getSegments()){
			if(seg instanceof HAPExecutableScriptSegExpression){
				HAPExecutableScriptSegExpression expressionSeg = (HAPExecutableScriptSegExpression)seg;
				funScript.append(expressionsDataParmName+"[\""+expressionSeg.getExpressionId()+"\"]");
			}
			else if(seg instanceof HAPExecutableScriptSegScript){
				HAPExecutableScriptSegScript scriptSegment = (HAPExecutableScriptSegScript)seg;
				List<Object> scriptSegmentEles = scriptSegment.getElements();
				for(Object scriptSegmentEle : scriptSegmentEles){
					if(scriptSegmentEle instanceof String){
						funScript.append((String)scriptSegmentEle);
					}
					else if(scriptSegmentEle instanceof HAPConstantInScript){
						funScript.append(constantsDataParmName + "[\"" + ((HAPConstantInScript)scriptSegmentEle).getConstantName()+"\"]");
					}
					else if(scriptSegmentEle instanceof HAPVariableInScript){
						funScript.append(variablesDataParmName + "[\"" + ((HAPVariableInScript)scriptSegmentEle).getVariableName()+"\"]");
					}
				}
			}
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return out;
	}
*/

}
