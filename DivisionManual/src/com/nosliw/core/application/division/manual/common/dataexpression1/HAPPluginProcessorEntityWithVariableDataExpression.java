package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorEntityWithVariableDataExpression implements HAPPluginProcessorEntityWithVariable{

	public static String RESULT = "result";
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginProcessorEntityWithVariableDataExpression(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public String getEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_DATAEXPRESSION;
	}

	@Override
	public Set<String> getVariableKeys(HAPWithVariable withVariableEntity){
		Set<String> out = new HashSet<String>();
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)withVariableEntity;

		HAPManualUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPManualHandlerOperand() {
			@Override
			public boolean processOperand(HAPManualWrapperOperand operand, Object data) {
				Set<String> varKeys = (Set<String>)data;
				String opType = operand.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPManualOperandVariable variableOperand = (HAPManualOperandVariable)operand.getOperand();
					varKeys.add(variableOperand.getVariableKey());
				}
				return true;
			}
		}, out);
		
		return out;
	}

	
	@Override
	public void resolveVariable(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer,
			HAPConfigureResolveElementReference resolveConfigure) {
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)withVariableEntity;
		
		HAPManualUtilityProcessorDataExpression.resolveVariable(dataExpression, varInfoContainer, resolveConfigure);
		
		//process reference operand
		HAPManualUtilityProcessorDataExpression.resolveReferenceVariableMapping(dataExpression, m_runtimeEnv);
		
	}

	@Override
	public Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverVariableCriteria(
			HAPWithVariable withVariableEntity, Map<String, HAPDataTypeCriteria> expections,
			HAPContainerVariableInfo varInfoContainer) {
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)withVariableEntity;

		List<HAPManualOperand> operands = new ArrayList<HAPManualOperand>();
		operands.add(dataExpression.getOperandWrapper().getOperand());
		
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		HAPDataTypeCriteria resultExpection = expections==null?null:expections.get(RESULT);
		if(resultExpection!=null) {
			expectOutputs.add(resultExpection);
		}
		else {
			expectOutputs.add(HAPDataTypeCriteriaAny.getCriteria()); 
		}
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		varInfoContainer = HAPManualUtilityOperand.discover(
				operands,
				expectOutputs,
				varInfoContainer,
				matchers,
				this.m_runtimeEnv.getDataTypeHelper(),
				new HAPProcessTracker());
		
		Map<String, HAPMatchers> outMatchers = new LinkedHashMap<String, HAPMatchers>();
		outMatchers.put(RESULT, matchers.get(0));
		return Pair.of(varInfoContainer, outMatchers);
	}

}
