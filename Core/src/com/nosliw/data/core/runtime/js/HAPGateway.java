package com.nosliw.data.core.runtime.js;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;

/**
 * Gateway is a java object that will accept command from runtime  
 *
 */
public interface HAPGateway {

	/**
	 * execute command
	 * @param command command name
	 * @param parms parameters in format of JSONObject
	 * @return HAPService containing data of HAPRuntimeGatewayOutput
	 */
	HAPServiceData command(String command, JSONObject parms) throws Exception;

}
