package com.nosliw.data.core.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;

public class HAPProcessorContext {

	public static HAPValueStructureDefinition process(HAPValueStructureDefinition context, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructureDefinition out = null;
		if(context!=null) {
			String type = context.getType();
			if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				out = process((HAPValueStructureDefinitionFlat)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
			else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				out = process((HAPValueStructureDefinitionGroup)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionGroup processRelative(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPValueStructureDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
		return processed.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPValueStructureDefinitionGroup out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPValueStructureDefinitionGroup processStatic(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parent, attachmentContainer, configure.inheritMode, runtimeEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, runtimeEnv);
		
		//process data rule
		HAPProcessorContextRule.process(contextGroup, runtimeEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextVariableInheritance.process(contextGroup, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return contextGroup;
	}
	
	public static HAPValueStructureDefinitionGroup processRelative(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		
		return contextGroup;
		
	}
}
