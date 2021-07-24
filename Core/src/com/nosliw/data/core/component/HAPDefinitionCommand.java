package com.nosliw.data.core.component;

import com.nosliw.data.core.common.HAPWithInteractiveImpBasic;

public class HAPDefinitionCommand extends HAPWithInteractiveImpBasic{

	public HAPDefinitionCommand cloneCommandDefinition() {
		HAPDefinitionCommand out = new HAPDefinitionCommand();
		this.cloneToWithInteractive(out);
		
		return out;
	}
	
}
