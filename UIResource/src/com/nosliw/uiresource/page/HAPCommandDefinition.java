package com.nosliw.uiresource.page;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.context.HAPContext;
import com.nosliw.uiresource.context.HAPContextParser;

//command def for ui resource
@HAPEntityWithAttribute
public class HAPCommandDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String PARMS = "parms";

	private String m_name;
	
	private HAPContext m_parms;

	public HAPCommandDefinition() {
		this.m_parms = new HAPContext();
	}

	public String getName() {   return this.m_name;  }
	
	public HAPContext getParms() {  return this.m_parms;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(PARMS, this.m_parms.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		HAPContextParser.parseContext(jsonObj.optJSONObject(PARMS), this.m_parms);
		return true;  
	}
}
