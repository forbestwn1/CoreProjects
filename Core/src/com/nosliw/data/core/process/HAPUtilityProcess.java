package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.process.resource.HAPProcessId;
import com.nosliw.data.core.process.resource.HAPResourceIdProcess;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.script.expression.HAPContextProcessScript;

public class HAPUtilityProcess {

	public static String buildOutputVarialbeName(String name) {
		return "nosliw_" + name;
	}
	
	public static HAPResourceId buildResourceId(String suiteId, String processId) {
		return new HAPResourceIdProcess(new HAPProcessId(suiteId, processId));
	}
	
//	public static HAPDefinitionProcessSuite buildProcessSuiteFromAttachment(HAPAttachmentContainer attachmentContainer, HAPManagerActivityPlugin activityPluginMan) {
//		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
//		Map<String, HAPAttachment> attachments = attachmentContainer.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS);
//		for(String id : attachments.keySet()) {
//			HAPAttachmentEntity entityAttachment = (HAPAttachmentEntity)attachments.get(id);
//			HAPDefinitionProcessSuiteElementEntity processDef = HAPParserProcessDefinition.parseProcess(entityAttachment.getEntityJsonObj(), activityPluginMan);
//			out.addContainerElement(processDef);
//		}
//		return out;
//	}

	public static void buildScriptExpressionProcessContext(HAPContext context, HAPContextProcessScript expProcessContext) {
		//prepare constant value 
		Map<String, Object> constantsValue = context.getConstantValue();
		for(String id : constantsValue.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
			expProcessContext.addConstantDefinition(constantDef);
		}
		expProcessContext.setContextStructure(context);
		
//		expProcessContext.addConstants(context.getConstantValue());
//		//prepare variables 
//		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(context));
	}

	public static void processNormalActivityInputDataAssocation(HAPExecutableActivityNormal activity, HAPDefinitionActivityNormal activityDefinition, HAPContextGroup processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPParentContext.createDefault(processContext), 
				activityDefinition.getInputMapping(), 
				HAPParentContext.createDefault(activityDefinition.getInputContextStructure(processContext)), 
				null, 
				runtimeEnv);
		activity.setInputDataAssociation(da);
	}
	
	public static void processBranchActivityInputDataAssocation(HAPExecutableActivityBranch activity, HAPDefinitionActivityBranch activityDefinition, HAPContextGroup processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPParentContext.createDefault(processContext), 
				activityDefinition.getInputMapping(), 
				HAPParentContext.createDefault(activityDefinition.getInputContextStructure(processContext)), 
				null, 
				runtimeEnv);
		activity.setInputDataAssociation(da);
	}
	
	//data variables infor in activity merge back to process context
	public static void mergeDataVariableInActivityToProcessContext(Map<String, HAPVariableInfo> activityVariablesInfo, HAPContext activityContext, HAPContextGroup processContext) {
		Map<String, HAPVariableInfo> expectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : expectedVariablesInfo.keySet()) {
			HAPVariableInfo expectedVarInfo = activityVariablesInfo.get(varName);
			HAPContextPath varPath = new HAPContextPath(varName);
			//affect global variable 
			HAPContextDefinitionRoot affectedRoot = activityContext.getElement(varPath.getRootElementId().getFullName());
			if(affectedRoot!=null) {
				//ele mapped from context variable
				HAPContextDefinitionElement currentEle = affectedRoot.getDefinition();
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
				expectedVariablesInfo.put(fullName, expectedVarInfo);
			}
			else {
				//root variable does not exist, generate one
//				HAPContextDefinitionLeafData dataEle = new HAPContextDefinitionLeafData(new HAPVariableInfo(out.getScriptExpressionProcessContext().getDataVariables().get(varName).getCriteria()));
//				affectedContext.addElement(varName, dataEle);
				HAPErrorUtility.invalid("");
			}
		}

		//affect parent context
		for(String basePath : expectedVariablesInfo.keySet()) {
			HAPContextPath cpath = new HAPContextPath(basePath);
			HAPContextDefinitionLeafData affectedEle = new HAPContextDefinitionLeafData(expectedVariablesInfo.get(basePath).getDataInfo());
			HAPUtilityContext.updateDataDescendant(processContext, cpath.getRootElementId().getCategary(), cpath.getPath(), affectedEle);
		}
	}
	
	//process result
	public static HAPExecutableResultActivityNormal processNormalActivityResult(
			HAPExecutableActivityNormal activity,
			HAPDefinitionActivityNormal activityDefinition,
			String resultName, 
			HAPContextGroup parentContext,
			HAPBuilderResultContext resultContextBuilder, 
			HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionResultActivityNormal resultDef = activityDefinition.getResult(resultName);
		HAPExecutableResultActivityNormal resultExe = new HAPExecutableResultActivityNormal(resultDef);
		if(resultContextBuilder!=null) {
			//data association input context
			HAPContextStructure dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(dataAssociationInputContext), resultDef.getOutputDataAssociation(), HAPParentContext.createDefault(parentContext), null, runtimeEnv);
			resultExe.setDataAssociation(outputDataAssociation);
		}
		return resultExe;
	}

	//process result
	public static void processBranchActivityBranch(HAPExecutableActivityBranch activity, HAPDefinitionActivityBranch activityDefinition) {
		for(HAPDefinitionResultActivityBranch branch :activityDefinition.getBranch()) {
			activity.addBranch(new HAPExecutableResultActivityBranch(branch));
		}
	}
	
	//build task wrapper for activity has task in it
	//all the input and result output for activity is mirror 
	public static HAPDefinitionWrapperTask parseTaskDefinition(HAPDefinitionActivityNormal activity, JSONObject jsonObj) {
		HAPDefinitionWrapperTask out = new HAPDefinitionWrapperTask();
		out.setInputMapping(activity.getInputMapping());
		activity.setInputMapping(new HAPDefinitionDataAssociationMirror());
		
		Map<String, HAPDefinitionResultActivityNormal> results = activity.getResults();
		for(String resultName : results.keySet()) {
			HAPDefinitionResultActivityNormal result = results.get(resultName);
			HAPDefinitionDataAssociation dataAssociation = result.getOutputDataAssociation();
			if(dataAssociation!=null)		out.addOutputMapping(resultName, dataAssociation.cloneDataAssocation());
			else out.addOutputMapping(resultName, new HAPDefinitionDataAssociationNone());
			result.setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
		}
		return out;
	}	
	
	public static void parseWithProcessTask(HAPEmbededProcessTask task, JSONObject jsonObj) {
		HAPDefinitionWrapperTask<String> process = new HAPDefinitionWrapperTask<String>();
		process.setTaskDefinition(jsonObj.optString(HAPEmbededProcessTask.PROCESS));
		process.buildMapping(jsonObj);
		task.setTask(process);
	}
	
}
