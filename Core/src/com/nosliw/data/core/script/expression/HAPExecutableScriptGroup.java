package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableScriptGroup extends HAPExecutableImp{

	private HAPExecutableExpression m_expressionExe;
	
	private List<HAPExecutableScript> m_elements;
	
	private Map<String, Object> m_constants;
	
	public HAPExecutableScriptGroup() {
		this.m_elements = new ArrayList<HAPExecutableScript>();
		this.m_constants = new LinkedHashMap<String, Object>();
	}
	
	public HAPExecutableExpression getExpression() {    return this.m_expressionExe;   }
	public void setExpression(HAPExecutableExpression expression) {    this.m_expressionExe = expression;    }
	
	public Map<String, Object> getConstantsValue(){    return this.m_constants;     }
	public void addConstants(Map<String, Object> constants) {  this.m_constants.putAll(constants);    }
	
	public void addScript(HAPExecutableScript script) {   this.m_elements.add(script);    }
	public HAPExecutableScript getScript(Object id) {
		HAPExecutableScript out = null;
		if(id instanceof String) {
			for(HAPExecutableScript script : this.m_elements) {
				if(id.equals(script.getName())) {
					out = script;
					break;
				}
			}
		}
		else if(id instanceof Integer) {
			int index = (Integer)id;
			out = this.m_elements.get(index);
		}
		return out;
	}

}
