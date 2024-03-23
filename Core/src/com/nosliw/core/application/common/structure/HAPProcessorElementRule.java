package com.nosliw.core.application.common.structure;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorElementRule {

	//process rule in data variable in context 
	public static HAPStructure1 process(HAPStructure1 structure, HAPRuntimeEnvironment runtimeEnv) {

		for(HAPRootStructure root : structure.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorStructureElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
						HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)eleInfo.getElement();
						if(dataEle.getDataInfo()!=null)    dataEle.getDataInfo().process(runtimeEnv);  
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) { }
			}, null);
			
		}
		return structure;
	}

}
