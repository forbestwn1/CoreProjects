package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPDataWrapper;
import com.nosliw.data.core.domain.common.interactive.HAPResultInteractive;
import com.nosliw.data.core.service.definition.HAPExecutableService;
import com.nosliw.data.core.service.definition.HAPUtilityService;

public class HAPServiceGetLineup implements HAPExecutableService{

	@Override
	public HAPResultInteractive execute(Map<String, HAPData> parms){
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		
		HAPPlayerLineup lineUp = HAPPlayerLineupManager.getInstance().getLineup();
		
		HAPResponsePlayerLineup response = new HAPResponsePlayerLineup();
		response.setWaitingList(lineUp.getWaitingList());
		
		List<HAPResponseSpot> spots = new ArrayList<HAPResponseSpot>();
		for(HAPSpot s : lineUp.getLineUp()) {
			HAPResponseSpot spot = new HAPResponseSpot();
			List<String> players = s.getPlayers();
			spot.addPlayer(players.get(0));
			if(players.size()>1) {
				String player1 = players.get(players.size()-1);
				if(!players.get(0).equals(player1)) {
					spot.addPlayer(player1);
				}
			}
			spots.add(spot);
		}
		for(int i : lineUp.getVacant()) spots.get(i).setVacant();

		for(HAPResponseSpot spot : spots) {
			if(spot.getVacant())  spot.addPlayer("????");
		}
		
		response.setSpots(spots);
		
		output.put("lineup", new HAPDataWrapper(new HAPDataTypeId("test.data;1.0.0"), response));
		return HAPUtilityService.generateSuccessResult(output);
	}

}
