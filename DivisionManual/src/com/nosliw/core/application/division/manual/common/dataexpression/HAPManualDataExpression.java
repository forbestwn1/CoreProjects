package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPWithVariable;

public class HAPManualDataExpression extends HAPSerializableImp implements HAPDataExpression{

	private HAPManualWrapperOperand m_operand;
	
	private Map<String, HAPIdElement> m_variablesInfo;

	public HAPManualDataExpression(HAPManualOperand operand) {
		this.m_variablesInfo = new LinkedHashMap<String, HAPIdElement>();
		this.m_operand = new HAPManualWrapperOperand(operand);
	}
	
	@Override
	public HAPOperand getOperand() {   return this.m_operand.getOperand();  }
	public HAPManualWrapperOperand getOperandWrapper() {   return this.m_operand;     }

	@Override
	public Map<String, HAPIdElement> getVariablesInfo() {   return this.m_variablesInfo;   }
	public void setVariableInfo(String key, HAPIdElement varId) {    this.m_variablesInfo.put(key, varId);       }

	@Override
	public void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(OPERAND, HAPManagerSerialize.getInstance().toStringValue(this.getOperand(), HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(HAPWithVariable.VARIABLEINFOS, HAPUtilityJson.buildJson(this.m_variablesInfo, HAPSerializationFormat.JSON));
	}
}