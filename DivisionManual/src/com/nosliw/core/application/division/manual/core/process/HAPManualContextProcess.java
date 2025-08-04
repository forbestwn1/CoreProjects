package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualContextProcess {

	private HAPBundle m_bundle;
	
	private String m_rootBrickName;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	public HAPManualContextProcess(HAPBundle bundle, String rootBrickName, HAPManualManagerBrick manualBrickMan) {
		this.m_bundle = bundle;
		this.m_rootBrickName = rootBrickName;
		this.m_manualBrickMan = manualBrickMan;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }
	
	public String getRootBrickName() {    return this.m_rootBrickName;       }

	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }

}
