package com.nosliw.core.application.division.manual.definition;

public class HAPManualDefinitionContextParse {

	private String m_basePath;
	
	private String m_brickDivision;
	
	public HAPManualDefinitionContextParse(String basePath, String brickDivision) {
		this.m_basePath = basePath;
		this.m_brickDivision = brickDivision;
	}
	
	public String getBasePath() {    return this.m_basePath;    }
	
	public String getBrickDivision() {   return this.m_brickDivision;   }
	
}
