package com.nosliw.data.expression;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
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
	
	public HAPOperandOperation(String dataTypeIdLiterate, String operation, Map<String, HAPOperand> parms){
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);
		this.m_operation = operation;
		this.m_parms = parms;
	}

	
	public HAPDataTypeId getDataTypeId(){   return this.m_dataTypeId; }

	@Override
	public String getType() {		return HAPConstant.EXPRESSION_OPERAND_OPERATION;	}
	
}
