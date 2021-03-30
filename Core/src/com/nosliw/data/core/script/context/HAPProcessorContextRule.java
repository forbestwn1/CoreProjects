package com.nosliw.data.core.script.context;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPProcessorContextRule {

	//process rule in data variable in context 
	public static HAPContextGroup process(HAPContextGroup orgContext, HAPRuntimeEnvironment runtimeEnv) {

		for(String group : orgContext.getAllContextTypes()) {
			HAPContext context = orgContext.getContext(group);
			for(String eleName : context.getElementNames()) {
				HAPUtilityContext.processContextRootElement(context.getElement(eleName), eleName, new HAPContextDefEleProcessor() {
					@Override
					public Pair<Boolean, HAPContextDefinitionElement> process(HAPInfoContextNode eleInfo, Object obj) {
						if(eleInfo.getContextElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
							HAPContextDefinitionLeafData dataEle = (HAPContextDefinitionLeafData)eleInfo.getContextElement();
							if(dataEle.getDataInfo()!=null)    dataEle.getDataInfo().process(runtimeEnv);  
						}
						return null;
					}

					@Override
					public void postProcess(HAPInfoContextNode eleInfo, Object value) { }
				}, null);
			}
		}
		

		
		return orgContext;
	}

}
