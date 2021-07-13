package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.component.attachment.HAPInfoAttachment;
import com.nosliw.data.core.component.attachment.HAPProcessorAttachmentEntity;

public class HAPProcessorAttachmentEntityActivity implements HAPProcessorAttachmentEntity{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPProcessorAttachmentEntityActivity(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity) {
		return HAPParserActivity.parseActivityDefinition((JSONObject)attachmentInfo.getEntity(), complexEntity, m_activityPluginMan);	
	}

}
