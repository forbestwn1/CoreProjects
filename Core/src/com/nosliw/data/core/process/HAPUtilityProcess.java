package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.process.resource.HAPIdProcess;
import com.nosliw.data.core.process.resource.HAPResourceIdProcess;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression.HAPContextProcessExpressionScript;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPContainerStructure;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPUtilityProcess {

	public static String buildOutputVarialbeName(String name) {
		return "nosliw_" + name;
	}
	
	public static HAPResourceId buildResourceId(String suiteId, String processId) {
		return new HAPResourceIdProcess(new HAPIdProcess(suiteId, processId));
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

	public static void buildScriptExpressionProcessContext(HAPValueStructureDefinitionFlat context, HAPContextProcessExpressionScript expProcessContext) {
		//prepare constant value 
		Map<String, Object> constantsValue = context.getConstantValue();
		for(String id : constantsValue.keySet()) {
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(id, constantsValue.get(id));
			expProcessContext.addConstantDefinition(constantDef);
		}
		expProcessContext.setValueContext(context);
		
//		expProcessContext.addConstants(context.getConstantValue());
//		//prepare variables 
//		expProcessContext.addDataVariables(HAPUtilityContext.discoverDataVariablesInContext(context));
	}

	public static void processNormalActivityInputDataAssocation(HAPExecutableActivityNormal activity, HAPDefinitionActivityNormal activityDefinition, HAPValueStructureDefinitionGroup processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(processContext), 
				activityDefinition.getInputMapping(), 
				HAPContainerStructure.createDefault(activityDefinition.getInputContextStructure(processContext)), 
				null, 
				runtimeEnv);
		activity.setInputDataAssociation(da);
	}
	
	public static void processBranchActivityInputDataAssocation(HAPExecutableActivityBranch activity, HAPDefinitionActivityBranch activityDefinition, HAPValueStructureDefinitionGroup processContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation da = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(processContext), 
				activityDefinition.getInputMapping(), 
				HAPContainerStructure.createDefault(activityDefinition.getInputContextStructure(processContext)), 
				null, 
				runtimeEnv);
		activity.setInputDataAssociation(da);
	}
	
	//data variables infor in activity merge back to process context
	public static void mergeDataVariableInActivityToProcessContext(Set<HAPVariableInfo> activityVariablesInfo, HAPValueStructureDefinitionFlat activityContext, HAPValueStructureDefinitionGroup processContext) {
		Map<String, HAPVariableInfo> expectedVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(HAPVariableInfo expectedVarInfo : activityVariablesInfo) {
			HAPReferenceElement varPath = new HAPReferenceElement(expectedVarInfo.getName());
			//affect global variable 
			HAPRoot affectedRoot = activityContext.getRoot(varPath.getRootReference().getFullName());
			if(affectedRoot!=null) {
				//ele mapped from context variable
				HAPElement currentEle = affectedRoot.getDefinition();
				String[] pathSegs = new HAPPath(varPath.getSubPath()).getPathSegments();
				if(pathSegs.length>0) {
					int i = 0;
					while(!HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE.equals(currentEle.getType())&&currentEle!=null) {
						currentEle = currentEle.getChild(pathSegs[i]);
						i++;
					}
				}
				HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)currentEle;
				HAPReferenceElement relativeElePath = relativeEle.getPathFormat();
				String fullName = relativeElePath.getFullPath();
				for(int i=0;i<pathSegs.length; i++) {
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
			HAPReferenceElement cpath = new HAPReferenceElement(basePath);
			HAPElementLeafData affectedEle = new HAPElementLeafData(expectedVariablesInfo.get(basePath).getDataInfo());
			HAPUtilityContext.updateDataDescendant(processContext, cpath.getRootReference().getCategary(), cpath.getPath(), affectedEle);
		}
	}
	
	//process result
	public static HAPExecutableResultActivityNormal processNormalActivityResult(
			HAPExecutableActivityNormal activity,
			HAPDefinitionActivityNormal activityDefinition,
			String resultName, 
			HAPValueStructureDefinitionGroup parentContext,
			HAPBuilderResultContext resultContextBuilder, 
			HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionResultActivityNormal resultDef = activityDefinition.getResult(resultName);
		HAPExecutableResultActivityNormal resultExe = new HAPExecutableResultActivityNormal(resultDef);
		if(resultContextBuilder!=null) {
			//data association input context
			HAPValueStructureDefinition dataAssociationInputContext = resultContextBuilder.buildResultContext(resultName, activity);
			//process data association
			HAPExecutableDataAssociation outputDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(dataAssociationInputContext), resultDef.getOutputDataAssociation(), HAPContainerStructure.createDefault(parentContext), null, runtimeEnv);
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
		process.buildObj(jsonObj, jsonObj.optString(HAPEmbededProcessTask.PROCESS));
		task.setTask(process);
	}
	
}
