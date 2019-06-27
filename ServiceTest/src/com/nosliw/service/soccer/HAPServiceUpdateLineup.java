package com.nosliw.service.soccer;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.data.core.service.provide.HAPExecutableService;
import com.nosliw.data.core.service.provide.HAPProviderService;
import com.nosliw.data.core.service.provide.HAPResultService;
import com.nosliw.data.core.service.provide.HAPUtilityService;

public class HAPServiceUpdateLineup implements HAPExecutableService, HAPProviderService{

	@Override
	public HAPResultService execute(Map<String, HAPData> parms){

		String player = (String)parms.get("player").getValue();
		String action = (String)parms.get("action").getValue();
		
		HAPActionResult actionResult = HAPPlayerLineupManager.getInstance().updateLineUp(player, action);
		
		HAPPlayerStatus playerStatus;
		if(actionResult==null) {
			playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);
		}
		else {
			playerStatus = actionResult.getPlayerStatus();
		}
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("action", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), playerStatus.getActions().get(0)));
		output.put("status", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), playerStatus.getStatus()));

		return HAPUtilityService.generateSuccessResult(output);
	}

}
