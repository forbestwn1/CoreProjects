package com.nosliw.core.application.division.manual.brick.test.complex.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickInBundle;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.core.application.common.task.HAPInfoTrigguerTask;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.brick.wrappertask.HAPManualBlockTaskWrapper;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.valueport.HAPUtilityValuePort;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.HAPUtilityBrickPath;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.xxx.application1.brick.test.complex.script.HAPTestTaskTrigguer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockComplexTestComplexScript extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockComplexTestComplexScript(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, HAPManualBlockTestComplexScript.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void normalizeBrickPath(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexScript definitionBlock = (HAPManualDefinitionBlockTestComplexScript)blockPair.getLeft();
		HAPManualBlockTestComplexScript executableBlock = (HAPManualBlockTestComplexScript)blockPair.getRight();

		for(HAPTestTaskTrigguer taskTrigguer : definitionBlock.getTaskTrigguers()) {
			HAPInfoTrigguerTask trigguerInfo = taskTrigguer.getTaskTrigguerInfo();
			HAPIdBrickInBundle handlerIdInBundle = trigguerInfo.getHandlerId();
			handlerIdInBundle.setIdPath(HAPUtilityBrickPath.normalizeBrickPath(new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), false, processContext.getCurrentBundle()).toString());
		}
		
	}

	@Override
	public void postProcessOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexScript definitionBlock = (HAPManualDefinitionBlockTestComplexScript)blockPair.getLeft();
		HAPManualBlockTestComplexScript executableBlock = (HAPManualBlockTestComplexScript)blockPair.getRight();

		for(HAPTestTaskTrigguer taskTrigguer : definitionBlock.getTaskTrigguers()) {
			HAPInfoTrigguerTask trigguerInfo = taskTrigguer.getTaskTrigguerInfo();
			HAPIdBrickInBundle handlerIdInBundle = trigguerInfo.getHandlerId();
			HAPUtilityBundle.processBrickIdInBundle(handlerIdInBundle, executableBlock.getTreeNodeInfo().getPathFromRoot().toString());
			
			String trigguerType = trigguerInfo.getTrigguerType();
			if(trigguerType.equals(HAPConstantShared.TASK_TRIGGUER_EVENTHANDLE)) {
				HAPManualBlockTaskWrapper taskWrapperBrick = (HAPManualBlockTaskWrapper)HAPUtilityBrick.getDescdentBrickLocal(bundle, handlerIdInBundle, processContext.getRootBrickName());
				HAPManualUtilityTask.buildValuePortGroupForInteractiveTaskEventHandler(taskWrapperBrick, trigguerInfo.getEventDataDefinition(), bundle.getValueStructureDomain());
			}
			else if(trigguerType.equals(HAPConstantShared.TASK_TRIGGUER_DATAVALIDATION)) {
				HAPManualUtilityTask.buildValuePortGroupForInteractiveTaskDataValidation(bundle, new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), trigguerInfo.getEventDataDefinition(), bundle.getValueStructureDomain(), processContext.getRuntimeEnv());
			}
			trigguerInfo.setExternalValuePortGroupName(HAPManualUtilityTask.getExternalValuePortGroupNameOfInteractiveTask(bundle, new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo()));
			
			executableBlock.getTaskTrigguers().add(taskTrigguer);
		}
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
//		HAPBundle bundle = processContext.getCurrentBundle();
//		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
//		HAPManualDefinitionBlockTestComplexScript definitionBlock = (HAPManualDefinitionBlockTestComplexScript)blockPair.getLeft();
//		HAPManualBlockTestComplexScript executableBlock = (HAPManualBlockTestComplexScript)blockPair.getRight();
//
//		for(HAPTestTaskTrigguer taskTrigguer : definitionBlock.getTaskTrigguers()) {
//			HAPInfoTrigguerTask trigguerInfo = taskTrigguer.getTaskTrigguerInfo();
//			HAPIdBrickInBundle handlerIdInBundle = trigguerInfo.getHandlerId();
//			HAPUtilityBundle.processBrickIdInBundle(handlerIdInBundle, executableBlock.getTreeNodeInfo().getPathFromRoot().toString());
//			
//			String trigguerType = trigguerInfo.getTrigguerType();
//			if(trigguerType.equals(HAPConstantShared.TASK_TRIGGUER_EVENTHANDLE)) {
//				HAPManualBlockTaskWrapper taskWrapperBrick = (HAPManualBlockTaskWrapper)HAPUtilityBrick.getDescdentBrickLocal(bundle, handlerIdInBundle, processContext.getRootBrickName());
//				HAPManualUtilityTask.buildValuePortGroupForInteractiveTaskEventHandler(taskWrapperBrick, trigguerInfo.getEventDataDefinition(), bundle.getValueStructureDomain());
//			}
//			else if(trigguerType.equals(HAPConstantShared.TASK_TRIGGUER_DATAVALIDATION)) {
//				HAPManualUtilityTask.buildValuePortGroupForInteractiveTaskDataValidation(bundle, new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), trigguerInfo.getEventDataDefinition(), bundle.getValueStructureDomain(), processContext.getRuntimeEnv());
//			}
//			trigguerInfo.setExternalValuePortGroupName(HAPManualUtilityTask.getExternalValuePortGroupNameOfInteractiveTask(bundle, new HAPPath(handlerIdInBundle.getIdPath()), processContext.getRootBrickName(), processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo()));
//			
//			executableBlock.getTaskTrigguers().add(taskTrigguer);
//		}
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexScript definitionBlock = (HAPManualDefinitionBlockTestComplexScript)blockPair.getLeft();
		HAPManualBlockTestComplexScript executableBlock = (HAPManualBlockTestComplexScript)blockPair.getRight();
		
		Map<String, Object> parms = definitionBlock.getParms();

		executableBlock.setScript(definitionBlock.getScript());
		
		executableBlock.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPBlockTestComplexScript.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPResultReferenceResolve> resolvedVars = new ArrayList<HAPResultReferenceResolve>();
			List<HAPReferenceElement> unknownVars = new ArrayList<HAPReferenceElement>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElement ref = new HAPReferenceElement();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				ref.setValuePortId(HAPUtilityValuePort.normalizeInternalValuePortId(ref.getValuePortId(), HAPConstantShared.IO_DIRECTION_BOTH, executableBlock));
				HAPResultReferenceResolve resolve  = HAPUtilityStructureElementReference.analyzeElementReferenceInternal(ref, executableBlock, new HAPConfigureResolveElementReference(), processContext.getCurrentBundle().getValueStructureDomain());
				
				if(resolve!=null) {
					resolvedVars.add(resolve);
				} else {
					unknownVars.add(ref);
				}
			}
			executableBlock.setVariables(resolvedVars);
			executableBlock.setUnknowVariable(unknownVars);
		}
	}

	
