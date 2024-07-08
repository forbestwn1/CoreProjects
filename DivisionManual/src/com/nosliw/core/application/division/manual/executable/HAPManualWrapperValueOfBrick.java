package com.nosliw.core.application.division.manual.executable;

import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPWrapperValueOfBrick;

public class HAPManualWrapperValueOfBrick extends HAPWrapperValueOfBrick{

	public HAPManualWrapperValueOfBrick(HAPBrick brick) {
		super(brick);
	}

	public HAPManualBrick getManualBrick() {
		return (HAPManualBrick)this.getBrick();
	}
	
}
