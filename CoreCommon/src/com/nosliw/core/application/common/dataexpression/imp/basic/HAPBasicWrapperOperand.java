package com.nosliw.core.application.common.dataexpression.imp.basic;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Wrapper of operand 
 * It is introduced so that we can replace operand with another one without knowing its parent during expression processing
 */
public class HAPBasicWrapperOperand extends HAPSerializableImp{

	private HAPBasicOperand m_operand;
	
	public HAPBasicWrapperOperand(HAPBasicOperand operand){
		this.m_operand = operand;
	}
	
	public HAPBasicWrapperOperand(){}
	
	public HAPBasicOperand getOperand(){		return this.m_operand;	}
	
	public void setOperand(HAPBasicOperand operand){	this.m_operand = operand;	}

	public String getOperandType() {   return this.getOperand().getType();     }
	
	@Override
	public String toStringValue(HAPSerializationFormat format) {
		return this.m_operand.toStringValue(format);
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		return false;
	}
	
	public boolean isEmpty(){ return this.m_operand==null;  }
	
	public HAPBasicWrapperOperand cloneWrapper(){
		return new HAPBasicWrapperOperand(this.getOperand().cloneOperand());
	}
}
