package com.nosliw.data.core.runtime.js.rhino;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.value.HAPRhinoDataUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPResourceDefinitionExpression;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPExecutableExpressionImp;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteExpressionRhino;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPRhinoRuntimeUtility {

	private static int index = 1;
	
	public static HAPData executeOperandSync(HAPOperandWrapper operand, Map<String, HAPData> parms, Map<String, HAPData> referenceValues, HAPRuntime runtime, HAPResourceManagerRoot resourceManager) {
		HAPRuntimeTaskExecuteExpressionRhino exeExpTask = new HAPRuntimeTaskExecuteExpressionRhino(new HAPExecutableExpressionImp() {
			@Override
			public HAPOperandWrapper getOperand() {  return operand;  }

			@Override
			public String toStringValue(HAPSerializationFormat format) {
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				HAPExecutableExpression.buildJsonMap(this, outJsonMap, typeJsonMap);
				return HAPJsonUtility.buildMapJson(outJsonMap, typeJsonMap);
			}

			@Override
			public boolean buildObject(Object value, HAPSerializationFormat format) {
				return false;
			}

			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Map<String, HAPVariableInfo> getVarsInfo() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HAPMatchers getOutputMatchers() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void updateVariableName(HAPUpdateName nameUpdate) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public HAPResourceDefinitionExpression getDefinition() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void discover(HAPDataTypeCriteria expectOutput, HAPProcessTracker processTracker) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public HAPDataTypeCriteria getOutputCriteria() {
				// TODO Auto-generated method stub
				return null;
			}
		}, parms, referenceValues, resourceManager);
		HAPData out = (HAPData)runtime.executeTaskSync(exeExpTask).getData();
		return out;
	}
	
	
	public static void invokeGatewayHandlers(HAPServiceData serviceData, Object handlers, Scriptable scope){
		Context context = Context.enter();
		try{
			NativeObject handlersObj = (NativeObject)handlers;
			if(serviceData.isSuccess()){
				Function successFun = (Function)handlersObj.get("success");
				successFun.call(context, scope, null, new Object[]{null, HAPRhinoDataUtility.toRhinoScriptableObjectFromObject(serviceData.getData())});
			}
			else{
				Function errorFun = (Function)handlersObj.get("error");
				errorFun.call(context, scope, null, new Object[]{null, context.javaToJS(serviceData, scope)});
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
	
	public static List<HAPResourceInfo> rhinoResourcesInfoToResourcesInfo(NativeArray rhinoResourceInfoArray){
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		for(int i=0; i<rhinoResourceInfoArray.size(); i++){
			try{
				NativeObject resourceInfoObject = (NativeObject)rhinoResourceInfoArray.get(i);
				JSONObject jsonObj = (JSONObject)HAPRhinoDataUtility.toJson(resourceInfoObject);
				
				HAPResourceInfo resourceInfo = new HAPResourceInfo();
				resourceInfo.buildObject(jsonObj, HAPSerializationFormat.JSON);
				out.add(resourceInfo);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return out;
	}
	
	//convert rhino resource id array to HAPResourceId array
	public static List<HAPResourceId> rhinoResourcesIdToResourcesId(NativeArray rhinoResourceIdArray){
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(int i=0; i<rhinoResourceIdArray.size(); i++){
			try{
				NativeObject resourceIdObject = (NativeObject)rhinoResourceIdArray.get(i);
				String jsonString = HAPRhinoDataUtility.toJson(resourceIdObject).toString();

				HAPResourceId resourceId = HAPResourceIdFactory.newInstance(new JSONObject(jsonString));
				resourceIds.add(resourceId);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return resourceIds;
	}
	
	public static void loadScript(String script, Scriptable scope, String name, boolean exportScript){
		Context context = Context.enter();
		try{
			if(!HAPRuntime.isDemo && exportScript) {
				//ppppp			
				String folder = getScriptTempFolder();
				String scriptTempFile = folder + "/" + String.format("%03d", index++) + "_" + name;  //+".js";
				HAPFileUtility.writeFile(scriptTempFile, script);
			}
			context.evaluateString(scope, script, name, 1, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
	
	public static String getScriptTempFolder(){
		File directory = new File(HAPSystemFolderUtility.getCurrentScriptExportFolder());
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    return directory.getAbsolutePath();
	}
	
	public static void exportToHtml() {
		List<File> files = HAPFileUtility.sortFiles(HAPFileUtility.getAllFiles(getScriptTempFolder()));
		
		StringBuffer scriptContent = new StringBuffer();
		for(File file : files){
			String fileName = file.getPath();
			if(fileName.contains("Library__nosliw.runtimerhinoinit;__init.js.js")) {
				appendScriptToScript(scriptContent, "nosliw.createNode(\"runtime.name\", \"browser\");");
			}
			else if(fileName.contains("Library__nosliw.runtimerhino;__gatewayservice.js.js")) {
				appendLibraryToScript(scriptContent, "remoteservice");
				appendLibraryToScript(scriptContent, "runtimebrowser");
				appendScriptToScript(scriptContent, "var runtime = nosliw.getNodeData(\"runtime.createRuntime\")(nosliw.runtimeName);	  runtime.interfaceObjectLifecycle.init();");
			}
			else if(fileName.contains("Library__nosliw.runtimerhino;__runtime.js.js")) {
			}
			else {
				appendFileToScript(scriptContent, fileName);
			}
		}
		scriptContent.append("\n");
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("script", scriptContent.toString());
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPScriptTracker.class, "scriptTracker.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		
		HAPFileUtility.writeFile(HAPRhinoRuntimeUtility.getScriptTempFolder()+"/main.html", out);

	}
	
	private static void appendFileToScript(StringBuffer scriptContent, String file) {
		scriptContent.append("<script src=\""+file+"\"></script>\n");
	}

	private static void appendScriptToScript(StringBuffer scriptContent, String script) {
		scriptContent.append("<script>"+script+"</script>\n");
	}

	private static void appendLibraryToScript(StringBuffer scriptContent, String libDir) {
		List<File> files = HAPFileUtility.sortFiles(HAPFileUtility.getAllFiles(HAPSystemFolderUtility.getNosliwJSFolder(libDir)));
		for(File file : files) {
			appendFileToScript(scriptContent, file.getAbsolutePath());
		}
	}
	
}
