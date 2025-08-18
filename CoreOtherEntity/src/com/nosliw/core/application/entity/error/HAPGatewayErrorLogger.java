package com.nosliw.core.application.entity.error;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
@Component
public class HAPGatewayErrorLogger extends HAPGatewayImp{

	@HAPAttribute
	static public final String COMMAND_LOGERRRO = "logError";
	@HAPAttribute
	static public final String PARMS_ERROR = "error";
	
	private final static Logger LOGGER = Logger.getLogger(HAPGatewayErrorLogger.class.getName());

	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		if(COMMAND_LOGERRRO.equals(command)) {
			JSONArray errorArray = parms.getJSONArray(PARMS_ERROR);
			System.out.println(errorArray.toString());
			LOGGER.severe(errorArray.toString());
			return this.createSuccessWithObject(null);
		}
		return null;
	}

	@Override
	public String getName() {   return HAPConstantShared.GATEWAY_ERRORLOG;  }

}
