package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.js.HAPGateway;

public abstract class HAPGatewayManager {
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
	abstract public HAPServiceData executeGateway(String gatewayId, String command, Object parms);
}
