package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualPluginProcessorBrick {

	private HAPIdBrickType m_brickType;
	
	private Class<? extends HAPManualBrick> m_brickClass;
	
	public HAPManualPluginProcessorBrick(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass) {
		this.m_brickType = brickType;
		this.m_brickClass = brickClass;
	}
	
	public HAPIdBrickType getBrickType() {    return this.m_brickType;     }

	public HAPManualBrick newInstance(HAPBundle bundle, HAPManualManagerBrick manualBrickMan) {
		HAPManualBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickType(m_brickType);
			out.setBundle(bundle);
			out.setManualBrickManager(manualBrickMan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
}
