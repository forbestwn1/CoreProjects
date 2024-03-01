package com.nosliw.data.core.entity;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPInfoDivision extends HAPEntityInfoImp{

	private HAPPluginRepositoryEntityPackage m_entityPackageRepositoryPlugin;

	private HAPPluginProcessorEntity m_entityProcessorPlugin;
	
	public HAPInfoDivision(HAPPluginRepositoryEntityPackage entityPackageRepositoryPlugin, HAPPluginProcessorEntity entityProcessorPlugin) {
		this.m_entityPackageRepositoryPlugin = entityPackageRepositoryPlugin;
		this.m_entityProcessorPlugin = entityProcessorPlugin;
	}
	
	public HAPPluginRepositoryEntityPackage getEntityPackageRepositoryPlugin() {    return this.m_entityPackageRepositoryPlugin;     }
	
	public HAPPluginProcessorEntity getEntityProcessorPlugin() {   return this.m_entityProcessorPlugin;     }

}
