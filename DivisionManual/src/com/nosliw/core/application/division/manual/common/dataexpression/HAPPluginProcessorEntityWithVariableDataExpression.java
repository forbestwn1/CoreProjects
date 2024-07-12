package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorEntityWithVariableDataExpression implements HAPPluginProcessorEntityWithVariable{

	public static String RESULT = "result";
	
	private HAPRuntimeEnvironment m_runtimEnv;
	
	@Override
	public void resolveVariable(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer,
			HAPConfigureResolveElementReference resolveConfigure) {
		HAPManualDataExpression dataExpression = (HAPManualDataExpression)withVariableEntity;
		
		HAPManualUtilityProcessorDataExpression.resolveVariable(dataExpression, varInfoContainer, resolveConfigure);
		
		//process reference operand
		HAPManualUtilityProcessorDataExpression.resolveReferenceVariableMapping(dataExpression, m_runtimEnv);
		
	}

	@Override
	public Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverVariableCriteria(
			HAPWithVariable withVariableEntity, Map<String, HAPDataTypeCriteria> expections,
			HAPContainerVariableInfo varInfoContainer) {
		HAPManualDataExpression dataExpression = (HAPManualDataExpression)withVariableEntity;

		List<HAPManualOperand> operands = new ArrayList<HAPManualOperand>();
		operands.add(dataExpression.getOperandWrapper().getOperand());
		
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		HAPDataTypeCriteria resultExpection = expections.get(RESULT);
		if(resultExpection!=null) {
			expectOutputs.add(resultExpection);
		}
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		varInfoContainer = HAPManualUtilityOperand.discover(
				operands,
				expectOutputs,
				varInfoContainer,
				matchers,
				this.m_runtimEnv.getDataTypeHelper(),
				new HAPProcessTracker());
		
		Map<String, HAPMatchers> outMatchers = new LinkedHashMap<String, HAPMatchers>();
		outMatchers.put(RESULT, matchers.get(0));
		return Pair.of(varInfoContainer, outMatchers);
	}

}
