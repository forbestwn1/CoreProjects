package com.nosliw.data.core.service.interfacee;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.interactive.HAPDefinitionInteractive;
import com.nosliw.data.core.interactive.HAPDefinitionInteractiveImpBasic;

@HAPEntityWithAttribute
public class HAPServiceInterface extends HAPDefinitionInteractiveImpBasic{

	@Override
	public HAPDefinitionInteractive cloneInteractiveDefinition() {
		HAPServiceInterface out = new HAPServiceInterface();
		this.cloneToInteractive(out);
		return out;
	}

}
