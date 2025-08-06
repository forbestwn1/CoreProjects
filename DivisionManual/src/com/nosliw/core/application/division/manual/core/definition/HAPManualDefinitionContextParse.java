package com.nosliw.core.application.division.manual.core.definition;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualDefinitionContextParse {

	private String m_basePath;
	
	private String m_brickDivision;
	
	private HAPManualManagerBrick m_manualBrickMan;
	
	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPManualDefinitionContextParse(String basePath, String brickDivision, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		this.m_basePath = basePath;
		this.m_brickDivision = brickDivision;
		this.m_manualBrickMan = manualBrickMan;
		this.m_brickManager = brickMan;
	}
	
	public String getBasePath() {    return this.m_basePath;    }
	
	public String getBrickDivision() {   return this.m_brickDivision;   }
	
	public HAPManualManagerBrick getManualBrickManager() {    return this.m_manualBrickMan;      }
	
	public HAPManagerApplicationBrick getBrickManager() {   return this.m_brickManager;     }

}
