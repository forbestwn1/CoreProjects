package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPContainerChildReferenceResource {

	private Map<String, List<HAPInfoChildResource>> m_children;
	
	public HAPContainerChildReferenceResource() {
		this.m_children = new LinkedHashMap<String, List<HAPInfoChildResource>>();
	}
	
	public void addChildCompoentId(HAPInfoChildResource childResourceInfo, HAPDefinitionEntityContainerAttachment parentAttachment) {
		HAPResourceId resourceId = childResourceInfo.getResourceId();
		
		HAPUtilityAttachment.mergeContainerToResourceIdSupplement(resourceId, parentAttachment);
		
		List<HAPInfoChildResource> byName = this.m_children.get(resourceId.getResourceType());
		if(byName==null) {
			byName = new ArrayList<HAPInfoChildResource>();
			this.m_children.put(resourceId.getResourceType(), byName);
		}
		byName.add(childResourceInfo);
	}

	public Map<String, List<HAPInfoChildResource>> getChildren(){
		return this.m_children;
	}
}
