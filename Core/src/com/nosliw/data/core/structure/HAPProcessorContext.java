package com.nosliw.data.core.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public class HAPProcessorContext {

	public static HAPContextStructureValueDefinition process(HAPContextStructureValueDefinition context, HAPParentContext parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPContextStructureValueDefinition out = null;
		if(context!=null) {
			String type = context.getType();
			if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				out = process((HAPContextStructureValueDefinitionFlat)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
			else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				out = process((HAPContextStructureValueDefinitionGroup)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
		}
		return out;
	}
	
	public static HAPContextStructureValueDefinitionGroup processRelative(HAPContextStructureValueDefinitionGroup contextGroup, HAPParentContext parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	public static HAPContextStructureValueDefinitionFlat process(HAPContextStructureValueDefinitionFlat context, HAPParentContext parent, HAPContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPContextStructureValueDefinitionGroup contextGroup = new HAPContextStructureValueDefinitionGroup();
		contextGroup.setContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPContextStructureValueDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
		return processed.getContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPContextStructureValueDefinitionGroup process(HAPContextStructureValueDefinitionGroup contextGroup, HAPParentContext parent, HAPContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPContextStructureValueDefinitionGroup out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPContextStructureValueDefinitionGroup processStatic(HAPContextStructureValueDefinitionGroup contextGroup, HAPParentContext parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
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
	
	public static HAPContextStructureValueDefinitionGroup processRelative(HAPContextStructureValueDefinitionGroup contextGroup, HAPParentContext parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		
		return contextGroup;
		
	}
}
