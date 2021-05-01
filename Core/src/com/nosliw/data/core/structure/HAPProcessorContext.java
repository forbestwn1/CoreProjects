package com.nosliw.data.core.structure;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPProcessorContext {

	public static HAPStructureValueDefinition process(HAPStructureValueDefinition context, HAPParentContext parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPStructureValueDefinition out = null;
		if(context!=null) {
			String type = context.getType();
			if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				out = process((HAPStructureValueDefinitionFlat)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
			else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				out = process((HAPStructureValueDefinitionGroup)context, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
		}
		return out;
	}
	
	public static HAPStructureValueDefinitionGroup processRelative(HAPStructureValueDefinitionGroup contextGroup, HAPParentContext parent, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	public static HAPStructureValueDefinitionFlat process(HAPStructureValueDefinitionFlat context, HAPParentContext parent, HAPContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPStructureValueDefinitionGroup contextGroup = new HAPStructureValueDefinitionGroup();
		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPStructureValueDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
		return processed.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPStructureValueDefinitionGroup process(HAPStructureValueDefinitionGroup contextGroup, HAPParentContext parent, HAPContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		HAPStructureValueDefinitionGroup out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPStructureValueDefinitionGroup processStatic(HAPStructureValueDefinitionGroup contextGroup, HAPParentContext parent, HAPContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
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
	
	public static HAPStructureValueDefinitionGroup processRelative(HAPStructureValueDefinitionGroup contextGroup, HAPParentContext parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorStructure();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		
		return contextGroup;
		
	}
}
