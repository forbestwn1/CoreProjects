package com.nosliw.core.application.common.event;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPParserStructure;

@HAPEntityWithAttribute
public class HAPInfoEvent extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String HANDLERID = "handlerId";
	
	@HAPAttribute
	public static final String EVENTDATADEFINITION = "eventDataDefinition";
	
	@HAPAttribute
	public static final String VALUEPORTGROUPNAME = "valuePortGroupName";
	
	private HAPIdBrickInBundle m_handlerId;
	
	private HAPElementStructure m_eventDataDefinition;

	private String m_externalValuePortGroupName;

	public HAPIdBrickInBundle getHandlerId() {   return this.m_handlerId;     }
	
	public HAPElementStructure getEventDataDefinition() {   return this.m_eventDataDefinition;      }
	
	public void setExternalValuePortGroupName(String name) {    this.m_externalValuePortGroupName = name;       }
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_handlerId = new HAPIdBrickInBundle();
		this.m_handlerId.buildObject(jsonObj.opt(HANDLERID), HAPSerializationFormat.JSON);

		this.m_eventDataDefinition = HAPParserStructure.parseStructureElement(jsonObj.optJSONObject(EVENTDATADEFINITION));
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(HANDLERID, this.m_handlerId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EVENTDATADEFINITION, m_eventDataDefinition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUEPORTGROUPNAME, m_externalValuePortGroupName);
	}
}
