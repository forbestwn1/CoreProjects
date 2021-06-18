package com.nosliw.data.core.dataassociation.mirror;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.temp.HAPUtilityContextStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

public class HAPProcessorDataAssociationMirror {

	public static HAPExecutableDataAssociationMirror processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMirror dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMirror out = new HAPExecutableDataAssociationMirror(dataAssociation, input, output);
		out.setInput(input);
	
		HAPContainerStructure outContext = output.cloneStructureContainer();
		for(String inputStructureName : input.getStructureNames()) {
			HAPValueStructure inputStructure = input.getStructure(inputStructureName);
			HAPValueStructure outputStructure = outContext.getStructure(inputStructureName);
			HAPValueStructure mergedOutputContext = HAPUtilityContextStructure.hardMergeContextStructure(inputStructure, outputStructure);
			outContext.addStructure(inputStructureName, mergedOutputContext);
		}
		
		for(String name : outContext.getStructureNames()) {
			out.addOutputStructure(name, outContext.getStructure(name));
		}
		return out;
	}


}
