package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.expression.literate.HAPEmbededScriptExpression;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpression;

public class HAPUIEmbededScriptExpression extends HAPEmbededScriptExpression{

	@HAPAttribute
	public static final String UIID = "uiId";
	
	private String m_uiId;
	
	public HAPUIEmbededScriptExpression(HAPDefinitionUIEmbededScriptExpression uiEmbededScriptExpression){
		super(uiEmbededScriptExpression);
		this.m_uiId = uiEmbededScriptExpression.getUIId();
	}
	
	public String getUIId(){   return this.m_uiId;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UIID, this.m_uiId);
	}
}
