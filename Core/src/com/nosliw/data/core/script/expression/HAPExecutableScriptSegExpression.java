package com.nosliw.data.core.script.expression;

public class HAPExecutableScriptSegExpression implements HAPExecutableScriptSeg{

	private String m_id;
	
	public HAPExecutableScriptSegExpression(String id) {
		this.m_id = id;
	}
	
	public String getExpressionId() {    return this.m_id;    }
	
}
