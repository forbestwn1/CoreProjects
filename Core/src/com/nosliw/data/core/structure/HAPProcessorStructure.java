package com.nosliw.data.core.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.reference.HAPProcessorElementRelative;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPProcessorStructure {

	public static HAPStructure process(HAPStructure valueStructure, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructure out = null;

		if(valueStructure!=null) {
			out = process(valueStructure, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
		}
		
		return out;
	}
	
	public static HAPStructure processRelative(HAPStructure contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
//	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
//		if(configure==null)  configure = new HAPConfigureProcessorStructure();
//		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
//		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
//		HAPValueStructureDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
//		return processed.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
//	}
	
	public static HAPStructure process(HAPStructure contextGroup, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPStructure out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPStructure processStatic(HAPStructure structure, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//figure out all constant values in context
		structure = HAPProcessorElementConstant.process(structure, parent, attachmentContainer, configure, runtimeEnv);
		
		//solidate name in context  
		structure = HAPProcessorElementSolidateConstantScript.process(structure, runtimeEnv);
		
		//process data rule
		HAPProcessorElementRule.process(structure, runtimeEnv);
		
		//process inheritance
		structure = HAPProcessorElementVariableInheritance.process((HAPValueStructure)structure, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return structure;
	}
	
	public static HAPStructure processRelative(HAPStructure structure, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//resolve relative context
		structure = HAPProcessorElementRelative.process(structure, parent, dependency, errors, configure, runtimeEnv);
		
		return structure;
	}
}
