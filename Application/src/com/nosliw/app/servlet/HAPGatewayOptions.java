package com.nosliw.app.servlet;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;

public class HAPGatewayOptions extends HAPGatewayImp{

	static public final String COMMAND_GETVALUES = "getValues";
	static public final String PARMS_GETVALUES_ID = "id";
	
	
	@Override
	public HAPServiceData command(String command, JSONObject parms) throws Exception {
		if(COMMAND_GETVALUES.equals(command)) {
			String[] values = null;
			switch(parms.getString(PARMS_GETVALUES_ID)) {
			case "schoolType":
				values = new String[] {"Public", "Private", "First Nation"};
				break;
			case "buildingType":
				values = new String[] {"House", "Townhouse", "Appartment"};
				break;
			}
			return this.createSuccessWithObject(values);
		}
		return null;
	}

}
