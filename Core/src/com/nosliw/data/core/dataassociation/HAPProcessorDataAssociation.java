package com.nosliw.data.core.dataassociation;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.mirror.HAPProcessorDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.dataassociation.none.HAPProcessorDataAssociationNone;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPProcessorDataAssociation {

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPContainerStructure externalContext, HAPInfo configure, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		return processDataAssociationWithTask(taskWrapperDef, taskExe, externalContext, configure, externalContext, configure, attachmentContainer, runtimeEnv);
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPContainerStructure inputContext, HAPInfo inputConfigure, HAPContainerStructure outputContext, HAPInfo outputConfigure, HAPContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), attachmentContainer, inputConfigure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		for(String resultName : resultOutputMapping.keySet()) {
			HAPContainerStructure inContext = taskExe.getOutResultContext().get(resultName);
			out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(inContext, resultOutputMapping.get(resultName), outputContext, attachmentContainer, outputConfigure, runtimeEnv));
		}
		String defaultResultName = HAPConstantShared.NAME_DEFAULT;
		if(out.getOutputMapping(defaultResultName)==null) {
			//if no default output mapping defined, then create default output with mirror data association
			HAPContainerStructure inContext = taskExe.getOutResultContext().get(defaultResultName);
			if(inContext!=null) {
				out.addOutputMapping(defaultResultName, HAPProcessorDataAssociation.processDataAssociation(inContext, new HAPDefinitionDataAssociationMirror(), outputContext, attachmentContainer, outputConfigure, runtimeEnv));
			}
		}
		return out;
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionDataMappingTask taskWrapperDef, HAPExecutableTask taskExe, HAPContainerStructure inputContext, Map<String, HAPContainerStructure> outputContexts, HAPContainerAttachment attachmentContainer, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), attachmentContainer, configure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		Map<String, HAPContainerStructure> taskResults = taskExe.getOutResultContext();
		for(String resultName : taskResults.keySet()) {
			HAPDefinitionDataAssociation outputMapping = resultOutputMapping.get(resultName);
			if(outputMapping==null)  outputMapping = new HAPDefinitionDataAssociationMirror();
			HAPContainerStructure outputContext = outputContexts.get(resultName);
//			if(outputContext==null)  outputContext = inputContext;
			if(outputContext!=null) out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(taskResults.get(resultName), outputMapping, outputContext, attachmentContainer, configure, runtimeEnv));
		}
		
		return out;
	}

	public static void enhanceDataAssociationWithTaskEndPointContext(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionDataMappingTask taskWrapperDef, HAPContainerStructure externalContext, boolean externalContextEnhance, HAPRuntimeEnvironment runtimeEnv) {
		enhanceDataAssociationWithTaskEndPointContext(taskIO, taskIOEnhance, taskWrapperDef, externalContext, externalContextEnhance, externalContext, externalContextEnhance, runtimeEnv);
	}

	public static void enhanceDataAssociationWithTaskEndPointContext(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionDataMappingTask taskWrapperDef, HAPContainerStructure inputContext, boolean inputContextEnhance, HAPContainerStructure outputContext, boolean outputContextEnhance, HAPRuntimeEnvironment runtimeEnv) {
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
	

	public static void enhanceDataAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionDataAssociation dataAssociation, HAPContainerStructure output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
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
	
	public static HAPExecutableDataAssociation processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociation dataAssociation, HAPContainerStructure output, HAPContainerAttachment attachmentContainer, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		if(dataAssociation==null)  dataAssociation = new HAPDefinitionDataAssociationNone();
		String type = dataAssociation.getType();
		
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, attachmentContainer, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
			HAPDefinitionDataAssociationMapping mappingDataAssociation = HAPProcessorDataAssociationMirror.convertToDataAssociationMapping(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output);
			return processDataAssociation(input, mappingDataAssociation, output, attachmentContainer, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		
		return null;
	}
}
