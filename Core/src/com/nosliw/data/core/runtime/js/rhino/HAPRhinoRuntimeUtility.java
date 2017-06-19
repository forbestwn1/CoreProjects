package com.nosliw.data.core.runtime.js.rhino;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;

import net.sf.json.JSONSerializer;

public class HAPRhinoRuntimeUtility {

	private static int index = 1;
	
	private static String scriptTempFolder = "C:/Temp/ScriptExport/scripts/" + System.currentTimeMillis() + "/";

	public static List<HAPResourceInfo> rhinoResourcesInfoToResourcesInfo(NativeArray rhinoResourceInfoArray){
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		for(int i=0; i<rhinoResourceInfoArray.size(); i++){
			NativeObject resourceInfoObject = (NativeObject)rhinoResourceInfoArray.get(i);
			String jsonString = resourceInfoObject.toString();
			HAPResourceInfo resourceInfo = new HAPResourceInfo();
			resourceInfo.buildObject(JSONSerializer.toJSON(jsonString), HAPSerializationFormat.JSON);
			out.add(resourceInfo);
		}
		return out;
	}
	
	//convert rhino resource id array to HAPResourceId array
	public static List<HAPResourceId> rhinoResourcesIdToResourcesId(NativeArray rhinoResourceIdArray){
		List<HAPResourceId> resourceIds = new ArrayList<HAPResourceId>();
		for(int i=0; i<rhinoResourceIdArray.size(); i++){
			NativeObject resourceIdObject = (NativeObject)rhinoResourceIdArray.get(i);
			String type = (String)resourceIdObject.get(HAPResourceId.TYPE);
			String id = (String)resourceIdObject.get(HAPResourceId.ID);
			resourceIds.add(new HAPResourceId(type, id));
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
