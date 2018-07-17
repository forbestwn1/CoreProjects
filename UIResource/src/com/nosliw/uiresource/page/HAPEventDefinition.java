package com.nosliw.uiresource.page;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.context.HAPContextParser;

//ui resource unit event
//		it can be within resource or tag
@HAPEntityWithAttribute
public class HAPEventDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String EVENTDATA = "eventData";
	
	private String m_name;
	
	private HAPContext m_eventData;
	
	public HAPEventDefinition() {
		this.m_eventData = new HAPContext();
	}
	
	public String getName() {   return this.m_name;  }
	
	public HAPContext getContext() {  return this.m_eventData;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(EVENTDATA, this.m_eventData.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		HAPContextParser.parseContext(jsonObj.optJSONObject(EVENTDATA), this.m_eventData);
		return true;  
	}
}
