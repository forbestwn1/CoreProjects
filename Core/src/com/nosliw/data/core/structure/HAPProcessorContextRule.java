package com.nosliw.data.core.structure;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPProcessorContextRule {

	//process rule in data variable in context 
	public static HAPStructureValueDefinitionGroup process(HAPStructureValueDefinitionGroup orgContext, HAPRuntimeEnvironment runtimeEnv) {

		for(String group : orgContext.getAllContextTypes()) {
			HAPStructureValueDefinitionFlat context = orgContext.getFlat(group);
			for(String eleName : context.getRootNames()) {
				HAPUtilityContext.processContextRootElement(context.getRoot(eleName), eleName, new HAPProcessorContextDefinitionElement() {
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
		}
		

		
		return orgContext;
	}

}
