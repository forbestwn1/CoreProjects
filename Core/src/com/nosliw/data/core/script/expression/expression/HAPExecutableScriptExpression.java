package com.nosliw.data.core.script.expression.expression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;

public class HAPExecutableScriptExpression extends HAPExecutableScriptEntity{

	
	private boolean m_isDataExpression;

	public HAPExecutableScriptExpression(String id) {
		super(HAPConstantShared.SCRIPT_TYPE_EXPRESSION, id);
	}
}
