package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableScriptGroup extends HAPExecutableImp{

	private HAPExecutableExpressionGroup m_expressionExe;
	
	private List<HAPExecutableScriptEntity> m_elements;
	
//	private Map<String, Object> m_constants;
	
	public HAPExecutableScriptGroup() {
		this.m_elements = new ArrayList<HAPExecutableScriptEntity>();
//		this.m_constants = new LinkedHashMap<String, Object>();
	}
	
	public HAPExecutableExpressionGroup getExpression() {    return this.m_expressionExe;   }
	public void setExpression(HAPExecutableExpressionGroup expression) {    this.m_expressionExe = expression;    }
	
//	public Map<String, Object> getConstantsValue(){    return this.m_constants;     }
//	public void addConstants(Map<String, Object> constants) {  this.m_constants.putAll(constants);    }
	
	public void updateConstant(Map<String, Object> constants) {
		for(HAPExecutableScriptEntity scriptExe : this.m_elements) {
		}
		
		for(HAPExecutableExpression expressionExe : this.m_expressionExe.getExpressionItems().values()) {
			expressionExe.updateConstant(constants);
		}
	}
	
	public Set<HAPDefinitionConstant> getConstantsDefinition() {
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>(); 
		for(HAPExecutableScriptEntity scriptExe : this.m_elements) {
			HAPUtilityScriptExpression.addConstantDefinition(out, scriptExe.getConstantsDefinition());
		}
		
		for(HAPExecutableExpression expressionExe : this.m_expressionExe.getExpressionItems().values()) {
			HAPUtilityScriptExpression.addConstantDefinition(out, expressionExe.getConstantsDefinition());
		}
		return new HashSet<HAPDefinitionConstant>(out.values());
	}
	
	public void addScript(HAPExecutableScriptEntity script) {   this.m_elements.add(script);    }
	public HAPExecutableScriptEntity getScript(Object id) {
		HAPExecutableScriptEntity out = null;
		if(id==null)  id = HAPConstant.NAME_DEFAULT;
		if(id instanceof String) {
			for(HAPExecutableScriptEntity script : this.m_elements) {
				if(id.equals(script.getId())) {
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
