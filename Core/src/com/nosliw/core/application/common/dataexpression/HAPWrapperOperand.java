package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Wrapper of operand 
 * It is introduced so that we can replace operand with another one without knowing its parent during expression processing
 */
public class HAPWrapperOperand extends HAPSerializableImp{

	private HAPOperand m_operand;
	
	public HAPWrapperOperand(HAPOperand operand){
		this.m_operand = operand;
	}
	
	public HAPWrapperOperand(){}
	
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
	
	public HAPWrapperOperand cloneWrapper(){
		return new HAPWrapperOperand(this.getOperand().cloneOperand());
	}
}
