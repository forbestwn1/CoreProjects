package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcessAdapter {

	private HAPBundle m_bundle;
	
	private HAPPath m_baseBrickPath;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle, HAPPath baseBrickPath, HAPRuntimeEnvironment runtimeEnv) {
		this.m_bundle = bundle;
		this.m_baseBrickPath = baseBrickPath;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }

	public HAPPath getRootPathForBaseBrick() {    return this.m_baseBrickPath;    }
	
	public HAPRuntimeEnvironment getRuntimeEnvironment() {     return this.m_runtimeEnv;      }
	
}
