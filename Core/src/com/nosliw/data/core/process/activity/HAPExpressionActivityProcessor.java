package com.nosliw.data.core.process.activity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPResultActivityNormal;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expression.HAPContextScriptExpressionProcess;
import com.nosliw.data.core.script.expression.HAPScriptExpression;
import com.nosliw.data.core.script.expression.HAPUtilityScriptExpression;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPExecutableProcess processExe,
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionDataAssociationGroup> results,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPEnvContextProcessor envContextProcessor,
			HAPProcessContext processContext) {
		
		HAPExpressionActivityDefinition expActivityDef = (HAPExpressionActivityDefinition)activityDefinition;
		
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, activityDefinition);

		//process input and create flat var context
		HAPContextFlat varContext = HAPUtilityProcess.processActivityInput(parentContext, expActivityDef.getInput(), envContextProcessor);
		
		HAPContextScriptExpressionProcess expProcessContext = new HAPContextScriptExpressionProcess();
		//prepare constant value
		Map<String, Object> constantsValue = varContext.getConstantValue();
		out.setConstants(constantsValue);
		//constants for expression		
		for(String name : constantsValue.keySet()) {
			Object constantValue = constantsValue.get(name);
			if(constantValue instanceof HAPData) {
				expProcessContext.addConstant(name, (HAPData)constantValue);
			}
		}
		
		//prepare variables 
		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(varContext.getContext());
		for(String varName : varsInfo.keySet()) {
			expProcessContext.addVariable(varName, varsInfo.get(varName));
		}
		
		//discover expression
		HAPScriptExpression scriptExpression = expActivityDef.getExpression();
		scriptExpression.processExpressions(expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), envContextProcessor.expressionManager);
		
		//result
		if(scriptExpression.isDataExpression()) {
			HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
			HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
			HAPResultActivityNormal result = expActivityDef.getResults().get("success");
		}
		
		//map back to flat context
		Map<String, HAPVariableInfo> disVarInfo = expProcessContext.getVariables();
		for(String varName : disVarInfo.keySet()) {
			HAPContextDefinitionElement ele = HAPUtilityContext.getDescendant(varContext.getContext(), varName);
			HAPContextDefinitionElement solidEle = ele.getSolidContextDefinitionElement();
			if(solidEle.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPContextDefinitionLeafData dataEle = (HAPContextDefinitionLeafData)solidEle;
				dataEle.getCriteria().setCriteria(disVarInfo.get(varName).getCriteria());
			}
		}
		
		//from flat context map back to parent context
		for(String varName : varContext.getContext().getElementNames()) {
			String rootName;
			HAPContextDefinitionRoot root = expActivityDef.getInput().getElement(rootName);
			if(root!=null) {
				root.getDefinition().getChildUnilReltive(childPath, parentEle);
				
			}
			else {
				//
				parentContext.getAncestor();
			}
		}
		
		return out;
	}
}
