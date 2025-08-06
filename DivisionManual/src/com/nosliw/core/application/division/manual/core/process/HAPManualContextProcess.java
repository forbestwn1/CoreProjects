package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualContextProcess {

	private HAPBundle m_bundle;
	
	private String m_rootBrickName;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPManualContextProcess(HAPBundle bundle, String rootBrickName, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		this.m_bundle = bundle;
		this.m_rootBrickName = rootBrickName;
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickManager = brickMan;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }
	
	public String getRootBrickName() {    return this.m_rootBrickName;       }

	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	
	public HAPManagerApplicationBrick getBrickManager() {   return this.m_brickManager;     }

}
