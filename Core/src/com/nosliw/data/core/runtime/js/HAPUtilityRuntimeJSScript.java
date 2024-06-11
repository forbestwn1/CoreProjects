package com.nosliw.data.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPOperationParm;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteConverter;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteDataOperation;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcess;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteProcessEmbeded;
import com.nosliw.data.core.runtime.HAPRuntimeTaskLoadResources;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSLibrary;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityRuntimeJSScript {

	public static List<HAPJSScriptInfo> buildScriptForResource(HAPResourceInfo resourceInfo, HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		//build library script info first
		if(resource.getId().getResourceTypeId().getResourceType().equals(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY)){
			out.addAll(buildScriptInfoForLibrary(resourceInfo, resource));
		}
		
		if(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER.equals(resourceInfo.getInfo().getValue(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER))) {
			return out;
		}
		
		//build script for resource with data
		out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource));
		
//		if(resource.getResourceData() instanceof HAPResourceDataJSValue){
//			out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource, ((HAPResourceDataJSValue)resource.getResourceData()).getValue()));
//		}
		return out;
	}
	
	private static HAPJSScriptInfo buildScriptInfoForResourceWithScript(HAPResourceInfo resourceInfo, HAPResource resource){
		HAPJSScriptInfo out = null;
		String script = buildImportResourceScriptForResource(resourceInfo, resource);
		
		String loadPattern = (String)resource.getInfoValue(HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN);
		if(loadPattern==null) {
			loadPattern = HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_VALUE;
		}
		switch(loadPattern){
		case HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_FILE:
			//load as file, create temp file first
			String name = resource.getId().toStringValue(HAPSerializationFormat.LITERATE);
			name = name.replace(";", "_");
			String resourceFile = HAPSystemFolderUtility.getResourceTempFileFolder() + name + ".js";
			resourceFile = HAPUtilityFile.writeFile(resourceFile, script);
			out = HAPJSScriptInfo.buildByFile(resourceFile, name);
			
			break;
		case HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_VALUE:
			//load as value
			out = HAPJSScriptInfo.buildByScript(script.toString(), "Resource__"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}

		return out;
	}
	
	private static String buildImportResourceScriptForResource(HAPResourceInfo resourceInfo, HAPResource resource){
		StringBuffer script = new StringBuffer();
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("resourceInfo", resourceInfo.toStringValue(HAPSerializationFormat.JSON));
		
		String infoJson = HAPSerializeManager.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON);
		if(HAPUtilityBasic.isStringEmpty(infoJson)){
			templateParms.put(HAPResource.INFO, "undefined");
		}
		else{
			templateParms.put(HAPResource.INFO, infoJson);
		}

		String valueScript = resource.getResourceData().toStringValue(HAPSerializationFormat.JAVASCRIPT);
		if(valueScript==null) {
			valueScript = "undefined";
		}
		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ImportResource.temp");
		script.append("\n");
		String resoruceDataScript = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		script.append(resoruceDataScript);
		script.append("\n");
		
		HAPUtilityFile.writeFile("c:\\Temp\\test.js", script.toString());
		
		return script.toString();
	}
	
	private static List<HAPJSScriptInfo> buildScriptInfoForLibrary(HAPResourceInfo resourceInfo, HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		
		HAPResourceDataJSLibrary resourceLibrary = (HAPResourceDataJSLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			File file = new File(uri);
			String fileFullName = file.getAbsolutePath().replaceAll("\\\\", "/");
			HAPJSScriptInfo scriptInfo = HAPJSScriptInfo.buildByFile(fileFullName, "Library__" + resource.getId().getCoreIdLiterate() + "__" + file.getName());
			scriptInfo.setType(resource.getId().getResourceTypeId().getResourceType());
			out.add(scriptInfo);
		}
		out.add(buildScriptInfoForResourceWithScript(resourceInfo, resource));
		return out;
	}

	public static HAPJSScriptInfo buildRequestScriptForExecuteDataOperationTask(HAPRuntimeTaskExecuteDataOperation executeDataOperationTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("dataTypeId", executeDataOperationTask.getDataTypeId().toStringValue(HAPSerializationFormat.LITERATE));
		templateParms.put("operation", executeDataOperationTask.getOperation());
		templateParms.put("parmsArray", HAPUtilityJson.formatJson(HAPUtilityJson.buildJson(executeDataOperationTask.getParms()==null?new ArrayList<HAPOperationParm>() : executeDataOperationTask.getParms(), HAPSerializationFormat.JSON)));

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeDataOperationTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteDataOperationScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeDataOperationTask.getTaskId());
		return out;
	}

	public static HAPJSScriptInfo buildRequestScriptForExecuteDataConvertTask(HAPRuntimeTaskExecuteConverter executeConverterTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("data", HAPUtilityJson.formatJson(executeConverterTask.getData().toStringValue(HAPSerializationFormat.JSON)));
		templateParms.put("matchers", HAPUtilityJson.formatJson(executeConverterTask.getMatchers().toStringValue(HAPSerializationFormat.JSON)));

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeConverterTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteDataConvertScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeConverterTask.getTaskId());
		return out;
	}
	
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

	public static HAPJSScriptInfo buildRequestScriptForLoadResourceTask(HAPRuntimeTaskLoadResources loadResourcesTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", loadResourcesTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		templateParms.put("resourceInfos", HAPUtilityJson.formatJson(HAPUtilityJson.buildJson(loadResourcesTask.getResourcesInfo(), HAPSerializationFormat.JSON)));
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "LoadResources.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, loadResourcesTask.getTaskId());
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
		for(HAPResourceDataScript seg : script.getSegments()){
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
