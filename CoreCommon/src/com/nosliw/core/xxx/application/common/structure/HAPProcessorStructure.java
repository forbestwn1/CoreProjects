package com.nosliw.core.xxx.application.common.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.xxx.application.common.structure.reference.HAPProcessorElementRelative;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;

public class HAPProcessorStructure {

	public static HAPStructure1 process(HAPStructure1 valueStructure, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructure1 out = null;

		if(valueStructure!=null) {
			out = process(valueStructure, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
		}
		
		return out;
	}
	
	public static HAPStructure1 processRelative(HAPStructure1 contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
//	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
//		if(configure==null)  configure = new HAPConfigureProcessorStructure();
//		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
//		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
//		HAPValueStructureDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
//		return processed.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
//	}
	
	public static HAPStructure1 process(HAPStructure1 contextGroup, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		HAPStructure1 out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPStructure1 processStatic(HAPStructure1 structure, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		
		//figure out all constant values in context
		structure = HAPProcessorElementConstant.process(structure, parent, attachmentContainer, configure, runtimeEnv);
		
		//solidate name in context  
		structure = HAPProcessorElementSolidateConstantScript.process(structure, runtimeEnv);
		
		//process data rule
		HAPProcessorElementRule.process(structure, runtimeEnv);
		
		//process inheritance
		structure = HAPProcessorElementVariableInheritance.process((HAPValueStructureInValuePort11111)structure, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return structure;
	}
	
	public static HAPStructure1 processRelative(HAPStructure1 structure, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		
		//resolve relative context
		structure = HAPProcessorElementRelative.process(structure, parent, dependency, errors, configure, runtimeEnv);
		
		return structure;
	}
}
