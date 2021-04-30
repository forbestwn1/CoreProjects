package com.nosliw.data.core.structure.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPUtilityContextStructure;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationMirror dataAssociation, HAPParentContext output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation, input);
		out.setInput(input);
	
		HAPParentContext outContext = output.cloneParentContext();
		for(String categaryName : input.getNames()) {
			HAPStructureValueDefinition inputContext = input.getContext(categaryName);
			HAPStructureValueDefinition outputContext = outContext.getContext(categaryName);
			HAPStructureValueDefinition mergedOutputContext = HAPUtilityContextStructure.hardMergeContextStructure(inputContext, outputContext);
			outContext.addContext(categaryName, mergedOutputContext);
		}
		
		for(String name : outContext.getNames()) {
			out.addOutputStructure(name, outContext.getContext(name));
		}
		return out;
	}


}
