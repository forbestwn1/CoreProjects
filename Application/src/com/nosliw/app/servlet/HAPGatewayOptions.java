package com.nosliw.app.servlet;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

public class HAPGatewayOptions extends HAPGatewayImp{

	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		return this.createSuccessWithObject(new String[] {"value1", "value2", "value3"});
	}

}
