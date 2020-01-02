package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPChildrenComponentIdContainer {

	private Map<String, Map<String, HAPResourceId>> m_children;
	
	public void addChildCompoentId(String name, HAPResourceId resourceId, HAPAttachmentContainer parentAttachment) {
		
		HAPAttachmentContainer merged = new HAPAttachmentContainer(resourceId.getSupplement());
		merged.merge(parentAttachment, HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD);
		HAPResourceId mergedResourceId = HAPResourceId.newInstance(resourceId.getType(), resourceId.getId(), merged.toResourceIdSupplement());
		
		Map<String, HAPResourceId> byName = this.m_children.get(mergedResourceId.getType());
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPResourceId>();
			this.m_children.put(mergedResourceId.getType(), byName);
		}
		byName.put(name, mergedResourceId);
	}

	
}
