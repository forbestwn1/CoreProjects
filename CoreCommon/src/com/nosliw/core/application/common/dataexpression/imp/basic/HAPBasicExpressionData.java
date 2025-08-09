package com.nosliw.core.application.common.dataexpression.imp.basic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;
import com.nosliw.core.xxx.application.valueport.HAPVariableInfo;

public class HAPBasicExpressionData extends HAPSerializableImp implements HAPExpressionData{

	private HAPBasicWrapperOperand m_operand;
	
	private Map<String, HAPVariableInfo> m_variablesInfo;

	public HAPBasicExpressionData(HAPBasicOperand operand) {
		this.m_variablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_operand = new HAPBasicWrapperOperand(operand);
	}
	
	@Override
	public String getWithVariableEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_DATAEXPRESSION;
	}

	@Override
	public HAPOperand getOperand() {   return this.m_operand.getOperand();  }
	public HAPBasicWrapperOperand getOperandWrapper() {   return this.m_operand;     }

	@Override
	public Map<String, HAPVariableInfo> getVariablesInfo() {   return this.m_variablesInfo;   }
	@Override
	public void addVariableInfo(HAPVariableInfo variableInfo) {  this.m_variablesInfo.put(variableInfo.getVariableKey(), variableInfo);  }

	@Override
	public void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERAND, HAPManagerSerialize.getInstance().toStringValue(this.getOperand(), HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(HAPWithVariable.VARIABLEINFOS, HAPUtilityJson.buildJson(this.m_variablesInfo, HAPSerializationFormat.JSON));
	}

}
