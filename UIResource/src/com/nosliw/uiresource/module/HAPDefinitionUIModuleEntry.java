package com.nosliw.uiresource.module;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.uiresource.context.HAPContext;

@HAPEntityWithAttribute
public class HAPDefinitionUIModuleEntry  extends HAPSerializableImp{

	@HAPAttribute
	public static final String PAGE = "page";

	@HAPAttribute
	public static final String CONTEXT = "context";

	private String m_page;
	
	private HAPContext m_context;
	
	public HAPContext getContext() { return this.m_context;   }
	
	public String getPage() {  return this.m_page;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PAGE, this.m_page);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_page = jsonObj.optString(PAGE);
		this.m_context = (HAPContext)HAPSerializeManager.getInstance().buildObject(HAPContext.class.getName(), jsonObj.optJSONObject(CONTEXT), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
	
}
