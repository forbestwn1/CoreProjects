package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.js.HAPGateway;
import com.nosliw.data.core.runtime.js.HAPGatewayOutput;

public class HAPGatewayManager {
	private Map<String, HAPGateway> m_gateways;
	
	public HAPGatewayManager(){
		this.m_gateways = new LinkedHashMap<String, HAPGateway>();
	}
	
	public void registerGateway(String name, HAPGateway gateway){
		this.m_gateways.put(name, gateway);
	}

	public void unregisterGateway(String name){
		this.m_gateways.remove(name);
	}
	
	public HAPGateway getGateway(String name){
		return this.m_gateways.get(name);
	}

	/**
	 * Implement the gateway 
	 * @param gatewayId
	 * @param command
	 * @param parms can be either:
	 * 					json string
	 * 					JSONObject
	 * 					NativeObject
	 * @return
	 */
	public HAPServiceData executeGateway(String gatewayId, String command, JSONObject parms, HAPRuntimeInfo runtimeInfo){
		
		HAPGateway gateway = this.getGateway(gatewayId);

		HAPServiceData commandResult = null;
		try {
			commandResult = gateway.command(command, parms, runtimeInfo);
		} catch (Exception e1) {
			e1.printStackTrace();
			return HAPServiceData.createFailureData(null, "Exception during command " + gatewayId + " : " +command + "!!");
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
