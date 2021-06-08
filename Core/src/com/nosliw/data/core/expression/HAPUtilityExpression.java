package com.nosliw.data.core.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.updatename.HAPUpdateNamePrefix;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPInfoVariable;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

public class HAPUtilityExpression {

	public static HAPWrapperValueStructure getValueStructure(Object expressionGroupDef, HAPRuntimeEnvironment runtimeEnv) {
		return HAPUtilityComponent.getValueStructure(expressionGroupDef, HAPUtilityExpressionProcessConfigure.getContextProcessConfigurationForExpression(), runtimeEnv);
	}
	
	//make global name
	public static HAPUpdateName getUpdateNameGlobal(HAPExecutableExpressionGroup expression) {
		return new HAPUpdateNamePrefix(expression.getId()+"_");
	}
	
	//get local name according to global name
	public static String getLocalName(HAPExecutableExpressionGroup expression, String globalName) {
		return globalName.substring((expression.getId()+"_").length());
	}

	public static HAPVariableInfoInStructure discoverDataVariablesDefinitionInStructure(HAPExecutableExpressionGroupInSuite expressionGroup) {
		HAPVariableInfoInStructure allVarInfosInStructure = HAPUtilityValueStructure.discoverDataVariablesDefinitionInStructure(expressionGroup.getValueStructureDefinitionWrapper().getValueStructure());

		Map<String, HAPInfoVariable> allVarInfosInExpression = new LinkedHashMap<String, HAPInfoVariable>(); 
		for(HAPExecutableExpression expression : expressionGroup.getExpressionItems().values()) {
			for(String varName : HAPOperandUtility.discoverVariableNames(expression.getOperand())) {
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
	
	public static Set<String> discoverDataVariablesNameInExpression(HAPExecutableExpressionGroup expressionGroup){
		Set<String> out = new HashSet<String>();
		for(HAPExecutableExpression expression : expressionGroup.getExpressionItems().values()) {
			out.addAll(HAPOperandUtility.discoverVariableNames(expression.getOperand()));
		}
		return out;
	}
	
	public static Set<String> discoverDataVariablesIdInExpression(HAPExecutableExpressionGroup expressionGroup){
		Set<String> out = new HashSet<String>();
		for(HAPExecutableExpression expression : expressionGroup.getExpressionItems().values()) {
			out.addAll(HAPOperandUtility.discoverVariableIds(expression.getOperand()));
		}
		return out;
	}
	
}
