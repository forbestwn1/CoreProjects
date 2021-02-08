package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPChildrenComponentIdContainer {

	private Map<String, List<HAPChildrenComponentId>> m_children;
	
	public HAPChildrenComponentIdContainer() {
		this.m_children = new LinkedHashMap<String, List<HAPChildrenComponentId>>();
	}
	
	public void addChildCompoentId(HAPChildrenComponentId childComponentId, HAPContainerAttachment parentAttachment) {
		HAPResourceId resourceId = childComponentId.getResourceId();
		
		HAPAttachmentUtility.mergeContainerToResourceIdSupplement(resourceId, parentAttachment);
		
		List<HAPChildrenComponentId> byName = this.m_children.get(resourceId.getType());
		if(byName==null) {
			byName = new ArrayList<HAPChildrenComponentId>();
			this.m_children.put(resourceId.getType(), byName);
		}
		byName.add(childComponentId);
	}

	public Map<String, List<HAPChildrenComponentId>> getChildren(){
		return this.m_children;
	}
}
