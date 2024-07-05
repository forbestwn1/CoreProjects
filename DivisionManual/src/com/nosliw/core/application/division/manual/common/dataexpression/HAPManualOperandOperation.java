package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandOperation;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandOperation;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPManualOperandOperation extends HAPManualOperand implements HAPOperandOperation{

	private HAPDataTypeId m_dataTypeId;
	
	private String m_operation; 
	
	private HAPManualWrapperOperand m_base;
	
	private Map<String, HAPManualWrapperOperand> m_parms;
	
	private Map<String, HAPMatchers> m_parmMatchers;
	
	public HAPManualOperandOperation(HAPDefinitionOperandOperation operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_OPERATION, operandDefinition);
		this.m_parmMatchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_dataTypeId = operandDefinition.getDataTypeId();
		this.m_operation = operandDefinition.getOperaion();
	}

	@Override
	public HAPOperand getBase() {   return this.m_base.getOperand();  }
	public void setBase(HAPManualOperand base) {   this.m_base = new HAPManualWrapperOperand(base);      }

	@Override
	public Map<String, HAPOperand> getParms() {
		Map<String, HAPOperand> out = new LinkedHashMap<String, HAPOperand>();
		for(String name : this.m_parms.keySet()) {
			out.put(name, this.m_parms.get(name).getOperand());
		}
		return out;
	}
	public void setParm(String name, HAPManualOperand parm) {
		this.m_parms.put(name, new HAPManualWrapperOperand(parm));
	}

	@Override
	public HAPDataTypeId getDataTypeId() {   return this.m_dataTypeId;   }

	@Override
	public String getOperaion() {   return this.m_operation;   }

	@Override
	public Map<String, HAPMatchers> getParmMatchers() {   return this.m_parmMatchers;   }

}
