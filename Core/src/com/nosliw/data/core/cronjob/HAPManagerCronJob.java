package com.nosliw.data.core.cronjob;

import com.nosliw.data.core.common.HAPEntityOrReference;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManagerCronJob {

	private HAPManagerResourceDefinition m_resourceDefManager;

	public HAPDefinitionCronJob getCronJobDefinition(HAPResourceId cronJobId, HAPAttachmentContainer parentAttachment) {
		//get definition itself
		HAPDefinitionCronJob cronJobDef = (HAPDefinitionCronJob)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(cronJobId, parentAttachment);
		return cronJobDef;
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
		
	}

	public HAPExecutableModule getEmbededUIModule(HAPEntityOrReference defOrRef, HAPAttachmentContainer parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionModule moduleDef = null;
		String id = null;
		HAPAttachmentContainer attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId moduleId = (HAPResourceId)defOrRef;
			moduleDef = getModuleDefinition(moduleId, attachmentEx);
			id = moduleId.getIdLiterate();
		}
		else if(defOrRef instanceof HAPResourceDefinition) {
			moduleDef = (HAPDefinitionModule)defOrRef;
			HAPUtilityComponent.mergeWithParentAttachment(moduleDef, attachmentEx);
		}
		return HAPProcessorModule.process(moduleDef, id, null, m_processMan, this, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager);
	}


}
