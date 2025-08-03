package com.nosliw.core.application.entity.service;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPResultInteractiveTask;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.core.gateway.HAPGatewayImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
@Component
public class HAPGatewayService extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_REQUEST = "request";

	@HAPAttribute
	final public static String COMMAND_REQUEST_QUERY = "query";

	@HAPAttribute
	final public static String COMMAND_REQUEST_PARMS = "parms";

	@HAPAttribute
	final public static String COMMAND_SEARCHDEFINITION = "searchDefinition";

	@HAPAttribute
	final public static String COMMAND_SEARCHDEFINITION_QUERY = "query";
	
	private HAPManagerService m_serviceManager;

	public HAPGatewayService(HAPManagerService serviceMan){
		this.m_serviceManager = serviceMan;
	}
	
	@Override
	public String getName() {   return HAPConstantShared.GATEWAY_SERVICE;  }
	
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
			HAPResultInteractiveTask serviceResult = this.m_serviceManager.execute(serviceQuery, dataSourceParms);
			out = this.createSuccessWithObject(serviceResult);
			break;
		}
		case COMMAND_SEARCHDEFINITION:
		{
			HAPQueryServiceDefinition defQuery = new HAPQueryServiceDefinition();
			defQuery.buildObject(parms.optJSONObject(COMMAND_SEARCHDEFINITION_QUERY), HAPSerializationFormat.JSON);
			List<HAPServiceProfile> serviceDefs = this.m_serviceManager.queryDefinition(defQuery);
			out = this.createSuccessWithObject(serviceDefs);
			return out;
		}
		}
		return out;
	}

}
