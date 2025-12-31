package com.nosliw.core.application.division.manual.brick.module;

import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.module.HAPBlockModule;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockModule extends HAPManualBrickImp implements HAPBlockModule{

	@Override
	public HAPBrickContainer getTasks() {   return (HAPBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.TASK);   }   

	@Override
	public HAPBrickContainer getCommands() {   return (HAPBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.COMMAND);   }

	@Override
	public HAPBrickContainer getPages() {   return (HAPBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.PAGE);   }

}
