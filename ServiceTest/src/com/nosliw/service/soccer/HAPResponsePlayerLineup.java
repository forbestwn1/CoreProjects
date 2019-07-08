package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPResponsePlayerLineup extends HAPExecutableImp{

	@HAPAttribute
	public static final String WAITINGLIST = "waitingList";

	@HAPAttribute
	public static final String LINEUP = "lineUp";

	@HAPAttribute
	public static final String LINEUP1 = "lineUp1";

	@HAPAttribute
	public static final String LINEUP2 = "lineUp2";

	@HAPAttribute
	public static final String LINEUP3 = "lineUp3";

	@HAPAttribute
	public static final String LINEUP4 = "lineUp4";

	private int itemsPerLoop = HAPSystemUtility.getItemsPerLoop();
	
	private List<String> m_waitingList;
	
	private List<HAPResponseSpot> m_lineUp;

	private List<HAPResponseSpot> m_lineUp1;
	private List<HAPResponseSpot> m_lineUp2;
	private List<HAPResponseSpot> m_lineUp3;
	private List<HAPResponseSpot> m_lineUp4;

	public void setWaitingList(List<String> wl) {   this.m_waitingList = wl;    }
	public void addSpot(HAPResponseSpot spot) {
		if(this.m_lineUp.size()<itemsPerLoop)		this.m_lineUp.add(spot);   
		else if(this.m_lineUp1.size()<itemsPerLoop)		this.m_lineUp1.add(spot);
		else if(this.m_lineUp2.size()<itemsPerLoop)		this.m_lineUp2.add(spot);
		else if(this.m_lineUp3.size()<itemsPerLoop)		this.m_lineUp3.add(spot);
		else	this.m_lineUp4.add(spot);
	}
	public void setSpots(List<HAPResponseSpot> spots) {
		for(HAPResponseSpot spot :spots) {
			this.addSpot(spot);
		}
	}

	public HAPResponsePlayerLineup() {
		this.m_waitingList = new ArrayList<String>();
		this.m_lineUp = new ArrayList<HAPResponseSpot>();
		this.m_lineUp1 = new ArrayList<HAPResponseSpot>();
		this.m_lineUp2 = new ArrayList<HAPResponseSpot>();
		this.m_lineUp3 = new ArrayList<HAPResponseSpot>();
		this.m_lineUp4 = new ArrayList<HAPResponseSpot>();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(WAITINGLIST, HAPJsonUtility.buildJson(this.m_waitingList, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP, HAPJsonUtility.buildJson(this.m_lineUp, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP1, HAPJsonUtility.buildJson(this.m_lineUp1, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP2, HAPJsonUtility.buildJson(this.m_lineUp2, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP3, HAPJsonUtility.buildJson(this.m_lineUp3, HAPSerializationFormat.JSON));
		jsonMap.put(LINEUP4, HAPJsonUtility.buildJson(this.m_lineUp4, HAPSerializationFormat.JSON));
	}
}
