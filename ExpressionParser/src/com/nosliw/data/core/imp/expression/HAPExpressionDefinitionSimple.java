package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionDefinitionSimple implements HAPExpressionDefinition{

	private String m_name; 
	
	private String m_expression;
	
	private Map<String, HAPData> m_constants;
	
	private Map<String, HAPDataTypeCriteria> m_variableCriterias; 
	
	private Map<String, HAPReferenceInfo> m_references;
	
	private HAPInfo m_info;
	
	private HAPOperand m_operand;
	
	HAPExpressionDefinitionSimple(){}
	
	HAPExpressionDefinitionSimple(
			String expression, 
			String name, 
			Map<String, HAPData> constants,
			Map<String, HAPDataTypeCriteria> variableCriterias, 
			Map<String, HAPReferenceInfo> references, 
			HAPInfo info){
		this.m_expression = expression;
		this.m_name = name;
		this.m_constants = constants;
		this.m_variableCriterias = variableCriterias;
		this.m_references = references;
		this.m_info = info;
	}
	
	@Override
	public String getName() {		return this.m_name;	}
	@Override
	public void setName(String name) {  this.m_name = name;  }

	@Override
	public HAPInfo getInfo() {		return this.m_info;	}

	@Override
	public String getExpression() {		return this.m_expression;	}

	@Override
	public Map<String, HAPData> getConstants() {		return this.m_constants;	}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariableCriterias() {		return this.m_variableCriterias;	}
	@Override
	public void setVariableCriterias(Map<String, HAPDataTypeCriteria> varCriterias) {
		this.m_variableCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.m_variableCriterias.putAll(varCriterias);
	}

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {		return this.m_references;	}

	@Override
	public HAPOperand getOperand() {   return this.m_operand;  }
	public void setOperand(HAPOperand operand){   this.m_operand = operand;   }

	@Override
	public HAPExpressionDefinition cloneExpressionDefinition() {
		HAPExpressionDefinitionSimple out = new HAPExpressionDefinitionSimple();
		
		out.m_name = this.m_name;
		out.m_expression = this.m_expression;
		
		if(this.m_constants!=null){
			out.m_constants = new LinkedHashMap<String, HAPData>();
			out.m_constants.putAll(this.m_constants);
		}
		
		if(this.m_variableCriterias!=null){
			out.m_variableCriterias = new LinkedHashMap<String, HAPDataTypeCriteria>();
			out.m_variableCriterias.putAll(this.m_variableCriterias);
		}
		
		if(this.m_references!=null){
			out.m_references = new LinkedHashMap<String, HAPReferenceInfo>();
			out.m_references.putAll(this.m_references);
		}
		
		out.m_info = this.m_info;
		if(this.m_operand!=null)  out.m_operand = this.m_operand.cloneOperand();
		
		return out;
	}
}
