package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.updatename.HAPUpdateNameMap;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPDefinitionResultActivityNormal;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafValue;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
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
			Map<String, HAPExecutableDataAssociationGroup> processResults,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPEnvContextProcessor envContextProcessor,
			HAPProcessContext processContext) {
		 
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, (HAPExpressionActivityDefinition)activityDefinition);

		//process input and create flat input context for activity
		out.setInputDataAssociation(HAPUtilityProcess.processDataAssociation(parentContext, out.getExpressionActivityDefinition().getInput(), envContextProcessor));
		
		//input context
		HAPContextFlat activityContext = out.getInputContext();
		
		//script expression process
		HAPProcessContextScriptExpression expProcessContext = out.getScriptExpressionProcessContext();
		//prepare constant value 
		expProcessContext.addConstants(activityContext.getConstantValue());
		//prepare variables 
		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(activityContext.getContext()));
		//process expression
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(out.getExpressionActivityDefinition().getExpression(), expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), envContextProcessor.expressionManager, envContextProcessor.runtime);
		out.setScriptExpression(scriptExpression);

		//find affected element to parent context
		Map<String, HAPVariableInfo> affectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();  //all the variables used in script expression
		for(String varName : relatedVarNames) {
			HAPVariableInfo affectVarInfo = out.getScriptExpressionProcessContext().getDataVariables().get(varName);
			HAPContextPath varPath = new HAPContextPath(varName);
			//affect sold variable 
			String solidVarRootName = activityContext.getSolidName(varPath.getRootElementId().getFullName());
			varPath = new HAPContextPath(new HAPContextDefinitionRootId(solidVarRootName), varPath.getSubPath());
			if(solidVarRootName!=null) {
				if(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE.equals(varPath.getRootElementId().getCategary())) {
					//mapped ele
					HAPContextDefinitionElement currentEle = activityContext.getContext().getElement(varPath.getRootElementId().getFullName()).getDefinition();
					String[] pathSegs = new HAPPath(varPath.getSubPath()).getPathSegs();
					int i = 0;
					while(!HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE.equals(currentEle.getType())&&currentEle!=null) {
						currentEle = currentEle.getChild(pathSegs[i]);
						i++;
					}
					HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)currentEle;
					HAPContextPath relativeElePath = relativeEle.getPath();
					String fullName = relativeElePath.getFullPath();
					for(;i<pathSegs.length; i++) {
						fullName = HAPNamingConversionUtility.buildPath(fullName, pathSegs[i]);
					}
					affectedVariablesInfo.put(fullName, affectVarInfo);
				}
				else {
					//inhereted ele
					affectedVariablesInfo.put(varPath.getFullPath(), affectVarInfo);
				}
			}
			else {
				//root variable does not exist, generate one
//				HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData(new HAPVariableInfo(out.getScriptExpressionProcessContext().getDataVariables().get(varName).getCriteria()));
//				affectedContext.addElement(varName, dataEle);
				HAPErrorUtility.invalid("");
			}
		}

		//affect parent context
		for(String basePath : affectedVariablesInfo.keySet()) {
			HAPContextPath cpath = new HAPContextPath(basePath);
			HAPContextDefinitionLeafData affectedEle = new HAPContextDefinitionLeafData(affectedVariablesInfo.get(basePath));
			HAPUtilityContext.setDescendant(parentContext, cpath.getRootElementId().getCategary(), cpath.getPath(), affectedEle);
		}

		//process success result
		String resultName = RESULT_SUCCESS;
		HAPDefinitionResultActivityNormal successResult = out.getExpressionActivityDefinition().getResult(resultName);
		HAPExecutableResultActivityNormal successResultExe = new HAPExecutableResultActivityNormal(successResult); 
		HAPContext successResultContext = new HAPContext();
		if(scriptExpression.isDataExpression()) {
			//if script expression is data expression only, then affect result
			HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
			HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
			successResultContext.addElement(HAPUtilityProcess.buildOutputVarialbeName(VARIABLE_OUTPUT), new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(outputCriteria)));
		}
		else {
			successResultContext.addElement(HAPUtilityProcess.buildOutputVarialbeName(VARIABLE_OUTPUT), new HAPContextDefinitionLeafValue());
		}
		//process output data association  output to variable
		HAPExecutableDataAssociationGroup outputDataAssociation = HAPUtilityProcess.processDataAssociation(successResultContext, successResult.getOutput(), envContextProcessor);
		successResultExe.setOutputDataAssociation(outputDataAssociation);
		
		out.addResult(resultName, successResultExe);
		
		//process result
		if(successResultExe.getOutputContext()!=null) {
			Map<String, String> nameMapping = new LinkedHashMap<String, String>();
			HAPContext outputContext = out.getOutputContext().getContext();
			for(String rootName : outputContext.getElementNames()) {
				//merge back to parent context
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(rootName), parentContext, null, null);
//				HAPContextDefinitionElement mergedEle = HAPUtilityContext.mergeDataElement(resolvedInfo.rootNode.getDefinition(), outputContext.getElement(rootName).getDefinition());
				HAPUtilityContext.setDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), rootName, outputContext.getElement(rootName).getDefinition());
				
				nameMapping.put(rootName, resolvedInfo.path.getRootElementId().getFullName());
			}
			
			//update variable names in output 
			out.getOutputDataAssociation().updateOutputRootName(new HAPUpdateNameMap(nameMapping));
		}
		
		return out;
	}
	
	private HAPContext buildSuccessResultContext(HAPExpressionActivityExecutable expressionActExt) {
		HAPScriptExpression scriptExpression = expressionActExt.getScriptExpression();
		HAPContext successResultContext = new HAPContext();
		if(scriptExpression.isDataExpression()) {
			//if script expression is data expression only, then affect result
			HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
			HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
			successResultContext.addElement(HAPUtilityProcess.buildOutputVarialbeName(VARIABLE_OUTPUT), new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(outputCriteria)));
		}
		else {
			successResultContext.addElement(HAPUtilityProcess.buildOutputVarialbeName(VARIABLE_OUTPUT), new HAPContextDefinitionLeafValue());
		}
		return successResultContext;
	}
	
	//process result
	private HAPExecutableResultActivityNormal processResult(HAPExecutableActivityNormal activity, String resultName, HAPBuilderResultContext resultContextBuilder, HAPEnvContextProcessor envContextProcessor) {
		//process success result
		HAPDefinitionResultActivityNormal resultDef = ((HAPDefinitionActivityNormal)activity.getActivityDefinition()).getResult(resultName);
		HAPExecutableResultActivityNormal resultExe = new HAPExecutableResultActivityNormal(resultDef); 
		//result context
		HAPContext resultContext = resultContextBuilder.buildResultContext(activity);
		//process output data association  output to variable
		HAPExecutableDataAssociationGroup outputDataAssociation = HAPUtilityProcess.processDataAssociation(resultContext, resultDef.getOutput(), envContextProcessor);
		resultExe.setOutputDataAssociation(outputDataAssociation);

		//process result
		if(successResultExe.getOutputContext()!=null) {
			Map<String, String> nameMapping = new LinkedHashMap<String, String>();
			HAPContext outputContext = out.getOutputContext().getContext();
			for(String rootName : outputContext.getElementNames()) {
				//merge back to parent context
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(rootName), parentContext, null, null);
//				HAPContextDefinitionElement mergedEle = HAPUtilityContext.mergeDataElement(resolvedInfo.rootNode.getDefinition(), outputContext.getElement(rootName).getDefinition());
				HAPUtilityContext.setDescendant(parentContext, resolvedInfo.path.getRootElementId().getCategary(), rootName, outputContext.getElement(rootName).getDefinition());
				
				nameMapping.put(rootName, resolvedInfo.path.getRootElementId().getFullName());
			}
			
			//update variable names in output 
			out.getOutputDataAssociation().updateOutputRootName(new HAPUpdateNameMap(nameMapping));
		}
		
		
		
		
		return resultExe;
		
		out.addResult(resultName, successResultExe);
		
	}
}
