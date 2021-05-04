package com.nosliw.data.core.structure.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPContainerStructure;
import com.nosliw.data.core.structure.HAPUtilityContextStructure;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMirror dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation, input);
		out.setInput(input);
	
		HAPContainerStructure outContext = output.cloneStructureContainer();
		for(String categaryName : input.getStructureNames()) {
			HAPStructureValueDefinition inputContext = input.getStructure(categaryName);
			HAPStructureValueDefinition outputContext = outContext.getStructure(categaryName);
			HAPStructureValueDefinition mergedOutputContext = HAPUtilityContextStructure.hardMergeContextStructure(inputContext, outputContext);
			outContext.addStructure(categaryName, mergedOutputContext);
		}
		
		for(String name : outContext.getStructureNames()) {
			out.addOutputStructure(name, outContext.getStructure(name));
		}
		return out;
	}


}
