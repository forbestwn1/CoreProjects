package com.nosliw.data.core.event;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPParserContext;

//event definition
public class HAPDefinitionEvent extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String CONTEXT = "context";

	//event name, used to match event process
	private String m_name;

	//event data structure definition
	private HAPContext m_context;

	public HAPContext getContext() {	return this.m_context;	}
	
	public String getName() {   return this.m_name;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_context = HAPParserContext.parseContext(jsonObj.optJSONObject(CONTEXT));
		return true;  
	}
}
