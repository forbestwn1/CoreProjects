package com.nosliw.data.core.process.activity;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.erro.HAPErrorUtility;
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
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	private final String VARIABLE_OUTPUT = "output";
	
	private final String RESULT_SUCCESS = "success";
	
	
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
		 
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, (HAPExpressionActivityDefinition)activityDefinition);

		//process input and create flat input context for activity
		out.setInputDataAssociation(HAPUtilityProcess.processDataAssociation(parentContext, out.getExpressionActivityDefinition().getInput(), envContextProcessor));
		
		//input context
		HAPContextFlat inputContext = out.getInput();
		
		//script expression process
		HAPProcessContextScriptExpression expProcessContext = out.getScriptExpressionProcessContext();
		//prepare constant value 
		expProcessContext.addConstants(inputContext.getConstantValue());
		//prepare variables 
		expProcessContext.addVariables(HAPUtilityContext.discoverDataVariablesInContext(inputContext.getContext()));
		//process expression
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(out.getExpressionActivityDefinition().getExpression(), expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), envContextProcessor.expressionManager, envContextProcessor.runtime);
		out.setScriptExpression(scriptExpression);
		
		//result
		HAPDefinitionDataAssociationGroupExecutable outputDataAssociation = null;
		if(scriptExpression.isDataExpression()) {
			//if script expression is data expression only, then affect result
			HAPContext internalContext = new HAPContext();
			HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
			HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
			HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData(new HAPVariableInfo(outputCriteria));
			HAPContextDefinitionRoot root = new HAPContextDefinitionRoot(dataEle);
			internalContext.addElement(HAPUtilityProcess.buildOutputVarialbeName(VARIABLE_OUTPUT), root);
			
			HAPResultActivityNormal result = out.getExpressionActivityDefinition().getResults().get(RESULT_SUCCESS);
			
			//process output
			outputDataAssociation = HAPUtilityProcess.processDataAssociation(internalContext, result.getOutput(), envContextProcessor);
			out.setOutputDataAssociation(outputDataAssociation);
		} 
		
		//merge variable criteria in script process context back to flat context
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();
		HAPContext affectedContext = new HAPContext();
		for(String varName : relatedVarNames) {
			HAPComplexPath varPath = new HAPComplexPath(varName);
			//affect sold variable 
			String solidVarRootName = inputContext.getSolidName(varPath.getRootName());
			if(solidVarRootName!=null) {
				varPath = new HAPComplexPath(solidVarRootName, varPath.getPath());
				//move root item to affected context
				affectedContext.addElement(solidVarRootName, inputContext.getContext().getElement(solidVarRootName));

				//change the criteria with new value
				HAPContextDefinitionElement ele = HAPUtilityContext.getDescendant(affectedContext, varPath.getFullName()).getSolidContextDefinitionElement();
				if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_DATA)) {
					HAPContextDefinitionLeafData dataEle = (HAPContextDefinitionLeafData)ele;
					dataEle.getCriteria().setCriteria(out.getScriptExpressionProcessContext().getDataVariables().get(varName).getCriteria());
				}
				else {
					HAPErrorUtility.invalid("");
				}
			}
			else {
				//root variable does not exist, generate one
//				HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData(new HAPVariableInfo(out.getScriptExpressionProcessContext().getDataVariables().get(varName).getCriteria()));
//				affectedContext.addElement(varName, dataEle);
				HAPErrorUtility.invalid("");
			}
		}
		
		//from flat context build context group
		HAPContextGroup affectedContextGroup = HAPUtilityContext.buildContextGroupFromContext(affectedContext);
		
		//merge back to parent context
		Map<String, HAPContextDefinitionElement> mapped = new LinkedHashMap<String, HAPContextDefinitionElement>();
		HAPContext helpContext = affectedContextGroup.removeContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		HAPDefinitionDataAssociationGroup inputDataAssocation = out.getExpressionActivityDefinition().getInput();
		for(String inputName : inputDataAssocation.getElementNames()) {
			HAPContextDefinitionRoot root = inputDataAssocation.getElement(inputName);
			HAPUtilityContext.processContextDefElementWithPathInfo(root.getDefinition(), new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
						String path = (String)value;
						HAPContextDefinitionElement resolvedEle = HAPUtilityContext.getDescendant(helpContext, path).getSolidContextDefinitionElement();
						HAPInfoRelativeContextResolve resolved = HAPUtilityContext.resolveReferencedParentContextNode(relativeEle.getPath(), affectedContextGroup, null, null);
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
