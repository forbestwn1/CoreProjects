package com.nosliw.data.core.structure;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorElementRule {

	//process rule in data variable in context 
	public static HAPStructure process(HAPStructure structure, HAPRuntimeEnvironment runtimeEnv) {

		for(HAPRoot root : structure.getAllRoots()) {
			HAPUtilityStructure.traverseElement(root, new HAPProcessorContextDefinitionElement() {
				@Override
				public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object obj) {
					if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
						HAPElementLeafData dataEle = (HAPElementLeafData)eleInfo.getElement();
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
