package com.nosliw.data.core.script.expression;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;

public class HAPExecutableScriptText extends HAPExecutableScriptEntity{

	private String m_text;
	
	public HAPExecutableScriptText(String id, String text) {
		super(HAPConstant.SCRIPT_TYPE_TEXT, id);
	}

	public String getText() {    return this.m_text;   }

	@Override
	public Set<HAPVariableInfo> getVariablesInfo() {  return new HashSet<HAPVariableInfo>();  }

	@Override
	public Set<HAPDefinitionConstant> getConstantsDefinition() {  return new HashSet<HAPDefinitionConstant>(); }
}
