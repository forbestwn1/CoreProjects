package com.nosliw.data.core.activity;

public class HAPManagerActivity {

	private HAPManagerActivityPlugin m_pluginManager;
	
	public HAPManagerActivity(HAPManagerActivityPlugin pluginManager) {
		this.m_pluginManager = pluginManager;
	}
	
	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }

}
