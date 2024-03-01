package com.nosliw.data.core.domain.entity.expression.data;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionWrapperValueStructure;
import com.nosliw.data.core.operand.HAPUtilityOperand;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.valuestructure1.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public class HAPUtilityExpression {

	public static HAPDefinitionWrapperValueStructure getValueStructure(Object expressionGroupDef, HAPRuntimeEnvironment runtimeEnv) {
		return HAPUtilityComponent.getValueStructure(expressionGroupDef, runtimeEnv);
	}
	
	//make global name
	public static HAPUpdateName getUpdateNameGlobal(HAPExecutableEntityExpressionDataGroup expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	//get local name according to global name
	public static String getLocalName(HAPExecutableEntityExpressionDataGroup expression, String globalName) {
		return globalName.substring((expression.getId()+"_").length());
	}

	public static HAPVariableInfoInStructure discoverDataVariablesDefinitionInStructure(HAPExecutableEntityExpressionDataGroup expressionGroup) {
		HAPVariableInfoInStructure allVarInfosInStructure = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(expressionGroup.getValueStructureDefinitionWrapper().getValueStructure());

		Map<String, HAPInfoVariable> allVarInfosInExpression = new LinkedHashMap<String, HAPInfoVariable>(); 
		for(HAPExecutableExpressionData expression : expressionGroup.getAllExpressionItems().values()) {
			for(String varName : HAPUtilityOperand.discoverVariableNames(expression.getOperand())) {
				HAPInfoVariable varInfo = allVarInfosInStructure.getVariableInfoByAlias(varName);
				if(allVarInfosInExpression.get(varInfo.getIdPath().getFullName())==null) {
					allVarInfosInExpression.put(varInfo.getIdPath().getFullName(), varInfo);
				}
			}
		}
		
		HAPVariableInfoInStructure out = new HAPVariableInfoInStructure();
		for(HAPInfoVariable varInfo : allVarInfosInExpression.values()) {
			out.addVariableCriteriaInfo(varInfo);
		}
		return out;
	}
	
	public static Set<String> discoverDataVariablesNameInExpression(HAPExecutableEntityExpressionDataGroup expressionGroup){
		Set<String> out = new HashSet<String>();
		for(HAPExecutableExpressionData expression : expressionGroup.getAllExpressionItems().values()) {
			out.addAll(HAPUtilityOperand.discoverVariableNames(expression.getOperand()));
		}
		return out;
	}
	
	public static Set<String> discoverDataVariablesIdInExpression(HAPExecutableEntityExpressionDataGroup expressionGroup){
		Set<String> out = new HashSet<String>();
		for(HAPExecutableExpressionData expression : expressionGroup.getAllExpressionItems().values()) {
			out.addAll(HAPUtilityOperand.discoverVariableKeys(expression.getOperand()));
		}
		return out;
	}
	
}
