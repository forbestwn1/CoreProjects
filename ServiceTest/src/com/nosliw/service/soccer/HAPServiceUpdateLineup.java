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

		String actionStr = null;
		String statusStr = null;
		if(parms.get("player")!=null) {
			String player = (String)parms.get("player").getValue();
			
			HAPData actionData = parms.get("action");
			String action = actionData==null? null : (String)actionData.getValue();
			
			HAPActionResult actionResult = null;
			actionResult = HAPPlayerLineupManager.getInstance().updateLineUp(player, action);
			
			HAPPlayerStatus playerStatus = null;
			if(actionResult==null) {
				playerStatus = HAPPlayerLineupManager.getInstance().getLineup().getPlayerStatus(player);
			}
			else {
				playerStatus = actionResult.getPlayerStatus();
			}
			actionStr = playerStatus.getActions().get(0);
			statusStr = player + "目前在 :  " + playerStatus.getStatus();
		}
		else {
			actionStr = "";
			statusStr = "还没有提供你的名字，请首先提供你的名字！！";
		}
		
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put("action", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), actionStr));
		output.put("status", new HAPDataWrapper(new HAPDataTypeId("test.string;1.0.0"), statusStr));
		return HAPUtilityService.generateSuccessResult(output);
	}

}
