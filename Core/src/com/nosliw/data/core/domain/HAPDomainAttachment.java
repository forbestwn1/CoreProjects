package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;

public class HAPDomainAttachment extends HAPSerializableImp{

	public static final String ATTACHMENT = "attachment";
	
	private Map<String, HAPDefinitionEntityContainerAttachment> m_attachmentContainerByComplexeExeId;
	
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainAttachment() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_attachmentContainerByComplexeExeId = new LinkedHashMap<String, HAPDefinitionEntityContainerAttachment>();
	}
	
	public String addAttachmentContainer(HAPDefinitionEntityContainerAttachment attachmentContainer) {
		String out = this.m_idGenerator.generateId();
		if(attachmentContainer!=null)		this.m_attachmentContainerByComplexeExeId.put(out, attachmentContainer.cloneAttachmentContainer());
		else this.m_attachmentContainerByComplexeExeId.put(out, new HAPDefinitionEntityContainerAttachment());
		return out;
	}
	
	public HAPDefinitionEntityContainerAttachment getAttachmentContainer(String attachmentId) {
		return this.m_attachmentContainerByComplexeExeId.get(attachmentId);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		Map<String, String> attachmentJsonMap = new LinkedHashMap<String, String>();
		for(String id : this.m_attachmentContainerByComplexeExeId.keySet()) {
			attachmentJsonMap.put(id, this.m_attachmentContainerByComplexeExeId.get(id).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTACHMENT, HAPUtilityJson.buildMapJson(attachmentJsonMap));
	}
}
