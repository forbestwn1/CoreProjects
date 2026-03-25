package com.nosliw.core.application.entity.datarule.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.brick.dataexpression.standalone.HAPBlockDataExpressionStandAloneImp;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicExpressionData;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicPluginProcessorEntityWithVariableDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicUtilityProcessorDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicWrapperOperand;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPManagerWithVariablePlugin;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRuleImp;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPPluginTransformerDataRuleExpression extends HAPPluginTransformerDataRuleImp{

	private HAPManagerWithVariablePlugin m_withVariableMan;

	public HAPPluginTransformerDataRuleExpression(HAPManagerWithVariablePlugin withVariableMan) {
		this.m_withVariableMan = withVariableMan;
	}
	
	@Override
	public HAPEntityOrReference transformDataRule(HAPDataRule dataRule, HAPDomainValueStructure valueStructureDomian) {
		HAPDataRuleExpression expressionDataRule = (HAPDataRuleExpression)dataRule;

		HAPBlockDataExpressionStandAloneImp brick = new HAPBlockDataExpressionStandAloneImp();

		HAPInteractiveExpression interactive = this.buildValuePortGroupForRuleTaskBrickExpression(expressionDataRule, brick, valueStructureDomian); 
		
		HAPDefinitionDataExpressionStandAlone dataExpressionStandAloneDef = new HAPDefinitionDataExpressionStandAlone(); 
		dataExpressionStandAloneDef.setExpression(expressionDataRule.getExpressionDefinition());		
		dataExpressionStandAloneDef.setExpressionInteractive(interactive);
		
		HAPDataExpressionStandAlone dataExpressionStandAloneExe = brick.getValue();
		dataExpressionStandAloneExe.setExpression(new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildBasicOperand(dataExpressionStandAloneDef.getExpression().getOperand())));
		
		//interactive request
		dataExpressionStandAloneExe.setExpressionInteractive(new HAPInteractiveExpression(dataExpressionStandAloneDef.getRequestParms(), dataExpressionStandAloneDef.getResult()));

		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(brick, valueStructureDomian);

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpressionStandAloneExe.getExpression(), varInfoContainer, null, this.m_withVariableMan, null);
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpressionStandAloneExe.getExpression(), varInfoContainer, this.m_withVariableMan);
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, valueStructureDomian);
		
		HAPBasicExpressionData dataExpression = (HAPBasicExpressionData)dataExpressionStandAloneExe.getExpression();
		HAPBasicWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT, dataExpressionStandAloneExe.getExpressionInterface().getResult().getDataDefinition().getCriteria());
		Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainer, this.m_withVariableMan);
		varInfoContainer = discoverResult.getLeft();
		
		//update value port element according to var info container after discovery
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, valueStructureDomian);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(dataExpressionStandAloneExe.getExpressionInterface().getResult().getDataDefinition().getCriteria()==null) {
			dataExpressionStandAloneExe.getExpressionInterface().getResult().getDataDefinition().setCriteria(resultCriteria);
		}
		else {
			dataExpressionStandAloneExe.setResultMatchers(discoverResult.getRight().get(HAPBasicPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
		
		return brick;
	}

}
