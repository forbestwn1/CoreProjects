package com.nosliw.core.application.common.dataexpression.imp.basic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPPluginProcessorEntityWithVariable;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPDataTypeCriteriaAny;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@Component
public class HAPBasicPluginProcessorEntityWithVariableDataExpression implements HAPPluginProcessorEntityWithVariable<HAPBasicExpressionData>{

	public static String RESULT = "result";

	private HAPManagerResource m_resourceMan;
	private HAPDataTypeHelper m_dataTypeHelper;
	
	public HAPBasicPluginProcessorEntityWithVariableDataExpression(HAPManagerResource resourceMan, HAPDataTypeHelper dataTypeHelper) {
		this.m_resourceMan = resourceMan;
		this.m_dataTypeHelper = dataTypeHelper;
	}
	
	@Override
	public String getEntityType() {
		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_DATAEXPRESSION;
	}

	@Override
	public Set<String> getVariableKeys(HAPBasicExpressionData dataExpression){
		Set<String> out = new HashSet<String>();

		HAPBasicUtilityOperand.traverseAllOperand(dataExpression.getOperandWrapper(), new HAPBasicHandlerOperand() {
			@Override
			public boolean processOperand(HAPBasicWrapperOperand operand, Object data) {
				Set<String> varKeys = (Set<String>)data;
				String opType = operand.getOperandType();
				if(opType.equals(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE)){
					HAPBasicOperandVariable variableOperand = (HAPBasicOperandVariable)operand.getOperand();
					varKeys.add(variableOperand.getVariableKey());
				}
				return true;
			}
		}, out);
		
		return out;
	}

	
	@Override
	public void resolveVariable(HAPBasicExpressionData dataExpression, HAPContainerVariableInfo varInfoContainer,
			HAPConfigureResolveElementReference resolveConfigure, HAPRuntimeInfo runtimeInfo) {
		
		HAPBasicUtilityProcessorDataExpression.resolveVariable(dataExpression, varInfoContainer, resolveConfigure);
		
		//process reference operand
		HAPBasicUtilityProcessorDataExpression.resolveReferenceVariableMapping(dataExpression, this.m_resourceMan, runtimeInfo);
		
	}

	@Override
	public Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverVariableCriteria(
			HAPBasicExpressionData dataExpression, Map<String, HAPDataTypeCriteria> expections,
			HAPContainerVariableInfo varInfoContainer) {
		List<HAPBasicOperand> operands = new ArrayList<HAPBasicOperand>();
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
		varInfoContainer = HAPBasicUtilityOperand.discover(
				operands,
				expectOutputs,
				varInfoContainer,
				matchers,
				this.m_dataTypeHelper,
				new HAPProcessTracker());
		
		Map<String, HAPMatchers> outMatchers = new LinkedHashMap<String, HAPMatchers>();
		outMatchers.put(RESULT, matchers.get(0));
		return Pair.of(varInfoContainer, outMatchers);
	}

}
