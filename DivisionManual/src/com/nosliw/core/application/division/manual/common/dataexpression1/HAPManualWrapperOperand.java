package com.nosliw.core.application.division.manual.common.dataexpression1;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

/**
 * Wrapper of operand 
 * It is introduced so that we can replace operand with another one without knowing its parent during expression processing
 */
public class HAPManualWrapperOperand extends HAPSerializableImp{

	private HAPManualOperand m_operand;
	
	public HAPManualWrapperOperand(HAPManualOperand operand){
		this.m_operand = operand;
	}
	
	public HAPManualWrapperOperand(){}
	
	public HAPManualOperand getOperand(){		return this.m_operand;	}
	
	public void setOperand(HAPManualOperand operand){	this.m_operand = operand;	}

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
	
	public HAPManualWrapperOperand cloneWrapper(){
		return new HAPManualWrapperOperand(this.getOperand().cloneOperand());
	}
}
