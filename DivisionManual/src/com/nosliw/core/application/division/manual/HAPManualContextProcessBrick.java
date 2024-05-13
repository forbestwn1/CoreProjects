package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcessBrick {

	private HAPBundle m_bundle;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualContextProcessBrick(HAPBundle bundle, HAPRuntimeEnvironment runtimeEnv) {
		this.m_bundle = bundle;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }

	public HAPRuntimeEnvironment getRuntimeEnv() {   return this.m_runtimeEnv;    }
}
