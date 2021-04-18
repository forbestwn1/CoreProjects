package com.nosliw.data.core.script.expression.imp.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;

public class HAPExecutableScriptEntityExpression extends HAPExecutableScriptEntity{

	
	private boolean m_isDataExpression;

	public HAPExecutableScriptEntityExpression(String id) {
		super(HAPConstantShared.SCRIPT_TYPE_EXPRESSION, id);
	}
}
