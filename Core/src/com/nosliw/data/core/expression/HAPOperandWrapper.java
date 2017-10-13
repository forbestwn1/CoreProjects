package com.nosliw.data.core.expression;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Wrapper of operand 
 * It is introduced so that we can replace operand with another one without knowing its parent during expression processing
 */
public class HAPOperandWrapper implements HAPSerializable{

	private HAPOperand m_operand;
	
	public HAPOperandWrapper(HAPOperand operand){
		this.m_operand = operand;
	}
	
	public HAPOperandWrapper(){}
	
	public HAPOperand getOperand(){
		return this.m_operand;
	}
	
	public void setOperand(HAPOperand operand){
		this.m_operand = operand;
	}

	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return this.m_operand.toStringValue(format);
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		return false;
	}
	
	public boolean isEmpty(){ return this.m_operand==null;  }
	
	public HAPOperandWrapper cloneWrapper(){
		return new HAPOperandWrapper(this.getOperand().cloneOperand());
	}
}
