package com.nosliw.miniapp.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppData extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String CONTEXT = "context";

	private HAPContext m_context;
	
	public HAPDefinitionMiniAppData() {
		this.m_context = new HAPContext();
	}
	
	@Override
	public boolean buildObjectByJson(Object obj) {
		super.buildObjectByJson(obj);
		JSONObject jsonObj = (JSONObject)obj;
		this.m_context.buildObject(jsonObj.optJSONObject(CONTEXT), HAPSerializationFormat.JSON);
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}
}
