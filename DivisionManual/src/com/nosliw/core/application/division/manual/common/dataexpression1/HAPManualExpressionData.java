package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.withvariable.HAPWithVariableImp;

public class HAPManualExpressionData extends HAPWithVariableImp implements HAPExpressionData{

	private HAPManualWrapperOperand m_operand;
	
	public HAPManualExpressionData(HAPManualOperand operand) {
		this.m_operand = new HAPManualWrapperOperand(operand);
	}
	
	@Override
	public String getWithVariableEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_DATAEXPRESSION;
	}

	@Override
	public HAPOperand getOperand() {   return this.m_operand.getOperand();  }
	public HAPManualWrapperOperand getOperandWrapper() {   return this.m_operand;     }

	@Override
	public void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERAND, HAPManagerSerialize.getInstance().toStringValue(this.getOperand(), HAPSerializationFormat.JAVASCRIPT));
	}

}
