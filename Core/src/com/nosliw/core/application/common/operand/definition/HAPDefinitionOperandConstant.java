package com.nosliw.core.application.common.operand.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;

public class HAPDefinitionOperandConstant extends HAPDefinitionOperand{

	protected HAPData m_data;

	protected String m_name;

	public HAPDefinitionOperandConstant(String constantStr) {
		super(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
		HAPData data = HAPUtilityData.buildDataWrapper(constantStr);
		if(data==null){
			//not a valid data literate, then it is a constant name
			this.setName(constantStr);
		}
		else{
			this.setData(data);
		}
	}

	public String getName(){  return this.m_name;  }
	public void setName(String name) {  this.m_name = name;   }
	
	public HAPData getData(){  return this.m_data;  }
	public void setData(HAPData data){ 	this.m_data = data;	}

}
