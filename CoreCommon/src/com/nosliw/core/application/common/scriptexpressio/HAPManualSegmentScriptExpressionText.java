package com.nosliw.core.application.common.scriptexpressio;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualSegmentScriptExpressionText extends HAPManualSegmentScriptExpression{

	@HAPAttribute
	public static String CONTENT = "content";

	private String m_content;
	
	public HAPManualSegmentScriptExpressionText(String id, String content) {
		super(id);
		this.m_content = content;
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT;  }

	public String getContent() {    return this.m_content;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTENT, this.getContent());
	}
}
