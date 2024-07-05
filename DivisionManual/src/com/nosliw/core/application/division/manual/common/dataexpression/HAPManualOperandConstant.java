package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPOperandConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandConstant;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;

public class HAPManualOperandConstant extends HAPManualOperand implements HAPOperandConstant{

	private HAPData m_data;
	
	private String m_name;
	
	public HAPManualOperandConstant(HAPDefinitionOperandConstant operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT, operandDefinition);
		
		String stringValue = operandDefinition.getStringValue();
		HAPData data = HAPUtilityData.buildDataWrapper(stringValue);
		if(data==null){
			//not a valid data literate, then it is a constant name
			this.m_name = stringValue;
		}
		else{
			this.m_data = data;
		}
	}

	@Override
	public HAPData getData() {   return this.m_data;   }


}
