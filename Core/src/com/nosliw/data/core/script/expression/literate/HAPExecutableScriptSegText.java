package com.nosliw.data.core.script.expression.literate;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression.HAPExecutableScriptImp;

//only plain text
public class HAPExecutableScriptSegText extends HAPExecutableScriptImp{

	@HAPAttribute
	public static String TEXT = "text";

	private String m_text;
	
	public HAPExecutableScriptSegText(String id, String text) {
		super(id);
		this.m_text = text;
	}

	@Override
	public String getScriptType() {   return HAPConstantShared.SCRIPT_TYPE_SEG_TEXT;  }
	
	public String getText() {    return this.m_text;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TEXT, this.m_text);
	}
}
