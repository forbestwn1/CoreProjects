package com.nosliw.core.application;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPInfoBrickDivision extends HAPEntityInfoImp{

	private HAPPluginRepositoryBundle m_entityPackageRepositoryPlugin;

	private HAPPluginProcessorBrick m_entityProcessorPlugin;
	
	public HAPInfoBrickDivision(HAPPluginRepositoryBundle entityPackageRepositoryPlugin, HAPPluginProcessorBrick entityProcessorPlugin) {
		this.m_entityPackageRepositoryPlugin = entityPackageRepositoryPlugin;
		this.m_entityProcessorPlugin = entityProcessorPlugin;
	}
	
	public HAPPluginRepositoryBundle getEntityPackageRepositoryPlugin() {    return this.m_entityPackageRepositoryPlugin;     }
	
	public HAPPluginProcessorBrick getEntityProcessorPlugin() {   return this.m_entityProcessorPlugin;     }

}
