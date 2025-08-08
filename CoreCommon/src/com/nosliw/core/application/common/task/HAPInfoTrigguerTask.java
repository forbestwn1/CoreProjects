package com.nosliw.core.application.common.task;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.common.structure22.HAPElementStructure;
import com.nosliw.core.application.common.structure22.HAPUtilityParserElement;

@HAPEntityWithAttribute
public class HAPInfoTrigguerTask extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String TRIGGUERTYPE = "trigguerType";
	
	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";
	
	@HAPAttribute
	public static final String HANDLERID = "handlerId";
	
	@HAPAttribute
	public static final String VALUEPORTGROUPNAME = "valuePortGroupName";

	private String m_trigguerType;
	
	private HAPElementStructure m_dataDefinition;

	private HAPIdBrickInBundle m_handlerId;
	
	private String m_externalValuePortGroupName;

	public String getTrigguerType() {     return this.m_trigguerType;       }
	
	public HAPElementStructure getEventDataDefinition() {   return this.m_dataDefinition;      }
	
	public HAPIdBrickInBundle getHandlerId() {   return this.m_handlerId;     }
	
	public void setExternalValuePortGroupName(String name) {    this.m_externalValuePortGroupName = name;       }
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_trigguerType = (String)jsonObj.opt(TRIGGUERTYPE);
		
		this.m_dataDefinition = HAPUtilityParserElement.parseStructureElement(jsonObj.optJSONObject(DATADEFINITION));
		
		this.m_handlerId = new HAPIdBrickInBundle();
		this.m_handlerId.buildObject(jsonObj.opt(HANDLERID), HAPSerializationFormat.JSON);

		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TRIGGUERTYPE, this.m_trigguerType);
		jsonMap.put(DATADEFINITION, m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(HANDLERID, this.m_handlerId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUEPORTGROUPNAME, m_externalValuePortGroupName);
	}
}
