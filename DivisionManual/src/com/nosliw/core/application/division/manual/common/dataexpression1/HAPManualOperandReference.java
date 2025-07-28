package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandReference;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualOperandReference extends HAPManualOperand implements HAPOperandReference{

	private HAPResourceId m_referedDataExpressionLibElementResourceId;
	
	private Map<String, HAPManualWrapperOperand> m_variableMapping;

	private Map<String, HAPIdElement> m_resolvedVariable;
	private Map<String, HAPDataTypeCriteria> m_resolvedVariableCriteria;

	private Map<String, HAPMatchers> m_matchers;

	public HAPManualOperandReference(HAPDefinitionOperandReference operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE, operandDefinition);
		this.m_variableMapping = new LinkedHashMap<String, HAPManualWrapperOperand>();
		this.m_resolvedVariable = new LinkedHashMap<String, HAPIdElement>();
		this.m_resolvedVariableCriteria = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
	}

	@Override
	public HAPResourceId getResourceId() {    return this.m_referedDataExpressionLibElementResourceId;     }
	public void setResourceId(HAPResourceId resourceId) {   this.m_referedDataExpressionLibElementResourceId = resourceId;     }

	@Override
	public Map<String, HAPOperand> getMapping(){
		Map<String, HAPOperand> out = new LinkedHashMap<String, HAPOperand>();
		for(String name : this.m_variableMapping.keySet()) {
			out.put(name, this.m_variableMapping.get(name).getOperand());
		}
		return out;
	}
	public void addMapping(String varName, HAPManualOperand operand) {	this.m_variableMapping.put(varName, this.createOperandWrapper(operand));	}

	public void addResolvedVariable(String varName, HAPIdElement varId, HAPDataTypeCriteria varCriteria) {	
		this.m_resolvedVariable.put(varName, varId);	
		this.m_resolvedVariableCriteria.put(varName, varCriteria);
	}

	@Override
	public Map<String, HAPMatchers> getMatchers(){	return this.m_matchers;	}
	@Override
	public Map<String, HAPIdElement> getResolvedVariable(){   return this.m_resolvedVariable;     }
	
	@Override
	public List<HAPManualWrapperOperand> getChildren(){   
		List<HAPManualWrapperOperand> out = new ArrayList<HAPManualWrapperOperand>();
		for(String name : this.m_variableMapping.keySet()) {
			out.add(this.m_variableMapping.get(name));
		}
		return out;
	}
	
	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		
		for(String name : this.m_variableMapping.keySet()) {
			HAPMatchers matchers = this.m_variableMapping.get(name).getOperand().discover(variablesInfo, this.m_resolvedVariableCriteria.get(name), processTracker, dataTypeHelper);
			if(matchers!=null) {
				this.m_matchers.put(name, matchers);
			}
		}
		
		return HAPUtilityCriteria.isMatchable(this.getOutputCriteria(), expectCriteria, dataTypeHelper);
	}

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOURCEID, this.m_referedDataExpressionLibElementResourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VARMAPPING, HAPUtilityJson.buildJson(this.m_variableMapping, HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(VARRESOLVE, HAPUtilityJson.buildJson(this.m_resolvedVariable, HAPSerializationFormat.JSON));
		jsonMap.put(VARMATCHERS, HAPUtilityJson.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
	}
	
}
