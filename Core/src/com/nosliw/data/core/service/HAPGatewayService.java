package com.nosliw.data.core.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

@HAPEntityWithAttribute
public class HAPGatewayService extends HAPGatewayImp{

	@HAPAttribute
	final public static String COMMAND_GETDATA = "getData";

	@HAPAttribute
	final public static String COMMAND_GETDATA_NAME = "name";

	@HAPAttribute
	final public static String COMMAND_GETDATA_PARMS = "parms";
	
	private HAPManagerService m_serviceManager;

	public HAPGatewayService(HAPManagerService serviceMan){
		this.m_serviceManager = serviceMan;
	}
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		switch(command){
		case COMMAND_GETDATA:
		{
			String dataSourceName = parms.getString(COMMAND_GETDATA_NAME);
			JSONObject parmsJson = parms.optJSONObject(COMMAND_GETDATA_PARMS);
			Map<String, HAPData> dataSourceParms = HAPDataUtility.buildDataWrapperMapFromJson(parmsJson);
			Map<String, HAPData> dataOut = this.m_serviceManager.execute(dataSourceName, dataSourceParms);
			out = this.createSuccessWithObject(dataOut);
			break;
		}
		}
		return out;
	}

}
