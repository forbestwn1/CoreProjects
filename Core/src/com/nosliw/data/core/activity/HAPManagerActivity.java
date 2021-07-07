package com.nosliw.data.core.activity;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.resource.HAPResourceDefinitionActivitySuite;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerActivity {

	private HAPManagerActivityPlugin m_pluginManager;
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerActivity(HAPManagerActivityPlugin pluginManager, HAPRuntimeEnvironment runtimeEnv) {
		this.m_pluginManager = pluginManager;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPManagerActivityPlugin getPluginManager() {   return this.m_pluginManager;    }

	public HAPExecutableTaskSuite getActivitySuite(HAPResourceId activitySuiteId) {
		HAPResourceDefinitionActivitySuite activitySuiteResourceDef = (HAPResourceDefinitionActivitySuite)this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(activitySuiteId);
		HAPContextProcessAttachmentReferenceActivity contextProcess = new HAPContextProcessAttachmentReferenceActivity(activitySuiteResourceDef, this.m_runtimeEnv);
		
		HAPExecutableTaskSuite out = HAPProcessorActivitySuite.process(
				activitySuiteId.toStringValue(HAPSerializationFormat.LITERATE), 
				activitySuiteResourceDef,
				contextProcess, 
				null, 
				this.m_runtimeEnv,
				new HAPProcessTracker());
		return out;
	}
}
