package com.nosliw.data.core.runtime.js;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

/**
 * Gateway is a java object that will accept command from runtime  
 *
 */
public interface HAPGateway {

	/**
	 * execute command
	 * @param command command name
	 * @param parms parameters in format of JSONObject
	 * @param runtimeInfo environment infor on request side of gate way 
	 * @return HAPService containing data of HAPRuntimeGatewayOutput
	 */
	HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception;

}
