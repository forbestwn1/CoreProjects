package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperandConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandConstant;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPUtilityData;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

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
	public void setData(HAPData data) {     this.m_data = data;           }
	
	public String getName() {    return this.m_name;   }

	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		//set output criteria
		if(this.getOutputCriteria()==null){
			HAPDataTypeCriteria criteria = dataTypeHelper.getDataTypeCriteriaByData(m_data);
			this.setOutputCriteria(criteria);
		}
		return HAPUtilityCriteria.isMatchable(this.getOutputCriteria(), expectCriteria, dataTypeHelper);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATA, HAPManagerSerialize.getInstance().toStringValue(this.m_data, HAPSerializationFormat.JSON));
	}
}
