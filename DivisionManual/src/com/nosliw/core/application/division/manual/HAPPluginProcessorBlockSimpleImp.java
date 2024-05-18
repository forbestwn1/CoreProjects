package com.nosliw.core.application.division.manual;

public abstract class HAPPluginProcessorBlockSimpleImp implements HAPPluginProcessorBlockSimple{

	private String m_entityType;

	public HAPPluginProcessorBlockSimpleImp(String entityType) {
		this.m_entityType = entityType;
	}
	
	@Override
	public String getEntityType() {  return this.m_entityType;  }

}
