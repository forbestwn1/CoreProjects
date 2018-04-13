package com.nosliw.data.core.runtime.js.rhino;

import java.io.File;
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
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPRhinoRuntimeUtility {

	private static int index = 1;
	
	private static String scriptTempFolder = HAPFileUtility.getScriptExportFolder() + System.currentTimeMillis() + "/";

	public static HAPData executeOperandSync(HAPOperandWrapper operand, Map<String, HAPData> parms, Map<String, HAPData> referenceValues, HAPRuntime runtime) {
		HAPRuntimeTaskExecuteExpressionRhino exeExpTask = new HAPRuntimeTaskExecuteExpressionRhino(new HAPExecuteExpression() {
			@Override
			public String getId() {				return "";			}

			@Override
			public HAPOperandWrapper getOperand() {  return operand;  }

			@Override
			public Map<String, HAPMatchers> getVariableMatchers() {	return null; }

			@Override
			public String toStringValue(HAPSerializationFormat format) {
				Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
				Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
				HAPExecuteExpression.buildJsonMap(this, outJsonMap, typeJsonMap);
				return HAPJsonUtility.buildMapJson(outJsonMap, typeJsonMap);
			}

			@Override
			public boolean buildObject(Object value, HAPSerializationFormat format) {
				return false;
			}
		}, parms, referenceValues);
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

				HAPResourceId resourceId = new HAPResourceId();
				resourceId.buildObject(new JSONObject(jsonString), HAPSerializationFormat.JSON);
				resourceIds.add(resourceId);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return resourceIds;
	}
	
	public static void loadScript(String script, Scriptable scope, String name){
		Context context = Context.enter();
		try{
//ppppp			
//			String folder = getScriptTempFolder();
//			String scriptTempFile = folder + "/" + String.format("%03d", index++) + "_" + name+".js";
//			HAPFileUtility.writeFile(scriptTempFile, script);
			
			context.evaluateString(scope, script, name, 1, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
	}
	
	private static String getScriptTempFolder(){
		File directory = new File(scriptTempFolder);
	    if (! directory.exists()){
	    	directory.mkdir();
	    }
	    return directory.getAbsolutePath();
	}
}
