package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.json.JsonParser;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;

public class HAPGatewayEmbededPoint {

	private Scriptable m_scope;
	
	private HAPGatewayManager m_gatewayMan;

	private HAPRuntimeImpRhino m_runtime;
	
	public HAPGatewayEmbededPoint(HAPGatewayManager gatewayManager, HAPRuntimeImpRhino runtime, Scriptable scope){
		this.m_gatewayMan = gatewayManager;
		this.m_runtime = runtime;
		this.m_scope = scope;
	}
	
	public Object executeGateway(String gatewayId, String command, Object parmsObj){
		HAPServiceData outServiceData = null;
		try{
			JSONObject jsonObjParms = null; 
			if(parmsObj instanceof String)				jsonObjParms = new JSONObject(parmsObj);
			else if(parmsObj instanceof JSONObject)		jsonObjParms = (JSONObject)parmsObj;
			else if(parmsObj instanceof NativeObject)	jsonObjParms = (JSONObject)HAPRhinoDataUtility.toJson(parmsObj);

			HAPServiceData serviceData = this.m_gatewayMan.executeGateway(gatewayId, command, jsonObjParms);
			if(serviceData.isSuccess()){
				//if command return success, need to process output, and create new ServiceData
				//for scripts part, load into tuntime
				//for data part, create
				HAPGatewayOutput output = (HAPGatewayOutput)serviceData.getData();
				List<HAPJSScriptInfo> scripts = output.getScripts();
				for(HAPJSScriptInfo scriptInfo : scripts){		this.m_runtime.loadScript(scriptInfo);	}
				
				outServiceData = HAPServiceData.createSuccessData(output.getData());
			}
			else  outServiceData = serviceData;
		}
		catch(Exception e){
			e.printStackTrace();
			outServiceData = HAPServiceData.createFailureData(null, "Exception during process command result!!");
		}

		Object out = null;
		Context context = Context.enter();
		try{
			String serviceDataStr = HAPSerializeManager.getInstance().toStringValue(outServiceData, HAPSerializationFormat.JSON);
			JsonParser jsonParser = new JsonParser(context, this.m_scope);
			out = jsonParser.parseValue(serviceDataStr);
		}
		catch(Exception e){
			e.printStackTrace();
			String serviceDataStr = HAPSerializeManager.getInstance().toStringValue(HAPServiceData.createFailureData(), HAPSerializationFormat.JSON);
			out = context.evaluateString(this.m_scope, serviceDataStr, null, 0, null);
		}
		finally{
			Context.exit();
		}
		
		return out;
	}
}
