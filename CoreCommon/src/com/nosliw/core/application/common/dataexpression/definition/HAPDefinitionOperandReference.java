package com.nosliw.core.application.common.dataexpression.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionOperandReference extends HAPDefinitionOperand{

	//reference to expression (attachent name or resource id)
	private String m_reference;
	
	//mapping from this expression to referenced expression variable (ref variable id path --  source operand)
	private Map<String, HAPDefinitionOperand> m_variableMapping;
	
	private HAPDefinitionOperandReference(){
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE);
		this.m_variableMapping = new LinkedHashMap<String, HAPDefinitionOperand>();
	}
	
	public HAPDefinitionOperandReference(String reference){
		this();
		this.m_reference = reference;
	}

	public String getReference(){  return this.m_reference;  }
	
	public void addMapping(String varName, HAPDefinitionOperand operand) {	this.m_variableMapping.put(varName, operand);	}
	public Map<String, HAPDefinitionOperand> getMapping(){    return this.m_variableMapping;      }
	public void setMapping(Map<String, HAPDefinitionOperand> mapping) {    
		this.m_variableMapping.clear();
		this.m_variableMapping.putAll(mapping);
	}
}
