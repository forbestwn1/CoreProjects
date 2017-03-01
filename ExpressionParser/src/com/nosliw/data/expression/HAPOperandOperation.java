package com.nosliw.data.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeId;

public class HAPOperandOperation implements HAPOperand{

	//the data type operation defined on
	protected HAPDataTypeId m_dataTypeId;
	
	//base data
	protected HAPOperand m_base;

	//operation name
	protected String m_operation;
	
	//operation parms
	protected Map<String, HAPOperand> m_parms;

	public HAPOperandOperation(HAPOperand base, String operation, Map<String, HAPOperand> parms){
		this.m_base = base;
		this.m_operation = operation;
		this.m_parms = parms;
	}
	
	public HAPOperandOperation(String dataTypeId, String operation, Map<String, HAPOperand> parms){
		
	}

	
	public HAPDataTypeId getDataTypeId(){   return this.m_dataTypeId; }

	@Override
	public String getType() {		return HAPConstant.EXPRESSION_OPERAND_OPERATION;	}
	
	
}
