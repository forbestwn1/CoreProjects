package com.nosliw.core.gateway;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@Component
public class HAPGatewayManager {
	
	private Map<String, HAPGateway> m_gateways;
	
	public HAPGatewayManager(List<HAPGateway> gateways){
		gateways.stream().forEach(g->this.m_gateways.put(g.getName(), g));
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
	 * @throws Exception 
	 */
	public HAPServiceData executeGateway(String gatewayId, String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception{
		
		HAPGateway gateway = this.getGateway(gatewayId);

		HAPServiceData commandResult = null;
		commandResult = gateway.command(command, parms, runtimeInfo);
		
		if(commandResult==null) {
			return HAPServiceData.createSuccessData();
		}
		
		if(commandResult.isFail()) {
			return commandResult;    //if command return fail result, then just return the result
		} else{
			try{
				//if command return success, need to process output, and create new ServiceData
				//for scripts part, load into tuntime
				//for data part, create
				HAPGatewayOutput output = (HAPGatewayOutput)commandResult.getData();
				return HAPServiceData.createSuccessData(output);
			}
			catch(Exception e){
				e.printStackTrace();
				return HAPServiceData.createFailureData(null, "Error during process command result!!");
			}
		}
		
	}
}
