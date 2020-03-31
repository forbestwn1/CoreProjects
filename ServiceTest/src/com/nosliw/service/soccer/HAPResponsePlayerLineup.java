package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.system.HAPSystemUtility;

@HAPEntityWithAttribute
public class HAPResponsePlayerLineup extends HAPExecutableImp{

	@HAPAttribute
	public static final String WAITINGLIST = "waitingList";

	@HAPAttribute
	public static final String LINEUP = "lineUp";

	private int itemsPerLoop = HAPSystemUtility.getItemsPerLoop();
	
	private List<String> m_waitingList;
	
	private List<HAPResponseSpot> m_lineUp;

	public void setWaitingList(List<String> wl) {   this.m_waitingList = wl;    }
	public void addSpot(HAPResponseSpot spot) {this.m_lineUp.add(spot);	}
	public void setSpots(List<HAPResponseSpot> spots) {
		for(HAPResponseSpot spot :spots) {
			this.addSpot(spot);
		}
	}

	public HAPResponsePlayerLineup() {
		this.m_waitingList = new ArrayList<String>();
		this.m_lineUp = new ArrayList<HAPResponseSpot>();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(WAITINGLIST, HAPJsonUtility.buildJson(this.m_waitingList, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP, HAPJsonUtility.buildJson(this.m_lineUp, HAPSerializationFormat.JSON));
	}
}
