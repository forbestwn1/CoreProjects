package com.nosliw.data.core.component.event;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.handler.HAPHandler;

@HAPEntityWithAttribute
public class HAPDefinitionHandlerEvent extends HAPEntityInfoImp{

	@HAPAttribute
	public static String EVENTNAME = "eventName";

	@HAPAttribute
	public static String HANDLER = "handler";

	@HAPAttribute
	public static String IN = "in";

	private String m_eventName;
	
	private HAPHandler m_hander;
	
	private HAPDefinitionDataAssociation m_dataAssociation;

	public HAPDefinitionHandlerEvent() {	
		this.m_hander = new HAPHandler();
	}

	public HAPDefinitionHandlerEvent(String eventName) {
		this();
		this.m_eventName = eventName;
	}
	
	public void setHandler(HAPHandler handler) {    this.m_hander = handler;     }
	public HAPHandler getHandler(){   return this.m_hander;  	}
	
	public String getEventName() {   return this.m_eventName;    }
	public void setEventName(String eventName) {   this.m_eventName = eventName;  }
	
	public HAPDefinitionDataAssociation getDataAssociation() {   return this.m_dataAssociation;    }
	
	public HAPDefinitionHandlerEvent cloneEventHandler() {
		HAPDefinitionHandlerEvent out = new HAPDefinitionHandlerEvent();
		this.cloneToEntityInfo(out);
		out.m_eventName = this.m_eventName;
		out.m_dataAssociation = this.m_dataAssociation.cloneDataAssocation();
		out.m_hander = this.m_hander.cloneHandler();
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTNAME, this.getEventName());
		jsonMap.put(HANDLER, HAPUtilityJson.buildJson(this.m_hander, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_dataAssociation, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject daJsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(daJsonObj);
		
		this.m_eventName = daJsonObj.getString(EVENTNAME);
		this.m_hander.buildObject(daJsonObj.get(HANDLER), HAPSerializationFormat.JSON);
		this.m_dataAssociation = HAPParserDataAssociation.buildDefinitionByJson(daJsonObj.optJSONObject(IN));
		return true;
	}
}
