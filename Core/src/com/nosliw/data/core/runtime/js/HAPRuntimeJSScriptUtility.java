package com.nosliw.data.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPOperationParm;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expressionscript.HAPEmbededScriptExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpression;
import com.nosliw.data.core.expressionscript.HAPScriptExpressionScriptConstant;
import com.nosliw.data.core.expressionscript.HAPScriptExpressionScriptSegment;
import com.nosliw.data.core.expressionscript.HAPScriptExpressionScriptVariable;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteExpression;
import com.nosliw.data.core.runtime.HAPRuntimeTaskLoadResources;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteConverter;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteDataOperation;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSLibrary;
import com.nosliw.data.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteScriptExpressionAbstract;

public class HAPRuntimeJSScriptUtility {

	public static List<HAPJSScriptInfo> buildScriptForResource(HAPResourceInfo resourceInfo, HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		//build library script info first
		if(resource.getId().getType().equals(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY)){
			out.addAll(buildScriptInfoForLibrary(resource));
		}
		
		
		if(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER.equals(resourceInfo.getInfo().getValue(HAPRuntimeImpRhino.ADDTORESOURCEMANAGER)))  return out;
		
		//build script for resource with data
		if(resource.getResourceData() instanceof HAPResourceDataJSValue){
			out.add(buildScriptInfoForResourceWithValue(resourceInfo, resource));
		}
		return out;
	}
	
	private static HAPJSScriptInfo buildScriptInfoForResourceWithValue(HAPResourceInfo resourceInfo, HAPResource resource){
		HAPJSScriptInfo out = null;
		String script = buildImportResourceScriptForResourceWithValue(resourceInfo, resource);
		
		String loadPattern = (String)resource.getInfoValue(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN);
		if(loadPattern==null)  loadPattern = HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_VALUE;
		switch(loadPattern){
		case HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE:
			//load as file, create temp file first
			String name = resource.getId().toStringValue(HAPSerializationFormat.LITERATE);
			String resourceFile = HAPFileUtility.getResourceTempFileFolder() + name + ".js";
			resourceFile = HAPFileUtility.writeFile(resourceFile, script);
			out = HAPJSScriptInfo.buildByFile(resourceFile, name);
			
			break;
		case HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_VALUE:
			//load as value
			out = HAPJSScriptInfo.buildByScript(script.toString(), "Resource__"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE));
			break;
		}

