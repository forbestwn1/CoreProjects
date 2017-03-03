package com.nosliw.data.core.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapperLiterate;

public class HAPOperandConstant implements HAPOperand{

	protected HAPData m_data;

	protected String m_name;
	
	public HAPOperandConstant(String constantStr){
		this.m_data = (HAPData)HAPSerializeManager.getInstance().buildObject(HAPDataWrapperLiterate.class.getName(), constantStr, HAPSerializationFormat.LITERATE);
		if(this.m_data==null){
			//not a valid data literate, then it is a constant name
			this.m_name = constantStr;
		}
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_CONSTANT;}
	
}
