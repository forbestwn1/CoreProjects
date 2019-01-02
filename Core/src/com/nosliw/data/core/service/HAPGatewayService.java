package com.nosliw.data.core.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayService extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_REQUEST = "request";

	@HAPAttribute
	final public static String COMMAND_REQUEST_ID = "id";

	@HAPAttribute
	final public static String COMMAND_REQUEST_PARMS = "parms";
	
	private HAPManagerService m_serviceManager;

	public HAPGatewayService(HAPManagerService serviceMan){
		this.m_serviceManager = serviceMan;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_REQUEST:
		{
			String serviceId = parms.getString(COMMAND_REQUEST_ID);
			JSONObject parmsJson = parms.optJSONObject(COMMAND_REQUEST_PARMS);
			Map<String, HAPData> dataSourceParms = HAPDataUtility.buildDataWrapperMapFromJson(parmsJson);
			HAPResultService serviceResult = this.m_serviceManager.execute(serviceId, dataSourceParms);
			out = this.createSuccessWithObject(serviceResult);
			break;
		}
		}
		return out;
	}

}
