package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorElementSolidateConstantScript {

	
	static public HAPStructure process(
			HAPStructure structure,
			HAPRuntimeEnvironment runtimeEnv){
		//find all constants
		Map<String, Object> constantsValue = HAPUtilityStructure.discoverConstantValue(structure);

		HAPStructure out = (HAPStructure)structure.solidateConstantScript(constantsValue, runtimeEnv);
		
		//solidate element in root
		for(HAPRoot root : out.getAllRoots()) {
			root.setDefinition((HAPElement)root.getDefinition().solidateConstantScript(constantsValue, runtimeEnv));
		}
		
		return out;
	}

}
