package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

public class HAPChildrenComponentIdContainer {

	private Map<String, List<HAPChildrenComponentId>> m_children;
	
	public HAPChildrenComponentIdContainer() {
		this.m_children = new LinkedHashMap<String, List<HAPChildrenComponentId>>();
	}
	
	public void addChildCompoentId(HAPChildrenComponentId childComponentId, HAPAttachmentContainer parentAttachment) {
		HAPResourceId resourceId = childComponentId.getResourceId();
		HAPAttachmentContainer merged = new HAPAttachmentContainer(resourceId.getSupplement());
		merged.merge(parentAttachment, HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD);
		HAPResourceId mergedResourceId = HAPResourceIdFactory.newInstance(resourceId, merged.toResourceIdSupplement());
		childComponentId.setResourceId(mergedResourceId);
		
		List<HAPChildrenComponentId> byName = this.m_children.get(mergedResourceId.getType());
		if(byName==null) {
			byName = new ArrayList<HAPChildrenComponentId>();
			this.m_children.put(mergedResourceId.getType(), byName);
		}
		byName.add(childComponentId);
	}

	public Map<String, List<HAPChildrenComponentId>> getChildren(){
		return this.m_children;
	}
}
