package com.nosliw.data.core.cronjob;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.service.HAPManagerServiceDefinition;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPManagerResourceDefinition;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.HAPWithNameMapping;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.expression.data1.HAPManagerExpression;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPManagerCronJob {

	private HAPManagerExpression m_expressionMan; 
	
	private HAPManagerResource m_resourceMan;

	private HAPRuntime m_runtime;

	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerProcess m_processMan;
	
	private HAPManagerServiceDefinition m_serviceDefinitionManager;
	
	private HAPManagerResourceDefinition m_resourceDefManager;
	
	private HAPManagerScheduleDefinition m_scheduleDefManager;
	
	public HAPManagerCronJob(
			HAPManagerExpression expressionMan, 
			HAPManagerResource resourceMan,
			HAPManagerProcess processMan,
			HAPRuntime runtime, 
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPManagerResourceDefinition resourceDefManager){
		this.m_expressionMan = expressionMan;
		this.m_resourceMan = resourceMan;
		this.m_processMan = processMan;
		this.m_runtime = runtime;
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_serviceDefinitionManager = serviceDefinitionManager;
		this.m_resourceDefManager = resourceDefManager;
		this.m_scheduleDefManager = new HAPManagerScheduleDefinition();
	}
	
	public HAPDefinitionCronJob getCronJobDefinition(HAPResourceId cronJobId, HAPDefinitionEntityContainerAttachment parentAttachment) {
		//get definition itself
		HAPDefinitionCronJob cronJobDef = (HAPDefinitionCronJob)this.m_resourceDefManager.getAdjustedComplextResourceDefinition(cronJobId, parentAttachment);
		return cronJobDef;
	}
	
	public HAPExecutableCronJob getCronJob(HAPResourceId cronJobId) {
		return this.getEmbededCronJob(cronJobId, null, null);
	}
	
	public HAPExecutableCronJob getEmbededCronJob(HAPEntityOrReference defOrRef, HAPDefinitionEntityContainerAttachment parentAttachment, HAPWithNameMapping withNameMapping) {
		HAPDefinitionCronJob cronJobDef = null;
		String id = null;
		HAPDefinitionEntityContainerAttachment attachmentEx = HAPUtilityComponent.buildNameMappedAttachment(parentAttachment, withNameMapping);
		if(defOrRef instanceof HAPResourceId) {
			HAPResourceId cronJobId = (HAPResourceId)defOrRef;
			cronJobDef = getCronJobDefinition(cronJobId, attachmentEx);
			id = cronJobId.getCoreIdLiterate();
		}
		else if(defOrRef instanceof HAPResourceDefinition1) {
			cronJobDef = (HAPDefinitionCronJob)defOrRef;
			HAPUtilityComponent.mergeWithParentAttachment(cronJobDef, attachmentEx);
		}
		return HAPProcessorCronJob.process(cronJobDef, id, null, m_processMan, m_dataTypeHelper, m_runtime, m_expressionMan, m_serviceDefinitionManager, m_resourceDefManager, null, m_scheduleDefManager);
		
	}

	public HAPManagerScheduleDefinition getScheduleDefinitionManager() {    return this.m_scheduleDefManager;    }
	
}
