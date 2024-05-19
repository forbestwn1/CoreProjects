package com.nosliw.data.core.dataassociation;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.dataassociation.mirror.HAPProcessorDataAssociationMirror;
import com.nosliw.data.core.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.dataassociation.none.HAPProcessorDataAssociationNone;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorDataAssociation {

	public static HAPExecutableDataAssociation processDataAssociation(
			HAPDefinitionDataAssociation dataAssociation,
			HAPExecutableEntity inEntityExe,
			HAPContextProcessor inProcessorContext, 
			HAPExecutableEntity outEntityExe,
			HAPContextProcessor outProcessorContext, 
			HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation out = null;
		String type = dataAssociation.getType();
		String direction = dataAssociation.getDirection();
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			HAPDefinitionDataAssociationMapping valueMappingDA = (HAPDefinitionDataAssociationMapping)dataAssociation;
			HAPDataAssociationMapping daMappingExe = new  HAPDataAssociationMapping(valueMappingDA);
			if(direction.equals(HAPConstantShared.DATAASSOCIATION_DIRECTION_DOWNSTREAM)) {
				
				HAPProcessorDataAssociationMapping.processValueMapping(
						daMappingExe,
						inEntityExe,
						inProcessorContext,
						valueMappingDA,
						outEntityExe,
						outProcessorContext,
						runtimeEnv);
			}
			else {
				HAPProcessorDataAssociationMapping.processValueMapping(
						daMappingExe,
						outEntityExe,
						outProcessorContext,
						valueMappingDA,
						inEntityExe,
						inProcessorContext,
						runtimeEnv);
			}
			out = daMappingExe;
//			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
//			HAPDefinitionDataAssociationMapping mappingDataAssociation = HAPProcessorDataAssociationMirror.convertToDataAssociationMapping(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output);
//			return processDataAssociation(input, mappingDataAssociation, output, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
//			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		dataAssociation.cloneToEntityInfo(out);
		return out;
	}
	
	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionGroupDataAssociationForTask taskWrapperDef, HAPExecutableTask taskExe, HAPContainerStructure externalContext, HAPInfo configure, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		return processDataAssociationWithTask(taskWrapperDef, taskExe, externalContext, configure, externalContext, configure, attachmentContainer, runtimeEnv);
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionGroupDataAssociationForTask taskWrapperDef, HAPExecutableTask taskExe, HAPContainerStructure inputStructure, HAPInfo inputConfigure, HAPContainerStructure outputStructure, HAPInfo outputConfigure, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		out.setInDataAssociation(HAPProcessorDataAssociation.processDataAssociation(inputStructure, inputMapping, taskExe.getInStructure(), inputConfigure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutDataAssociations();
		for(String resultName : resultOutputMapping.keySet()) {
			HAPContainerStructure inContext = taskExe.getOutResultStructure().get(resultName);
			out.addOutDataAssociation(resultName, HAPProcessorDataAssociation.processDataAssociation(inContext, resultOutputMapping.get(resultName), outputStructure, outputConfigure, runtimeEnv));
		}
		String defaultResultName = HAPConstantShared.NAME_DEFAULT;
		if(out.getOutDataAssociation(defaultResultName)==null) {
			//if no default output mapping defined, then create default output with mirror data association
			HAPContainerStructure inContext = taskExe.getOutResultStructure().get(defaultResultName);
			if(inContext!=null) {
				out.addOutDataAssociation(defaultResultName, HAPProcessorDataAssociation.processDataAssociation(inContext, new HAPDefinitionDataAssociationMirror(), outputStructure, outputConfigure, runtimeEnv));
			}
		}
		return out;
	}

	public static void enhanceDataAssociationWithTaskEndPointValueStructure(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionGroupDataAssociationForTask taskWrapperDef, HAPContainerStructure externalValueStructure, boolean externalValueStructureEnhance, HAPRuntimeEnvironment runtimeEnv) {
		enhanceDataAssociationWithTaskEndPointValueStructure(taskIO, taskIOEnhance, taskWrapperDef, externalValueStructure, externalValueStructureEnhance, externalValueStructure, externalValueStructureEnhance, runtimeEnv);
	}

	public static void enhanceDataAssociationWithTaskEndPointValueStructure(HAPIOTask taskIO, boolean taskIOEnhance, HAPDefinitionGroupDataAssociationForTask taskWrapperDef, HAPContainerStructure inputContext, boolean inputContextEnhance, HAPContainerStructure outputContext, boolean outputContextEnhance, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionDataAssociation inputMapping = getInputMappingFromTaskDataMapping(taskWrapperDef);
		enhanceDataAssociationEndPointValueStructure(inputContext, inputContextEnhance, inputMapping, taskIO.getInStructure(), taskIOEnhance, runtimeEnv);

		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutDataAssociations();
		for(String resultName : resultOutputMapping.keySet()) {
			enhanceDataAssociationEndPointValueStructure(taskIO.getOutResultStructure().get(resultName), taskIOEnhance, resultOutputMapping.get(resultName), outputContext, outputContextEnhance, runtimeEnv);
		}
	}

	private static HAPDefinitionDataAssociation getInputMappingFromTaskDataMapping(HAPDefinitionGroupDataAssociationForTask taskWrapperDef) {
		HAPDefinitionDataAssociation out = taskWrapperDef.getInDataAssociation();
		if(out==null)  out = new HAPDefinitionDataAssociationMirror();   //if no input mapping defined, then use mirror
		return out;
	}
	

	public static void enhanceDataAssociationEndPointValueStructure(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionDataAssociation dataAssociation, HAPContainerStructure output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
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
	
	public static HAPExecutableDataAssociation processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociation dataAssociation, HAPContainerStructure output, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociation out = null;
		if(dataAssociation==null)  dataAssociation = new HAPDefinitionDataAssociationNone();
		String type = dataAssociation.getType();
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
			HAPDefinitionDataAssociationMapping mappingDataAssociation = HAPProcessorDataAssociationMirror.convertToDataAssociationMapping(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output);
			return processDataAssociation(input, mappingDataAssociation, output, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		dataAssociation.cloneToEntityInfo(out);
		return out;
	}
}
