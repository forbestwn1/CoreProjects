package com.nosliw.data.core.runtime.js.rhino;

import java.util.List;

import org.json.JSONObject;
import org.mozilla.javascript.NativeObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeGateway;
import com.nosliw.data.core.runtime.js.HAPRuntimeGatewayOutput;

/**
 */
public class HAPGatewayManagerRhino extends HAPGatewayManager{

	private HAPRuntimeImpRhino m_runtime;
	
	public HAPGatewayManagerRhino(HAPRuntimeImpRhino runtime){
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPServiceData executeGateway(String gatewayId, String command, Object parmsObj){
		HAPRuntimeGateway gateway = this.getGateway(gatewayId);

		JSONObject jsonObjParms = null; 
		if(parmsObj instanceof String)				jsonObjParms = new JSONObject(parmsObj);
		else if(parmsObj instanceof JSONObject)		jsonObjParms = (JSONObject)parmsObj;
		else if(parmsObj instanceof NativeObject)	jsonObjParms = (JSONObject)HAPRhinoDataUtility.toJson(parmsObj);
		
		HAPServiceData commandResult = gateway.command(command, jsonObjParms);
		
		if(commandResult.isFail())  return commandResult;    //if command return fail result, then just return the result
		else{
			try{
				//if command return success, need to process output, and create new ServiceData
				//for scripts part, load into tuntime
				//for data part, create
				HAPRuntimeGatewayOutput output = (HAPRuntimeGatewayOutput)commandResult.getData();
				List<HAPJSScriptInfo> scripts = output.getScripts();
				for(HAPJSScriptInfo scriptInfo : scripts){		this.m_runtime.loadScript(scriptInfo);	}
				return HAPServiceData.createSuccessData(output.getData());
			}
			catch(Exception e){
				e.printStackTrace();
				return HAPServiceData.createFailureData(null, "Fail to load resources into Rhino runtime");
			}
		}
	}

}
