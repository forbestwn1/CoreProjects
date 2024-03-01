package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.data.core.domain.entity.attachment.HAPProcessorAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment1.HAPInfoAttachment;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public class HAPProcessorAttachmentEntityActivity implements HAPProcessorAttachmentEntity{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPProcessorAttachmentEntityActivity(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPManualEntityComplex complexEntity) {
		return HAPParserActivity.parseActivityDefinition((JSONObject)attachmentInfo.getEntity(), complexEntity, m_activityPluginMan);	
	}

}
