package com.nosliw.data.core.component.command;

import com.nosliw.data.core.interactive.HAPWithInteractiveImpBasic;

public class HAPDefinitionCommand extends HAPWithInteractiveImpBasic{

	public HAPDefinitionCommand cloneCommandDefinition() {
		HAPDefinitionCommand out = new HAPDefinitionCommand();
		this.cloneToWithInteractive(out);
		
		return out;
	}
	
}
