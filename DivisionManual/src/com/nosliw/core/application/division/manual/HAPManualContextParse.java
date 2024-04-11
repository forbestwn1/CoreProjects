package com.nosliw.core.application.division.manual;

public class HAPManualContextParse {

	private String m_basePath;
	
	private String m_brickDivision;
	
	public HAPManualContextParse(String basePath, String brickDivision) {
		this.m_basePath = basePath;
		this.m_brickDivision = brickDivision;
	}
	
	public String getBasePath() {    return this.m_basePath;    }
	
	public String getBrickDivision() {   return this.m_brickDivision;   }
	
}
