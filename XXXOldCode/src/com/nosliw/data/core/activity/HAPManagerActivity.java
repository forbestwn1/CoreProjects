package com.nosliw.data.core.activity;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerActivity {

	private HAPManagerActivityPlugin m_pluginManager;
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerActivity(HAPManagerActivityPlugin pluginManager, HAPRuntimeEnvironment runtimeEnv) {
		this.m_pluginManager = pluginManager;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }

}
