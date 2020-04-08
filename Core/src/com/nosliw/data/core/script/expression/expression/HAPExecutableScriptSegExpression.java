package com.nosliw.data.core.script.expression.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.expression.HAPExecutableScript;

public class HAPExecutableScriptSegExpression implements HAPExecutableScript{

	private String m_id;
	
	private String m_expressionId;
	
	public HAPExecutableScriptSegExpression(String id, String expressionId) {
		this.m_id = id;
		this.m_expressionId = expressionId;
	}
	
	public String getExpressionId() {    return this.m_expressionId;    }

	@Override
	public String getId() {  return this.m_id;  }
	
	@Override
	public String getScriptType() {  return HAPConstant.SCRIPT_TYPE_SEG_EXPRESSION;  }

}
