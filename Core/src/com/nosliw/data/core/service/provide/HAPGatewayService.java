package com.nosliw.data.core.service.provide;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayService extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_REQUEST = "request";

	@HAPAttribute
	final public static String COMMAND_REQUEST_QUERY = "query";

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
			HAPQueryService serviceQuery = new HAPQueryService();
			serviceQuery.buildObject(parms.optJSONObject(COMMAND_REQUEST_QUERY), HAPSerializationFormat.JSON);
			JSONObject parmsJson = parms.optJSONObject(COMMAND_REQUEST_PARMS);
			Map<String, HAPData> dataSourceParms = HAPUtilityData.buildDataWrapperMapFromJson(parmsJson);
			HAPResultService serviceResult = this.m_serviceManager.execute(serviceQuery, dataSourceParms);
			out = this.createSuccessWithObject(serviceResult);
			break;
		}
		}
		return out;
	}

}
