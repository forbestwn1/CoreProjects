package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;

public class HAPExecutableScript extends HAPExecutableImpEntityInfo{

	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;
	
	private boolean m_isDataExpression;

	private List<HAPExecutableScriptSeg> m_segs;
	
	public HAPExecutableScript() {
		this.m_segs = new ArrayList<HAPExecutableScriptSeg>();
	}
	
	
	public void addSegment(HAPExecutableScriptSeg segment) {    this.m_segs.add(segment);   }
	
	public List<HAPExecutableScriptSeg> getSegments(){    return this.m_segs;     }
}
