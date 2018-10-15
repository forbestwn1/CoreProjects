package com.nosliw.uiresource.page.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextEntity;
import com.nosliw.data.core.script.context.HAPContextNodeRoot;
import com.nosliw.data.core.script.context.HAPContextParser;

public class HAPDefinitionUIEvent {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String CONTEXT = "context";

	private String m_name;

	private String m_description;
	
	//context
	private HAPContext m_context;
	
	public HAPContextEntity() {
		this.m_context = new HAPContext();
	}

	public String getName() {   return this.m_name;  }
	
	public HAPContext getContext() {  return this.m_context;   }
	public void setContext(HAPContext context) {   this.m_context = context;  }
	
	public void addContextElement(String name, HAPContextNodeRoot node) {  this.m_context.addElement(name, node);  }
	
	public void cloneBasicTo(HAPContextEntity contextEntity) {
		contextEntity.m_name = this.m_name;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		HAPContextParser.parseContext(jsonObj.optJSONObject(CONTEXT), this.m_context);
		return true;  
	}

}
