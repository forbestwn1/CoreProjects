package com.nosliw.data.core.script.expression.literate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
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

	@Override
	public Set<HAPVariableInfo> getVariablesInfo() {  return new HashSet<HAPVariableInfo>();  }

	@Override
	public Set<HAPDefinitionConstant> getConstantsDefinition() {  return new HashSet<HAPDefinitionConstant>(); }
	
	@Override
	public void updateConstant(Map<String, Object> value) {	}
	
}
