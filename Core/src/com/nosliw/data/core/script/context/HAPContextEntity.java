package com.nosliw.data.core.script.context;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPContextEntity  extends HAPEntityInfoImp{

	@HAPAttribute
	public static String CONTEXT = "context";

	//context
	private HAPContext m_context;
	
	public HAPContextEntity() {
		this.m_context = new HAPContext();
	}

	public HAPContext getContext() {  return this.m_context;   }
	public void setContext(HAPContext context) {   this.m_context = context;  }
	
	public void addContextElement(String name, HAPContextDefinitionRoot node) {  this.m_context.addElement(name, node);  }
	
	public void cloneToBase(HAPContextEntity contextEntity) {
		this.cloneToEntityInfo(contextEntity);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		HAPParserContext.parseContext(jsonObj.optJSONObject(CONTEXT), this.m_context);
		return true;  
	}
}
