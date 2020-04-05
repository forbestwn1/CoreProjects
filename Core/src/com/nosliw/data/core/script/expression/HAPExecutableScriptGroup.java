package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableScriptGroup extends HAPExecutableImp{

	private HAPExecutableExpression m_expressionExe;
	
	private List<HAPExecutableScript> m_elements;
	
	public HAPExecutableScriptGroup() {
		this.m_elements = new ArrayList<HAPExecutableScript>();
	}
	
	public void setExpression(HAPExecutableExpression expression) {    this.m_expressionExe = expression;    }
	
	public void addScript(HAPExecutableScript script) {   this.m_elements.add(script);    }
}
