package com.nosliw.data.core.process.activity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionDataAssociationGroupExecutable;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPResultActivityNormal;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPExecutableProcess processExe,
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionDataAssociationGroupExecutable> processResults,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPEnvContextProcessor envContextProcessor,
			HAPProcessContext processContext) {
		 
		HAPExpressionActivityDefinition expActivityDef = (HAPExpressionActivityDefinition)activityDefinition;
		
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, activityDefinition);

		//process input and create flat var context
		HAPDefinitionDataAssociationGroupExecutable inputDataAssociation = HAPUtilityProcess.processDataAssociation(parentContext, expActivityDef.getInput(), envContextProcessor);
		out.setInputDataAssociation(inputDataAssociation);
		
		HAPContextFlat varContext = inputDataAssociation.getContext();
		
		HAPProcessContextScriptExpression expProcessContext = new HAPProcessContextScriptExpression();
		//prepare constant value
		Map<String, Object> constantsValue = varContext.getConstantValue();
		out.setConstants(constantsValue);
		//constants for expression		
		for(String name : constantsValue.keySet()) {
			expProcessContext.addConstant(name, constantsValue.get(name));
		}
		
		//prepare variables 
		Map<String, HAPVariableInfo> varsInfo = HAPUtilityContext.discoverDataVariablesInContext(varContext.getContext());
		for(String varName : varsInfo.keySet()) {
			expProcessContext.addVariable(varName, varsInfo.get(varName));
		}
		
		//discover expression
		HAPDefinitionScriptExpression scriptExpressionDefinition = expActivityDef.getExpression();
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(scriptExpressionDefinition, expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), envContextProcessor.expressionManager, envContextProcessor.runtime);
		out.setScriptExpression(scriptExpression);
		
		//result
		HAPDefinitionDataAssociationGroupExecutable outputDataAssociation = null;
		if(scriptExpression.isDataExpression()) {
			//if script expression is data expression only, then affect result
			HAPContext internalContext = new HAPContext();
			HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
			HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
			HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData();
			dataEle.setCriteria(new HAPVariableInfo(outputCriteria));
			HAPContextDefinitionRoot root = new HAPContextDefinitionRoot(dataEle);
			internalContext.addElement("output", root);
			
			HAPResultActivityNormal result = expActivityDef.getResults().get("success");
			
			//process output
			outputDataAssociation = HAPUtilityProcess.processDataAssociation(internalContext, result.getOutput(), envContextProcessor);
		}
		
		//merge variable criteria back to flat context
		Map<String, HAPVariableInfo> disVarInfo = expProcessContext.getDataVariables();
		Set<String> relatedVarNames = new HashSet<String>();
		for(String varName : disVarInfo.keySet()) {
			HAPComplexPath cpath = new HAPComplexPath(varName);
			cpath = new HAPComplexPath(varContext.getSolidName(cpath.getRootName()), cpath.getPath());
			
			HAPContextDefinitionElement ele = HAPUtilityContext.getDescendant(varContext.getContext(), cpath.getFullName());
			HAPContextDefinitionElement solidEle = ele.getSolidContextDefinitionElement();
			if(solidEle.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPContextDefinitionLeafData dataEle = (HAPContextDefinitionLeafData)solidEle;
				dataEle.getCriteria().setCriteria(disVarInfo.get(varName).getCriteria());
				relatedVarNames.add(varName);
			}
		}
		
		Set<String> relatedRootVarNames = new HashSet<String>();
		for(String name : relatedVarNames) {
			HAPComplexPath cpath = new HAPComplexPath(name);
			relatedRootVarNames.add(cpath.getRootName());
		}
		
		//from flat context build context group
		HAPContextGroup processedContextGroup = HAPUtilityContext.buildContextGroupFromFlatContext(varContext, relatedRootVarNames);
		
		//merge back to parent context
		Map<String, HAPContextDefinitionElement> mapped = new LinkedHashMap<String, HAPContextDefinitionElement>();
		HAPContext helpContext = processedContextGroup.removeContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		HAPDefinitionDataAssociationGroup inputDataAssocation = expActivityDef.getInput();
		for(String inputName : inputDataAssocation.getElementNames()) {
			HAPContextDefinitionRoot root = inputDataAssocation.getElement(inputName);
			HAPUtilityContext.processContextDefElementWithPathInfo(root.getDefinition(), new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
						String path = (String)value;
						HAPContextDefinitionElement resolvedEle = HAPUtilityContext.getDescendant(helpContext, path).getSolidContextDefinitionElement();
						HAPInfoRelativeContextResolve resolved = HAPUtilityContext.resolveReferencedParentContextNode(relativeEle.getPath(), processedContextGroup, null, null);
						mapped.put(resolved.path.getFullPath(), resolvedEle);
					}
					return false;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {
					return false;
				}} , "");
		}
		
		for(String basePath : mapped.keySet()) {
			HAPContextDefinitionElement processedEle = mapped.get(basePath);
			HAPContextPath cpath = new HAPContextPath(basePath);
			HAPContextDefinitionElement originalEle = HAPUtilityContext.getDescendant(processedContextGroup, cpath.getRootElementId().getCategary(), cpath.getPath());
			originalEle = HAPUtilityContext.mergeDataElement(originalEle, processedEle);
			HAPUtilityContext.setDescendant(parentContext, cpath.getRootElementId().getCategary(), cpath.getPath(), originalEle);
		}
		
		//process result
		if(outputDataAssociation!=null) {
			out.setOutputDataAssociation(outputDataAssociation);
			HAPContext outputContext = outputDataAssociation.getContext().getContext();
			for(String rootName : outputContext.getElementNames()) {
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(rootName), parentContext, null, null);
				HAPContextDefinitionElement mergedEle = HAPUtilityContext.mergeDataElement(resolvedInfo.rootNode.getDefinition(), outputContext.getElement(rootName).getDefinition());
				HAPUtilityContext.setDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), rootName, mergedEle);
			}
		}
		
		
		return out;
	}
}
