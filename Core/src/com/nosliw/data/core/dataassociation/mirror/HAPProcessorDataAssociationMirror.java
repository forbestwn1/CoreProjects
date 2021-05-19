package com.nosliw.data.core.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.temp.HAPUtilityContextStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinition;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMirror dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation, input);
		out.setInput(input);
	
		HAPContainerStructure outContext = output.cloneStructureContainer();
		for(String categaryName : input.getStructureNames()) {
			HAPValueStructureDefinition inputContext = input.getStructure(categaryName);
			HAPValueStructureDefinition outputContext = outContext.getStructure(categaryName);
			HAPValueStructureDefinition mergedOutputContext = HAPUtilityContextStructure.hardMergeContextStructure(inputContext, outputContext);
			outContext.addStructure(categaryName, mergedOutputContext);
		}
		
		for(String name : outContext.getStructureNames()) {
			out.addOutputStructure(name, outContext.getStructure(name));
		}
		return out;
	}


}
