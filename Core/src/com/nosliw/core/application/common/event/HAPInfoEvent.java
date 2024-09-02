package com.nosliw.core.application.common.event;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPParserStructure;

public class HAPInfoEvent extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String HANDLERID = "handlerId";
	
	@HAPAttribute
	public static final String EVENTDATADEFINITION = "eventDataDefinition";
	
	private HAPIdBrickInBundle m_handlerId;
	
	private HAPElementStructure m_eventDataDefinition;
	
	public HAPIdBrickInBundle getHandlerId() {   return this.m_handlerId;     }
	
	public HAPElementStructure getEventDataDefinition() {   return this.m_eventDataDefinition;      }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_handlerId = new HAPIdBrickInBundle();
		this.m_handlerId.buildObject(jsonObj.opt(HANDLERID), HAPSerializationFormat.JSON);

		this.m_eventDataDefinition = HAPParserStructure.parseStructureElement(jsonObj.optJSONObject(EVENTDATADEFINITION));
		
		return true;  
	}
	
}
