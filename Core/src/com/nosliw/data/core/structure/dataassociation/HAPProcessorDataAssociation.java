package com.nosliw.data.core.structure.dataassociation;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.structure.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.structure.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.structure.dataassociation.mirror.HAPProcessorDataAssociationMirror;
import com.nosliw.data.core.structure.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.structure.dataassociation.none.HAPProcessorDataAssociationNone;
import com.nosliw.data.core.structure.story.HAPParentContext;

public class HAPProcessorDataAssociation {

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext externalContext, HAPInfo configure, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		return processDataAssociationWithTask(taskWrapperDef, taskExe, externalContext, configure, externalContext, configure, attachmentContainer, runtimeEnv);
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext inputContext, HAPInfo inputConfigure, HAPParentContext outputContext, HAPInfo outputConfigure, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), attachmentContainer, inputConfigure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		for(String resultName : resultOutputMapping.keySet()) {
			HAPParentContext inContext = taskExe.getOutResultContext().get(resultName);
			out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(inContext, resultOutputMapping.get(resultName), outputContext, attachmentContainer, outputConfigure, runtimeEnv));
		}
		String defaultResultName = HAPConstantShared.NAME_DEFAULT;
		if(out.getOutputMapping(defaultResultName)==null) {
			//if no default output mapping defined, then create default output with mirror data association
			HAPParentContext inContext = taskExe.getOutResultContext().get(defaultResultName);
			if(inContext!=null) {
				out.addOutputMapping(defaultResultName, HAPProcessorDataAssociation.processDataAssociation(inContext, new HAPDefinitionDataAssociationMirror(), outputContext, attachmentContainer, outputConfigure, runtimeEnv));
			}
		}
		return out;
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext inputContext, Map<String, HAPParentContext> outputContexts, HAPContainerAttachment attachmentContainer, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), attachmentContainer, configure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		Map<String, HAPParentContext> taskResults = taskExe.getOutResultContext();
		for(String resultName : taskResults.keySet()) {
			HAPDefinitionDataAssociation outputMapping = resultOutputMapping.get(resultName);
			if(outputMapping==null)  outputMapping = new HAPDefinitionDataAssociationMirror();
			HAPParentContext outputContext = outputContexts.get(resultName);
//			if(outputContext==null)  outputContext = inputContext;
			if(outputContext!=null) out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(taskResults.get(resultName), outputMapping, outputContext, attachmentContainer, configure, runtimeEnv));
		}
		
		return out;
	}

	public static void enhanceDataAssociationWithTaskEndPointContext(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionDataMappingTask taskWrapperDef, HAPParentContext externalContext, boolean externalContextEnhance, HAPRuntimeEnvironment runtimeEnv) {
		enhanceDataAssociationWithTaskEndPointContext(taskIO, taskIOEnhance, taskWrapperDef, externalContext, externalContextEnhance, externalContext, externalContextEnhance, runtimeEnv);
	}

	public static void enhanceDataAssociationWithTaskEndPointContext(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionDataMappingTask taskWrapperDef, HAPParentContext inputContext, boolean inputContextEnhance, HAPParentContext outputContext, boolean outputContextEnhance, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		enhanceDataAssociationEndPointContext(inputContext, inputContextEnhance, inputMapping, taskIO.getInContext(), taskIOEnhance, runtimeEnv);

		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		for(String resultName : resultOutputMapping.keySet()) {
			enhanceDataAssociationEndPointContext(taskIO.getOutResultContext().get(resultName), taskIOEnhance, resultOutputMapping.get(resultName), outputContext, outputContextEnhance, runtimeEnv);
		}
	}

	private static HAPDefinitionDataAssociation getInputMappingFromTaskDataMapping(HAPDefinitionDataMappingTask taskWrapperDef) {
		HAPDefinitionDataAssociation out = taskWrapperDef.getInputMapping();
		if(out==null)  out = new HAPDefinitionDataAssociationMirror();   //if no input mapping defined, then use mirror
		return out;
	}
	

	public static void enhanceDataAssociationEndPointContext(HAPParentContext input, boolean inputEnhance, HAPDefinitionDataAssociation dataAssociation, HAPParentContext output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		String type = dataAssociation.getType();
		
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			HAPProcessorDataAssociationMapping.enhanceDataAssociationEndPointContext(input, inputEnhance, (HAPDefinitionDataAssociationMapping)dataAssociation, output, outputEnhance, runtimeEnv);
			break;
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
			break;

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
			break;
		}
	}
	
	public static HAPExecutableDataAssociation processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, HAPParentContext output, HAPContainerAttachment attachmentContainer, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		if(dataAssociation==null)  dataAssociation = new HAPDefinitionDataAssociationNone();
		String type = dataAssociation.getType();
		
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, attachmentContainer, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
			return HAPProcessorDataAssociationMirror.processDataAssociation(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		
		return null;
	}
}
