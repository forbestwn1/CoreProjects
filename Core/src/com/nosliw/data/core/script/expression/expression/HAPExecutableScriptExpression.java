package com.nosliw.data.core.script.expression.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPExecutableScriptEntity;

public class HAPExecutableScriptExpression extends HAPExecutableScriptEntity{

	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;
	
	private boolean m_isDataExpression;

	public HAPExecutableScriptExpression(String id) {
		super(HAPConstant.SCRIPT_TYPE_EXPRESSION, id);
	}
}
