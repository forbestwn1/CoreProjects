package com.nosliw.data.core.runtime.js.rhino;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;

public class HAPRhinoRuntimeUtility {

	private static int index = 1;
	
	private static String scriptTempFolder = "C:/Temp/ScriptExport/scripts/" + System.currentTimeMillis() + "/";

	public static List<HAPResourceInfo> rhinoResourcesInfoToResourcesInfo(NativeArray rhinoResourceInfoArray, Context context, Scriptable scope){
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		for(int i=0; i<rhinoResourceInfoArray.size(); i++){
			try{
				NativeObject resourceInfoObject = (NativeObject)rhinoResourceInfoArray.get(i);
				String jsonString = HAPRhinoDataUtility.toJSONStringValue(resourceInfoObject, context, scope);
				HAPResourceInfo resourceInfo = new HAPResourceInfo();
				resourceInfo.buildObject(new JSONObject(jsonString), HAPSerializationFormat.JSON);
				out.add(resourceInfo);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return out;
	}
	
	//convert rhino resource id array to HAPResourceId array
	public static List<HAPResourceId> rhinoResourcesIdToResourcesId(NativeArray rhinoResourceIdArray, Context context, Scriptable scope){
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(int i=0; i<rhinoResourceIdArray.size(); i++){
			try{
				NativeObject resourceIdObject = (NativeObject)rhinoResourceIdArray.get(i);
				String jsonString = HAPRhinoDataUtility.toJSONStringValue(resourceIdObject, context, scope);

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
	
	public static void loadScript(String script, Context context, Scriptable scope, String name){
		try{
			String folder = getScriptTempFolder();
			String scriptTempFile = folder + "/" + String.format("%03d", index++) + "_" + name+".js";
			HAPFileUtility.writeFile(scriptTempFile, script);
			
			context.evaluateString(scope, script, name, 1, null);
		}
		catch(Exception e){
			e.printStackTrace();
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
