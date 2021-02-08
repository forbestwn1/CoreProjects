package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPDefinitionProcess extends HAPComponentContainerElement{

	private HAPDefinitionProcess() {}
	
	public HAPDefinitionProcess(HAPDefinitionProcessSuite suite, String process) {
		super(suite, process);
	}

	public HAPDefinitionProcessSuiteElementEntity getProcess() {    return (HAPDefinitionProcessSuiteElementEntity)this.getComponentEntity();   }
	public HAPDefinitionProcessSuite getSuite() {   return (HAPDefinitionProcessSuite)this.getResourceContainer();  }

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPDefinitionProcess out = new HAPDefinitionProcess();
		this.cloneToComponentContainerElement(out);
		return out;
	}

}
