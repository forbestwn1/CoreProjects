package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPDefinitionProcess extends HAPComponentContainerElement{

	public HAPDefinitionProcess(HAPDefinitionProcessSuite suite, String process) {
		super(suite, process);
	}

	public HAPDefinitionProcessSuiteElementEntity getProcess() {    return (HAPDefinitionProcessSuiteElementEntity)this.getElement();   }
	public HAPDefinitionProcessSuite getSuite() {   return (HAPDefinitionProcessSuite)this.getContainer();  }


	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS;  }

}
