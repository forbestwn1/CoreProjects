package com.nosliw.data.core.script.expression;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPExecutableScriptEntityText extends HAPExecutableScriptEntity{

	private String m_text;
	
	public HAPExecutableScriptEntityText(String id, String text) {
		super(HAPConstantShared.EXPRESSION_TYPE_TEXT, id);
	}

	public String getText() {    return this.m_text;   }

}
