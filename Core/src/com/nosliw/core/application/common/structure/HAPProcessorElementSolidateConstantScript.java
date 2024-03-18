package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorElementSolidateConstantScript {

	
	static public HAPStructure1 process(
			HAPStructure1 structure,
			HAPRuntimeEnvironment runtimeEnv){
		//find all constants
		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(structure);

		HAPStructure1 out = (HAPStructure1)structure.solidateConstantScript(constantsValue, runtimeEnv);
		
		//solidate element in root
		for(HAPRootStructure root : out.getAllRoots()) {
			root.setDefinition((HAPElementStructure)root.getDefinition().solidateConstantScript(constantsValue, runtimeEnv));
		}
		
		return out;
	}

}
