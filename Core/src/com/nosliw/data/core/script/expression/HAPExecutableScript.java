package com.nosliw.data.core.script.expression;

import java.util.List;

import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableScript extends HAPExecutableImp{

	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;
	
	private boolean m_isDataExpression;

	private List<HAPExecutableScriptSeg> m_segs;
	

	public void addSegment(HAPExecutableScriptSeg segment) {    this.m_segs.add(segment);   }
	
	
}
