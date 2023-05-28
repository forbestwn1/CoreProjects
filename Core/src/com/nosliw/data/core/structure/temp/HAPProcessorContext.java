package com.nosliw.data.core.structure.temp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPProcessorElementConstant;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorContext {

	public static HAPValueStructureDefinitionGroup processStatic(HAPValueStructureDefinitionGroup contextGroup, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorElementConstant.process(contextGroup, attachmentContainer, configure, runtimeEnv);

		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, runtimeEnv);
		
		//process data rule
		HAPProcessorContextRule.process(contextGroup, runtimeEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextVariableInheritance.process(contextGroup, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return contextGroup;
	}


	public static HAPValueStructure process(HAPValueStructure valueStructure, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		HAPValueStructure out = null;
		if(valueStructure!=null) {
			String type = valueStructure.getStructureType();
			if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT)) {
				out = process((HAPValueStructureDefinitionFlat)valueStructure, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
			else if(type.equals(HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
				out = process((HAPValueStructureDefinitionGroup)valueStructure, parent, attachmentContainer, new HashSet<String>(), errors, configure, runtimeEnv);
			}
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionGroup processRelative(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		return processRelative(contextGroup, parent, new HashSet<String>(), errors, configure, runtimeEnv);
	}
	
	public static HAPValueStructureDefinitionFlat process(HAPValueStructureDefinitionFlat context, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, Set<String> dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		HAPValueStructureDefinitionGroup contextGroup = new HAPValueStructureDefinitionGroup();
		contextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPValueStructureDefinitionGroup processed = process(contextGroup, parent, attachmentContainer, dependency, errors, configure, runtimeEnv);
		return processed.getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		HAPValueStructureDefinitionGroup out = processStatic(contextGroup, parent, attachmentContainer, errors, configure, runtimeEnv);
		out = processRelative(out, parent, dependency, errors, configure, runtimeEnv);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPValueStructureDefinitionGroup processStatic(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, HAPDefinitionEntityContainerAttachment attachmentContainer, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorElementConstant.process(contextGroup, parent, attachmentContainer, configure, runtimeEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, runtimeEnv);
		
		//process data rule
		HAPProcessorContextRule.process(contextGroup, runtimeEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextVariableInheritance.process(contextGroup, parent, configure.inheritMode, configure.inheritanceExcludedInfo, runtimeEnv);
		
		return contextGroup;
	}

	public static HAPValueStructureDefinitionGroup processRelative(HAPValueStructureDefinitionGroup contextGroup, HAPContainerStructure parent, Set<String>  dependency, List<HAPServiceData> errors, HAPConfigureProcessorValueStructure configure, HAPRuntimeEnvironment runtimeEnv) {
		if(configure==null)  configure = new HAPConfigureProcessorValueStructure();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, dependency, errors, configure, runtimeEnv);
		
		return contextGroup;
		
	}
}
