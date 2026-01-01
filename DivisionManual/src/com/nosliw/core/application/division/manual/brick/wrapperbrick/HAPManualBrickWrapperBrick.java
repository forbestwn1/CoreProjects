package com.nosliw.core.application.division.manual.brick.wrapperbrick;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.wrapperbrick.HAPBrickWrapperBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrickWithEntityInfo;

public class HAPManualBrickWrapperBrick extends HAPManualBrickWithEntityInfo implements HAPBrickWrapperBrick{

	public static final String INFO = "info";
	
	@Override
	public HAPEntityOrReference getBrick() {   return this.getAttributeValueOfBrick(BRICK);  }

}
