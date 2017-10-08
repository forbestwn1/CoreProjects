package com.nosliw.data.core.runtime.js.rhino;

import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.runtime.HAPGatewayManager;

public class HAPGatewayEmbededPoint {

	private Scriptable m_scope;
	
	private HAPGatewayManager m_gatewayMan;
	
	public HAPGatewayEmbededPoint(HAPGatewayManager gatewayManager, Scriptable scope){
		this.m_gatewayMan = gatewayManager;
		this.m_scope = scope;
	}
	
	public Object executeGateway(String gatewayId, String command, Object parmsObj){
	
		JSONObject jsonObjParms = null; 
		if(parmsObj instanceof String)				jsonObjParms = new JSONObject(parmsObj);
		else if(parmsObj instanceof JSONObject)		jsonObjParms = (JSONObject)parmsObj;
		else if(parmsObj instanceof NativeObject)	jsonObjParms = (JSONObject)HAPRhinoDataUtility.toJson(parmsObj);

		HAPServiceData serviceData = this.m_gatewayMan.executeGateway(gatewayId, command, jsonObjParms);
		String serviceDataStr = HAPSerializeManager.getInstance().toStringValue(serviceData, HAPSerializationFormat.JSON);
		
		Object out = null;
		try{
			out = Context.enter().evaluateString(this.m_scope, serviceDataStr, null, 0, null);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			Context.exit();
		}
		return out;
	}
}
