package com.nosliw.data.core.process.activity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExpressionProcessConfigureUtil;
import com.nosliw.data.core.process.HAPBuilderResultContext;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.process.HAPProcessorDataAssociation;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafValue;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;
import com.nosliw.data.core.script.expression.HAPProcessorScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	private HAPBuilderResultContext m_resultContextBuilder = new HAPBuilderResultContext1(); 
	 
	@Override
	public HAPExecutableActivity process(
			HAPDefinitionActivity activityDefinition, 
			String id, 
			HAPExecutableProcess processExe,
			HAPContextGroup parentContext, 
			Map<String, HAPExecutableDataAssociationGroup> processResults,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		 
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(id, (HAPExpressionActivityDefinition)activityDefinition);

		//process input and create flat input context for activity
		out.setInputDataAssociation(HAPProcessorDataAssociation.processDataAssociation(parentContext, out.getExpressionActivityDefinition().getInput(), contextProcessRequirement));
		
		//input context
		HAPContextFlat activityContext = out.getInputContext();
		
		//script expression process
		HAPProcessContextScriptExpression expProcessContext = out.getScriptExpressionProcessContext();
		//prepare constant value 
		expProcessContext.addConstants(activityContext.getConstantValue());
		//prepare variables 
		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(activityContext.getContext()));
		//process expression
		HAPScriptExpression scriptExpression = HAPProcessorScriptExpression.processScriptExpression(out.getExpressionActivityDefinition().getExpression(), expProcessContext, HAPExpressionProcessConfigureUtil.setDoDiscovery(null), contextProcessRequirement.expressionManager, contextProcessRequirement.runtime);
		out.setScriptExpression(scriptExpression);

		//find affected element to parent context
		Map<String, HAPVariableInfo> affectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<String> relatedVarNames = out.getScriptExpression().getDataVariableNames();  //all the variables used in script expression
		for(String varName : relatedVarNames) {
			HAPVariableInfo affectVarInfo = out.getScriptExpressionProcessContext().getDataVariables().get(varName);
			HAPContextPath varPath = new HAPContextPath(varName);
			//affect sold variable 
			String solidVarRootName = activityContext.getGlobalName(varPath.getRootElementId().getFullName());
			if(solidVarRootName!=null) {
				varPath = new HAPContextPath(new HAPContextDefinitionRootId(solidVarRootName), varPath.getSubPath());
				HAPContextDefinitionRoot currentRootEle = activityContext.getContext().getElement(varPath.getRootElementId().getFullName());
				if(HAPProcessorDataAssociation.isMappedRoot(currentRootEle)) {
//				if(HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE.equals(varPath.getRootElementId().getCategary())) {
					//mapped ele
					HAPContextDefinitionElement currentEle = currentRootEle.getDefinition();
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
			HAPUtilityContext.updateDataDescendant(parentContext, cpath.getRootElementId().getCategary(), cpath.getPath(), affectedEle);
		}

		HAPExecutableResultActivityNormal successResultExe = HAPUtilityProcess.processNormalActivityResult(out, HAPConstant.ACTIVITY_RESULT_SUCCESS, parentContext, m_resultContextBuilder, contextProcessRequirement);
		out.addResult(HAPConstant.ACTIVITY_RESULT_SUCCESS, successResultExe);
		
		return out;
	}

	class HAPBuilderResultContext1 implements HAPBuilderResultContext {
		@Override
		public HAPContext buildResultContext(String resultName, HAPExecutableActivityNormal activity) {
			HAPContext out = new HAPContext();
			if(HAPConstant.ACTIVITY_RESULT_SUCCESS.equals(resultName)) {
				String outputVar = HAPConstant.ACTIVITY_OUTPUTVARIABLE_OUTPUT;
				HAPExpressionActivityExecutable expressionActExt = (HAPExpressionActivityExecutable)activity;
				HAPScriptExpression scriptExpression = expressionActExt.getScriptExpression();
				if(scriptExpression.isDataExpression()) {
					//if script expression is data expression only, then affect result
					HAPExecutableExpression expExe = scriptExpression.getExpressions().values().iterator().next();
					HAPDataTypeCriteria outputCriteria = expExe.getOperand().getOperand().getOutputCriteria();
					out.addElement(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(outputCriteria)));
				}
				else {
					out.addElement(HAPUtilityProcess.buildOutputVarialbeName(outputVar), new HAPContextDefinitionLeafValue());
				}
			}
			return out;
		}
	}
}
