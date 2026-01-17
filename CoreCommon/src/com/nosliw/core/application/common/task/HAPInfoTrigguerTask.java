package com.nosliw.core.application.common.task;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPPackageBrickInBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPUtilityParserElement;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

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

	private HAPPackageBrickInBundle m_handlerId;
	
	private String m_externalValuePortGroupName;

	public HAPInfoTrigguerTask() {}
	
	public String getTrigguerType() {     return this.m_trigguerType;       }
	public void setTrigguerType(String type) {   this.m_trigguerType = type;    }
	
	public HAPElementStructure getEventDataDefinition() {   return this.m_dataDefinition;      }
	public void setEventDataDefinition(HAPElementStructure dataDef) {   this.m_dataDefinition = dataDef;      }
	
	public HAPPackageBrickInBundle getHandlerId() {   return this.m_handlerId;     }
	public void setHandlerId(HAPPackageBrickInBundle handlerId) {   this.m_handlerId = handlerId;    }
	
	public void setExternalValuePortGroupName(String name) {    this.m_externalValuePortGroupName = name;       }
	
	
	public static HAPInfoTrigguerTask parseInfoTrigguerTask(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPInfoTrigguerTask out = new HAPInfoTrigguerTask();
		
		out.buildEntityInfoByJson(jsonObj);
		
		out.setTrigguerType((String)jsonObj.opt(TRIGGUERTYPE));
		out.setEventDataDefinition(HAPUtilityParserElement.parseStructureElement(jsonObj.optJSONObject(DATADEFINITION), dataRuleMan));
		
		HAPPackageBrickInBundle handlerId = new HAPPackageBrickInBundle();
		handlerId.buildObject(jsonObj.opt(HANDLERID), HAPSerializationFormat.JSON);
		out.setHandlerId(handlerId);
		
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_trigguerType!=null) {
			jsonMap.put(TRIGGUERTYPE, this.m_trigguerType);
		}
		if(m_dataDefinition!=null) {
			jsonMap.put(DATADEFINITION, m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(HANDLERID, this.m_handlerId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUEPORTGROUPNAME, m_externalValuePortGroupName);
	}
}
