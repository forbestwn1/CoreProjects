package com.nosliw.data.core.script.expression;

import com.nosliw.common.utils.HAPConstant;

public class HAPExecutableScriptText extends HAPExecutableScriptEntity{

	private String m_text;
	
	public HAPExecutableScriptText(String id, String text) {
		super(HAPConstant.SCRIPT_TYPE_TEXT, id);
	}

	public String getText() {    return this.m_text;   }

}