		return out;
	}
	
	private static String buildImportResourceScriptForResourceWithValue(HAPResourceInfo resourceInfo, HAPResource resource){
		StringBuffer script = new StringBuffer();
		
		//build script for resource data
		String valueScript = ((HAPResourceDataJSValue)resource.getResourceData()).getValue();
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("resourceInfo", resourceInfo.toStringValue(HAPSerializationFormat.JSON));
		
		String infoJson = HAPSerializeManager.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON);
		if(HAPBasicUtility.isStringEmpty(infoJson)){
			templateParms.put(HAPResource.INFO, "undefined");
		}
		else{
			templateParms.put(HAPResource.INFO, infoJson);
		}

		if(valueScript==null)  valueScript = "undefined";
		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ImportResource.temp");
		script.append("\n");
		String resoruceDataScript = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		script.append(resoruceDataScript);
		script.append("\n");
		
		return script.toString();
	}
	
	private static List<HAPJSScriptInfo> buildScriptInfoForLibrary(HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		
		HAPResourceDataJSLibrary resourceLibrary = (HAPResourceDataJSLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			File file = new File(uri);
			String fileFullName = file.getAbsolutePath().replaceAll("\\\\", "/");
			out.add(HAPJSScriptInfo.buildByFile(fileFullName, "Library__" + resource.getId().getId() + "__" + file.getName()));
		}
		return out;
	}

	public static HAPJSScriptInfo buildRequestScriptForExecuteDataOperationTask(HAPRuntimeTaskExecuteDataOperation executeDataOperationTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("dataTypeId", executeDataOperationTask.getDataTypeId().toStringValue(HAPSerializationFormat.LITERATE));
		templateParms.put("operation", executeDataOperationTask.getOperation());
		templateParms.put("parmsArray", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(executeDataOperationTask.getParms()==null?new ArrayList<HAPOperationParm>() : executeDataOperationTask.getParms(), HAPSerializationFormat.JSON)));

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeDataOperationTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteDataOperationScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeDataOperationTask.getTaskId());
		return out;
	}

	public static HAPJSScriptInfo buildRequestScriptForExecuteDataConvertTask(HAPRuntimeTaskExecuteConverter executeConverterTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("data", HAPJsonUtility.formatJson(executeConverterTask.getData().toStringValue(HAPSerializationFormat.JSON)));
		templateParms.put("matchers", HAPJsonUtility.formatJson(executeConverterTask.getMatchers().toStringValue(HAPSerializationFormat.JSON)));

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeConverterTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteDataConvertScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeConverterTask.getTaskId());
		return out;
	}
	

	public static HAPJSScriptInfo buildRequestScriptForExecuteExpressionTask(HAPRuntimeTaskExecuteExpression executeExpressionTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("expression", HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(executeExpressionTask.getExpression(), HAPSerializationFormat.JSON)));
		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(executeExpressionTask.getVariablesValue()==null?new LinkedHashMap<String, HAPData>() : executeExpressionTask.getVariablesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("references", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(executeExpressionTask.getReferencesValue()==null?new LinkedHashMap<String, HAPData>() : executeExpressionTask.getReferencesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("constants", "{}");

		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeExpressionTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeExpressionTask.getTaskId());
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

		templateParms.put("resourceInfos", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(loadResourcesTask.getResourcesInfo(), HAPSerializationFormat.JSON)));
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "LoadResources.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, loadResourcesTask.getTaskId());
		return out;
	}
	
	//build script for execute script expression task 
	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptExpressionTask(HAPRuntimeTaskExecuteScriptExpressionAbstract task, HAPRuntimeImpRhino runtime){
		Map<String, Object> variableValue = task.getVariablesValue();

		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(variableValue==null?new LinkedHashMap<String, HAPData>() : variableValue, HAPSerializationFormat.JSON)));

		//build javascript function to execute the script
		templateParms.put("functionScript", task.getScriptFunction());
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("expressions", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(task.getExpressions(), HAPSerializationFormat.JSON)));
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("constants", HAPJsonUtility.buildJson(task.getScriptConstants(), HAPSerializationFormat.JSON));

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteScriptExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}

	public static String buildEmbedScriptExpressionJSFunction(HAPEmbededScriptExpression embedScriptExpression){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		String scriptExpressionParmName = "scriptExpressionData";

		StringBuffer scriptExpressionFunScript = new StringBuffer(); 
		List<HAPScriptExpression> scriptExpressions = embedScriptExpression.getScriptExpressionsList();
		for(HAPScriptExpression scriptExpression : scriptExpressions){
			scriptExpressionFunScript.append(scriptExpressionParmName +  "["+ scriptExpression.getId()  + "]="  +buildScriptExpressionJSFunction(scriptExpression) + "("+expressionsDataParmName+","+constantsDataParmName+","+variablesDataParmName+");");
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "EmbededScriptExpressionFunction.temp");
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
	
	
	public static String buildMainScriptEmbededScriptExpression(HAPEmbededScriptExpression embededScriptExp) {
		//build javascript function to execute the script
		String scriptExpressionDataParmName = "scriptExpressionData"; 
		StringBuffer funScript = new StringBuffer();
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		int i = 0;
		for(Object ele : embededScriptExp.getElements()){
			if(i>0)  funScript.append("+");
			if(ele instanceof String){
				funScript.append("\""+ele+"\"");
			}
			else if(ele instanceof HAPScriptExpression){
				HAPScriptExpression scriptExpression = (HAPScriptExpression)ele;
				funScript.append(scriptExpressionDataParmName+"[\""+scriptExpression.getId()+"\"]");
			}
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "EmbededScriptExpressionSubFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("scriptExpressionData", scriptExpressionDataParmName);
		return HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);

	}
	
	//build execute function for script expression
	public static String buildScriptExpressionJSFunction(HAPScriptExpression scriptExpression){
		String expressionsDataParmName = "expressionsData"; 
		String constantsDataParmName = "constantsData"; 
		String variablesDataParmName = "variablesData"; 
		
		//build javascript function to execute the script
		StringBuffer funScript = new StringBuffer();
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		int i = 0;
		for(Object ele : scriptExpression.getElements()){
			if(ele instanceof HAPDefinitionExpression){
				HAPDefinitionExpression expression = (HAPDefinitionExpression)ele;
				funScript.append(expressionsDataParmName+"[\""+i+"\"]");
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
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return script;
	}

}
