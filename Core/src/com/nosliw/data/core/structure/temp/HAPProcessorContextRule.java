package com.nosliw.data.core.structure.temp;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPProcessorStructureElement;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorContextRule {

	//process rule in data variable in context 
	public static HAPValueStructureDefinitionGroup process(HAPValueStructureDefinitionGroup orgContext, HAPRuntimeEnvironment runtimeEnv) {

		for(String group : orgContext.getAllCategaries()) {
			HAPValueStructureDefinitionFlat context = orgContext.getFlat(group);
			for(String eleName : context.getRootNames()) {
				HAPUtilityContext.processContextRootElement(context.getRoot(eleName), eleName, new HAPProcessorStructureElement() {
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
		}
		

		
		return orgContext;
	}

}
