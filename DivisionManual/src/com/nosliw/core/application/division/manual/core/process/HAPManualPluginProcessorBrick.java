package com.nosliw.core.application.division.manual.core.process;

import org.springframework.beans.factory.annotation.Autowired;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualPluginProcessorBrick {

	private HAPIdBrickType m_brickType;
	
	private Class<? extends HAPManualBrick> m_brickClass;
	
	private HAPManagerApplicationBrick m_brickMan;
	
	private HAPManualManagerBrick m_manualBrickMan;

	public HAPManualPluginProcessorBrick(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass) {
		this.m_brickType = brickType;
		this.m_brickClass = brickClass;
	}
	
	@Autowired
	public void setBrickManager(HAPManagerApplicationBrick brickMan) {  this.m_brickMan = brickMan;     }
	@Autowired
	public void setManualBrickManager(HAPManualManagerBrick manualBrickMan) {   this.m_manualBrickMan = manualBrickMan;      }
	protected HAPManagerApplicationBrick getBrickManager() {    return this.m_brickMan;     }
	protected HAPManualManagerBrick getManualBrickManager() {   return this.m_manualBrickMan;      }

	public HAPIdBrickType getBrickType() {    return this.m_brickType;     }

	public HAPManualBrick newInstance(HAPBundle bundle) {
		HAPManualBrick out = null;
		try {
			out = this.m_brickClass.newInstance();
			out.setBrickType(m_brickType);
			out.setBundle(bundle);
			out.setManualBrickManager(this.m_manualBrickMan);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
}
