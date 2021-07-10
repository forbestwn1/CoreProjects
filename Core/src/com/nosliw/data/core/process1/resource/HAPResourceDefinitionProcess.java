package com.nosliw.data.core.process1.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.process1.HAPDefinitionProcess;

public class HAPResourceDefinitionProcess extends HAPComponentContainerElement implements HAPDefinitionProcess{

	private HAPResourceDefinitionProcess() {}
	
	public HAPResourceDefinitionProcess(HAPResourceDefinitionProcessSuite suite, String process) {
		super(suite, process);
	}

	public HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite getProcess() {    return (HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite)this.getComponentEntity();   }
	public HAPResourceDefinitionProcessSuite getSuite() {   return (HAPResourceDefinitionProcessSuite)this.getResourceContainer();  }

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPResourceDefinitionProcess out = new HAPResourceDefinitionProcess();
		this.cloneToComponentContainerElement(out);
		return out;
	}

}
