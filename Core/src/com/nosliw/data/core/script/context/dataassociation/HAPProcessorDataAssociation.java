package com.nosliw.data.core.script.context.dataassociation;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPProcessorDataAssociationMirror;
import com.nosliw.data.core.script.context.dataassociation.none.HAPDefinitionDataAssociationNone;
import com.nosliw.data.core.script.context.dataassociation.none.HAPProcessorDataAssociationNone;

public class HAPProcessorDataAssociation {

	private static String INFO_MODIFYSTRUCTURE = "modifyStructure";
	
	public static HAPInfo withModifyStructureTrue(HAPInfo info) {
		info.setValue(INFO_MODIFYSTRUCTURE, "true");
		return info;
	}

	public static HAPInfo withModifyStructureFalse(HAPInfo info) {
		info.setValue(INFO_MODIFYSTRUCTURE, "false");
		return info;
	}
	
	public static boolean getModifyStructure(HAPInfo info) {
		boolean defaultValue = true;
		if(info==null)  return defaultValue;
		String outStr = (String)info.getValue(INFO_MODIFYSTRUCTURE, defaultValue+"");
		return Boolean.valueOf(outStr);
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionWrapperTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext externalContext, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		return processDataAssociationWithTask(taskWrapperDef, taskExe, externalContext, externalContext, configure, runtimeEnv);
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionWrapperTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext inputContext, HAPParentContext outputContext, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = taskWrapperDef.getInputMapping();
		if(inputMapping==null)  inputMapping = new HAPDefinitionDataAssociationMirror();   //if no input mapping defined, then use mirror
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), configure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		for(String resultName : resultOutputMapping.keySet()) {
			HAPParentContext inContext = taskExe.getOutResultContext().get(resultName);
			out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(inContext, resultOutputMapping.get(resultName), outputContext, configure, runtimeEnv));
		}
		String defaultResultName = HAPConstantShared.NAME_DEFAULT;
		if(out.getOutputMapping(defaultResultName)==null) {
			//if no default output mapping defined, then create default output with mirror data association
			HAPParentContext inContext = taskExe.getOutResultContext().get(defaultResultName);
			if(inContext!=null) {
				out.addOutputMapping(defaultResultName, HAPProcessorDataAssociation.processDataAssociation(inContext, new HAPDefinitionDataAssociationMirror(), outputContext, configure, runtimeEnv));
			}
		}
		return out;
	}

	public static HAPExecutableWrapperTask processDataAssociationWithTask(HAPDefinitionWrapperTask taskWrapperDef, HAPExecutableTask taskExe, HAPParentContext inputContext, Map<String, HAPParentContext> outputContexts, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableWrapperTask out = new HAPExecutableWrapperTask();
		out.setTask(taskExe);
		//process input mapping
		HAPDefinitionDataAssociation inputMapping = taskWrapperDef.getInputMapping();
		if(inputMapping==null)  inputMapping = new HAPDefinitionDataAssociationMirror();   //if no input mapping defined, then use mirror
		out.setInputMapping(HAPProcessorDataAssociation.processDataAssociation(inputContext, inputMapping, taskExe.getInContext(), configure, runtimeEnv));
		
		Map<String, HAPDefinitionDataAssociation> resultOutputMapping = taskWrapperDef.getOutputMapping();
		Map<String, HAPParentContext> taskResults = taskExe.getOutResultContext();
		for(String resultName : taskResults.keySet()) {
			HAPDefinitionDataAssociation outputMapping = resultOutputMapping.get(resultName);
			if(outputMapping==null)  outputMapping = new HAPDefinitionDataAssociationMirror();
			HAPParentContext outputContext = outputContexts.get(resultName);
//			if(outputContext==null)  outputContext = inputContext;
			if(outputContext!=null) out.addOutputMapping(resultName, HAPProcessorDataAssociation.processDataAssociation(taskResults.get(resultName), outputMapping, outputContext, configure, runtimeEnv));
		}
		
		return out;
	}

	
	public static HAPExecutableDataAssociation processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, HAPParentContext output, HAPInfo configure, HAPRuntimeEnvironment runtimeEnv) {
		if(dataAssociation==null)  dataAssociation = new HAPDefinitionDataAssociationNone();
		String type = dataAssociation.getType();
		
		switch(type) {
		case HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING:
			return HAPProcessorDataAssociationMapping.processDataAssociation(input, (HAPDefinitionDataAssociationMapping)dataAssociation, output, configure, runtimeEnv);
		
		case HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR:
			return HAPProcessorDataAssociationMirror.processDataAssociation(input, (HAPDefinitionDataAssociationMirror)dataAssociation, output, configure, runtimeEnv);

		case HAPConstantShared.DATAASSOCIATION_TYPE_NONE:
			return HAPProcessorDataAssociationNone.processDataAssociation(input, (HAPDefinitionDataAssociationNone)dataAssociation, output, configure, runtimeEnv);
		}
		
		return null;
	}
}
