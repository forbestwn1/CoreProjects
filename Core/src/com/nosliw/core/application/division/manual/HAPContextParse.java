package com.nosliw.core.application.division.manual;

public class HAPContextParse {

	private String m_basePath;
	
	private String m_entityDivision;
	
	public HAPContextParse(String basePath, String entityDivision) {
		this.m_basePath = basePath;
		this.m_entityDivision = entityDivision;
	}
	
	public String getBasePath() {    return this.m_basePath;    }
	
	public String getEntityDivision() {   return this.m_entityDivision;   }
	
}
