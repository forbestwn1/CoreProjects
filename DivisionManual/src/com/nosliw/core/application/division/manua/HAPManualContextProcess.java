package com.nosliw.core.application.division.manua;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcess {

	private HAPBundle m_bundle;
	
	private String m_rootBrickName;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	public HAPManualContextProcess(HAPBundle bundle, String rootBrickName, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		this.m_bundle = bundle;
		this.m_rootBrickName = rootBrickName;
		this.m_runtimeEnv = runtimeEnv;
		this.m_manualBrickMan = manualBrickMan;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }
	
	public String getRootBrickName() {    return this.m_rootBrickName;       }

	public HAPRuntimeEnvironment getRuntimeEnv() {   return this.m_runtimeEnv;    }
	
	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }

}
