package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBrick {

	private HAPIdBrickType m_brickType;
	
	private Class<? extends HAPManualBrick> m_brickClass;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginProcessorBrick(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv) {
		this.m_brickType = brickType;
		this.m_brickClass = brickClass;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPIdBrickType getBrickType() {    return this.m_brickType;     }

	public HAPManualBrick newInstance() {
		HAPManualBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickType(m_brickType);
			out.setRuntimeEnvironment(this.getRuntimeEnvironment());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {   return this.m_runtimeEnv;     }
}