/*	
	
	
	@Override
	public void processValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		
		HAPBlockTestComplexTask executableEntity = (HAPBlockTestComplexTask)complexEntityExecutable;
		
		HAPIdEntityInDomain complexEntityDefinitionId = executableEntity.getDefinitionEntityId();
		HAPManualDefinitionBlockTestComplexTask definitionEntity = (HAPManualDefinitionBlockTestComplexTask)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();
		Map<String, Object> parms = definitionEntity.getParms();
		
		//extended variable
		Object variablesExtension = parms.get(HAPBlockTestComplexTask.VARIABLEEXTENDED);
		if(variablesExtension!=null) {
			JSONArray varJsonArray = (JSONArray)variablesExtension;
			List<HAPExecutableVariableExpected> resolvedVars = new ArrayList<HAPExecutableVariableExpected>();
			for(int i=0; i<varJsonArray.length(); i++) {
				
				JSONObject varJson = varJsonArray.getJSONObject(i);
				HAPDefinitionVariableExpected varDef = new HAPDefinitionVariableExpected();
				varDef.buildObject(varJson, HAPSerializationFormat.JSON);

				HAPExecutableVariableExpected varExe = new HAPExecutableVariableExpected(varDef);
				resolvedVars.add(varExe);
			}
			executableEntity.setExtendedVariables(resolvedVars);
		}
	}
	
	@Override
	public void processEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext) {
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainEntityDefinitionGlobal definitionDomain = currentBundle.getDefinitionDomain();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPBlockTestComplexTask executableEntity = (HAPBlockTestComplexTask)complexEntityExecutable;
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		HAPIdEntityInDomain complexEntityDefinitionId = complexEntityExecutable.getDefinitionEntityId();
		HAPManualDefinitionBlockTestComplexTask definitionEntity = (HAPManualDefinitionBlockTestComplexTask)definitionDomain.getEntityInfoDefinition(complexEntityDefinitionId).getEntity();

		
		HAPEntityBundle bundle = processContext.getCurrentBundle();
		HAPUtilityEntityDefinition.getEntityDefinitionFromExeTreeNodeE(null, bundle)
//		HAPUtilityBrick.getEntityDefinitionFromExeTreeNodeE(bundle., bundle)
		
		
		Map<String, Object> parms = definitionEntity.getParms();

		executableEntity.setScriptName(definitionEntity.getScriptName());
		
		executableEntity.setParms(parms);
	
		//normal variable
		Object variables = parms.get(HAPBlockTestComplexTask.VARIABLE);
		if(variables!=null) {
			JSONArray varJsonArray = (JSONArray)variables;
			List<HAPResultReferenceResolve> resolvedVars = new ArrayList<HAPResultReferenceResolve>();
			List<HAPReferenceElement> unknownVars = new ArrayList<HAPReferenceElement>();
			for(int i=0; i<varJsonArray.length(); i++) {
				HAPReferenceElement ref = new HAPReferenceElement();
				ref.buildObject(varJsonArray.get(i), HAPSerializationFormat.JSON);
				
				HAPResultReferenceResolve resolve = HAPUtilityProcessRelativeElement.resolveElementReference(ref, new HAPConfigureResolveElementReference(), processContext);
				if(resolve!=null) {
					resolvedVars.add(resolve);
				} else {
					unknownVars.add(ref);
				}
			}
			executableEntity.setVariables(resolvedVars);
			executableEntity.setUnknowVariable(unknownVars);
		}
	
		HAPDomainAttachment attachmentDomain = processContext.getCurrentBundle().getAttachmentDomain();
		Object attachmentsObj = parms.get(HAPBlockTestComplexTask.ATTACHMENT);
		if(attachmentsObj!=null) {
			List<HAPInfoAttachmentResolve> attachments = new ArrayList<HAPInfoAttachmentResolve>(); 
			JSONArray attachmentsArray = (JSONArray)attachmentsObj;
			for(int i=0; i<attachmentsArray.length(); i++) {
				String attIdStr = (String)attachmentsArray.get(i);
				String[] segs = HAPUtilityNamingConversion.parseLevel2(attIdStr);
				String valueType = segs[0];
				String itemName = segs[1];
				HAPAttachmentImpEntity attachment = (HAPAttachmentImpEntity)attachmentDomain.getAttachment(executableEntity.getAttachmentContainerId(), valueType, itemName);
				String entityStr = attachment.getEntity().toExpandedJsonString(definitionDomain);
				attachments.add(new HAPInfoAttachmentResolve(valueType, itemName, attachment, entityStr));
			}
			executableEntity.setAttachment(attachments);
		}

//		System.out.println(new HAPIdElement(resolve.structureId, variable).toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		super.processValueContextExtension(complexEntityExecutable, processContext);
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		
		HAPBlockTestComplexTask executableEntity = (HAPBlockTestComplexTask)complexEntityExecutable;
		HAPExecutableEntityValueContext valueStructureComplex = executableEntity.getValueContext();
		
		List<HAPExecutableVariableExpected> expectedVars = executableEntity.getExtendedVariables();
		if(expectedVars!=null) {
			for(HAPExecutableVariableExpected extendedVar : expectedVars) {
				HAPDefinitionVariableExpected varDef = extendedVar.getDefinition();
				HAPIdElement idVariable = HAPUtilityValueContextReference.resolveVariableName(varDef.getVariableName(), valueStructureComplex, varDef.getGroup(), valueStructureDomain, null);
				extendedVar.setVariableId(idVariable);
			}
		}
	}
	
	@Override
	public void processValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext) {
		super.processValueContextDiscovery(complexEntityExecutable, processContext);
		
		HAPExecutableBundle currentBundle = processContext.getCurrentBundle();
		HAPDomainValueStructure valueStructureDomain = currentBundle.getValueStructureDomain();
		HAPBlockTestComplexTask executableEntity = (HAPBlockTestComplexTask)complexEntityExecutable;
		
		List<HAPExecutableVariableExpected> expectedVars = executableEntity.getExtendedVariables();
		if(expectedVars!=null) {
			for(HAPExecutableVariableExpected extendedVar : expectedVars) {
				HAPDefinitionVariableExpected varDef = extendedVar.getDefinition();
				HAPElementStructureLeafData dataStructureEle = (HAPElementStructureLeafData)HAPManualUtilityValueContext.getStructureElement(extendedVar.getVariableId(), valueStructureDomain);
				
				HAPMatchers matchers = HAPUtilityCriteria.mergeVariableInfo(dataStructureEle.getCriteriaInfo(), new HAPDataTypeCriteriaId(varDef.getDataTypeId(), null), processContext.getRuntimeEnvironment().getDataTypeHelper());
				extendedVar.setMarchers(matchers);
			}
		}
	}
	
*/	
}
