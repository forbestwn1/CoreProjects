package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcessBrick {

	private HAPBundle m_bundle;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	public HAPManualContextProcessBrick(HAPBundle bundle, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		this.m_bundle = bundle;
		this.m_runtimeEnv = runtimeEnv;
		this.m_manualBrickMan = manualBrickMan;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }

	public HAPRuntimeEnvironment getRuntimeEnv() {   return this.m_runtimeEnv;    }
	
	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	
}
