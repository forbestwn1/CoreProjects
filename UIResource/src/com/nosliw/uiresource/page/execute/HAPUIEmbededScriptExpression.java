package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.expression.HAPScript;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEmbededScriptExpression;

@HAPEntityWithAttribute
public class HAPUIEmbededScriptExpression extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String UIID = "uiId";
	
	@HAPAttribute
	public static final String SCRIPT = "script";
	
	private String m_uiId;
	
	private HAPScript m_script;
	
	public HAPUIEmbededScriptExpression(HAPDefinitionUIEmbededScriptExpression uiEmbededScriptExpression){
		uiEmbededScriptExpression.cloneToEntityInfo(this);
		this.m_uiId = uiEmbededScriptExpression.getUIId();
		this.m_script = uiEmbededScriptExpression.getScript();
	}
	
	public String getUIId(){   return this.m_uiId;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPT, this.m_script.toStringValue(HAPSerializationFormat.JSON));
	}
}
