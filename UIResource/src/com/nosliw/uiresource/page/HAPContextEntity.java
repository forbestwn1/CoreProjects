package com.nosliw.uiresource.page;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextParser;

@HAPEntityWithAttribute
public class HAPContextEntity  extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String CONTEXTDEFINITION = "contextDefinition";
	
	@HAPAttribute
	public static String CONTEXT = "context";

	private String m_name;
	
	//context defintion
	private HAPContext m_contextDefinition;
	
	//absolute context
	private HAPContext m_context;

	public HAPContextEntity() {
		this.m_context = new HAPContext();
		this.m_contextDefinition = new HAPContext();
	}

	public String getName() {   return this.m_name;  }
	
	public HAPContext getContextDefinition() {  return this.m_contextDefinition;   }

	public HAPContext getContext() {  return this.m_context;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		HAPContextParser.parseContext(jsonObj.optJSONObject(CONTEXTDEFINITION), this.m_contextDefinition);
		HAPContextParser.parseContext(jsonObj.optJSONObject(CONTEXT), this.m_context);
		return true;  
	}
}
