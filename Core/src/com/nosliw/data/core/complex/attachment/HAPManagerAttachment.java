package com.nosliw.data.core.complex.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public class HAPManagerAttachment {

	private Map<String, HAPProcessorAttachmentEntity> m_processors;
	
	public HAPManagerAttachment() {
		this.m_processors = new LinkedHashMap<String, HAPProcessorAttachmentEntity>();
	}
	
	public void registerProcessor(String attachmentType, HAPProcessorAttachmentEntity processor) {
		this.m_processors.put(attachmentType, processor);
	}
	
	public HAPProcessorAttachmentEntity getProcessor(String attachmentType) {    return this.m_processors.get(attachmentType);     }

	public Object parseEntityAttachment(HAPInfoAttachment attachmentInfo, HAPDefinitionEntityComplex complexEntity) {
		return this.getProcessor(attachmentInfo.getValueType()).parseEntityAttachment(attachmentInfo, complexEntity);
	}

	
}
