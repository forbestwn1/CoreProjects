package com.nosliw.data.core.cronjob;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.common.HAPEntityOrReference;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPManagerCronJob {

	private HAPExpressionSuiteManager m_expressionMan; 
	
	private HAPResourceManagerRoot m_resourceMan;

	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerProcess m_processMan;
	
	private HAPManagerServiceDefinition m_serviceDefinitionManager;
	
	private HAPManagerResourceDefinition m_resourceDefManager;
	
	
	public HAPDefinitionCronJob getCronJobDefinition(HAPResourceId cronJobId, HAPAttachmentContainer parentAttachment) {
		//get definition itself
		HAPDefinitionCronJob cronJobDef = (HAPDefinitionCronJob)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(cronJobId, parentAttachment);
		return cronJobDef;
	}
	
	public HAPExecutableCronJob getCronJob(HAPResourceId cronJobId) {
		return this.getEmbededCronJob(cronJobId, null, null);
	}
	
	public HAPExecutableCronJob getEmbededCronJob(HAPEntityOrReference defOrRef, HAPAttachmentContainer parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionCronJob cronJobDef = null;
		String id = null;
		HAPAttachmentContainer attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId cronJobId = (HAPResourceId)defOrRef;
			cronJobDef = getCronJobDefinition(cronJobId, attachmentEx);
			id = cronJobId.getIdLiterate();
		}
		else if(defOrRef instanceof HAPResourceDefinition) {
			cronJobDef = (HAPDefinitionCronJob)defOrRef;
			HAPUtilityComponent.mergeWithParentAttachment(cronJobDef, attachmentEx);
		}
		return HAPProcessorCronJob.process(cronJobDef, id, null, m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager);
		
	}

}
