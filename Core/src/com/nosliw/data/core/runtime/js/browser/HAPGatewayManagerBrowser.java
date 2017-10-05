package com.nosliw.data.core.runtime.js.browser;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.js.HAPGateway;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;

public class HAPGatewayManagerBrowser extends HAPGatewayManager{

	public HAPGatewayManagerBrowser(){	}
	
	@Override
	public HAPServiceData executeGateway(String gatewayId, String command, Object parmsObj){
		HAPGateway gateway = this.getGateway(gatewayId);

		JSONObject jsonObjParms = null; 
		if(parmsObj instanceof String)				jsonObjParms = new JSONObject(parmsObj);
		else if(parmsObj instanceof JSONObject)		jsonObjParms = (JSONObject)parmsObj;
		
		HAPServiceData commandResult = null;
		try {
			commandResult = gateway.command(command, jsonObjParms);
		} catch (Exception e1) {
			e1.printStackTrace();
			return HAPServiceData.createFailureData(null, "Exception during command!!");
		}
		
		if(commandResult==null)  return HAPServiceData.createSuccessData();
		
		if(commandResult.isFail())  return commandResult;    //if command return fail result, then just return the result
		else{
			try{
				//if command return success, need to process output, and create new ServiceData
				//for scripts part, load into tuntime
				//for data part, create
				HAPGatewayOutput output = (HAPGatewayOutput)commandResult.getData();
				return HAPServiceData.createSuccessData(output);
			}
			catch(Exception e){
				e.printStackTrace();
				return HAPServiceData.createFailureData(null, "Exception during process command result!!");
			}
		}
	}

}
