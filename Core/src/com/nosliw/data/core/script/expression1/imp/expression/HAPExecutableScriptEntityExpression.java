package com.nosliw.data.core.script.expression1.imp.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression1.HAPExecutableScriptEntity;

public class HAPExecutableScriptEntityExpression extends HAPExecutableScriptEntity{

	
	private boolean m_isDataExpression;

	public HAPExecutableScriptEntityExpression(String id) {
		super(HAPConstantShared.EXPRESSION_TYPE_SCRIPT, id);
	}
}
