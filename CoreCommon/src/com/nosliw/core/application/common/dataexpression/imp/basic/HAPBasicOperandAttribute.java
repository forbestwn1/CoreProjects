package com.nosliw.core.application.common.dataexpression.imp.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPBasicOperandAttribute extends HAPBasicOperand implements HAPOperandAttribute{

	private String m_attribute;
	
	private HAPBasicWrapperOperand m_base;
	
	public HAPBasicOperandAttribute(HAPDefinitionOperandAttribute operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION, operandDefinition);
		this.m_attribute = operandDefinition.getAttribute();
	}

	@Override
	public String getAttribute() {    return this.m_attribute;    }

	@Override
	public HAPOperand getBase() {   return this.m_base.getOperand();    }   
	public void setBase(HAPBasicOperand base) {   this.m_base = new HAPBasicWrapperOperand(base);    }

	@Override
	public List<HAPBasicWrapperOperand> getChildren(){   
		List<HAPBasicWrapperOperand> out = new ArrayList<HAPBasicWrapperOperand>();
		out.add(this.m_base);
		return out;
	}
	
	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		return null;
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ATTRIBUTE, this.m_attribute);
		jsonMap.put(BASEDATA, HAPManagerSerialize.getInstance().toStringValue(this.getBase(), HAPSerializationFormat.JAVASCRIPT));
	}
}
