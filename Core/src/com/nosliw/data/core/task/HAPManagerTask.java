package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.attachment.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.task.resource.HAPResourceDefinitionTaskSuite;

public class HAPManagerTask {

	private HAPRuntimeEnvironment m_runtimeEnv;
	private Map<String, HAPInfoTask> m_taskInfo;
	
	public HAPManagerTask(HAPRuntimeEnvironment runtimeEnv) {
		this.m_taskInfo = new LinkedHashMap<String, HAPInfoTask>();
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public HAPInfoTask getTaskInfo(String taskType) {		return this.m_taskInfo.get(taskType);	}
	
	public void registerTaskInfo(String taskType, HAPInfoTask taskInfo) {    this.m_taskInfo.put(taskType, taskInfo);   	}
	
	public HAPExecutableTaskSuite getTaskSuite(HAPResourceId activitySuiteId) {
		HAPResourceDefinitionTaskSuite activitySuiteResourceDef = (HAPResourceDefinitionTaskSuite)this.m_runtimeEnv.getResourceDefinitionManager().getResourceDefinition(activitySuiteId);
		HAPContextProcessAttachmentReference contextProcess = new HAPContextProcessAttachmentReference(activitySuiteResourceDef, this.m_runtimeEnv);
		
		HAPExecutableTaskSuite out = HAPProcessorTaskSuite.process(
				activitySuiteId.toStringValue(HAPSerializationFormat.LITERATE), 
				activitySuiteResourceDef,
				contextProcess, 
				null, 
				this.m_runtimeEnv,
				new HAPProcessTracker());
		return out;
	}

}
