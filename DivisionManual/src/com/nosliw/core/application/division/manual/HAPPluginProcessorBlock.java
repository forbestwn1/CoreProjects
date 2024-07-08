package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;

public class HAPPluginProcessorBlock {

	private HAPIdBrickType m_brickType;
	
	private Class<? extends HAPManualBrick> m_brickClass;
	
	public HAPPluginProcessorBlock(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass) {
		this.m_brickType = brickType;
		this.m_brickClass = brickClass;
	}
	
	public HAPIdBrickType getBrickType() {    return this.m_brickType;     }

	public HAPManualBrick newInstance() {
		HAPManualBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickType(m_brickType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
