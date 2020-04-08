package com.nosliw.data.core.script.expression.literate;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPExecutableScript;

public class HAPExecutableScriptSegText implements HAPExecutableScript{

	private String m_text;
	
	private String m_id;
	
	public HAPExecutableScriptSegText(String id, String text) {
		this.m_id = id;
		this.m_text = text;
	}

	@Override
	public String getId() {   return this.m_id; }

	@Override
	public String getScriptType() {   return HAPConstant.SCRIPT_TYPE_SEG_TEXT;  }
	
	public String getText() {    return this.m_text;   }

}
