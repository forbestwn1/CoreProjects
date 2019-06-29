package com.nosliw.servlet;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPGatewayImp;
import com.nosliw.service.soccer.HAPPlayerLineupManager;

public class HAPGatewayOptions extends HAPGatewayImp{

	static public final String COMMAND_GETVALUES = "getValues";
	static public final String PARMS_GETVALUES_ID = "id";
	
	
	@Override
	public HAPServiceData command(String command, JSONObject parms, HAPRuntimeInfo runtimeInfo) throws Exception {
		if(COMMAND_GETVALUES.equals(command)) {
			String[] values = null;
			switch(parms.getString(PARMS_GETVALUES_ID)) {
			case "schoolType":
				values = new String[] {"Public", "Private", "First Nation"};
				break;
			case "buildingType":
				values = new String[] {"House", "Townhouse", "Appartment"};
				break;
			case "players2019Summer":
				values = HAPPlayerLineupManager.getInstance().getInitialLineupPlayers().toArray(new String[0]);
				break;
			}
			return this.createSuccessWithObject(values);
		}
		return null;
	}

}
