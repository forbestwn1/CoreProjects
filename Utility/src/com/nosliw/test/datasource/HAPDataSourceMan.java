package com.nosliw.test.datasource;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.HAPGatewayDataSource;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPDataSourceMan {

	public static void main(String[] args) throws Exception {

		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		JSONObject parms = new JSONObject();
		parms.put(HAPGatewayDataSource.COMMAND_GETDATA_NAME, "myrealtor");
		
		HAPServiceData serviceData = runtimeEnvironment.getGatewayManager().getGateway(HAPDataSourceManager.GATEWAY_DATASOURCE).command(HAPGatewayDataSource.COMMAND_GETDATA, parms, null);
		System.out.println(serviceData);
		
	}

}
