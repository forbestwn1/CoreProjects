package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSupplement;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;

//define attachment, the entity that component need
@HAPEntityWithAttribute
public class HAPAttachmentContainer extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	private Map<String, Map<String, HAPAttachment>> m_element;
	
	public HAPAttachmentContainer() {
		this.m_element = new LinkedHashMap<>();
	}

	public HAPAttachmentContainer(HAPResourceIdSupplement resourceIdSupplement) {
		this();
		if(resourceIdSupplement!=null) {
			Map<String, Map<String, HAPResourceId>> resourceIds = resourceIdSupplement.getAllSupplymentResourceId();
			for(String type : resourceIds.keySet()) {
				Map<String, HAPResourceId> byName = resourceIds.get(type);
				for(String name : byName.keySet()) {
					HAPAttachmentReference ele = new HAPAttachmentReference(type);
					ele.setName(name);
					ele.setId(byName.get(name));
					this.addAttachment(type, ele);
				}
			}
		}
	}
	
	public Map<String, HAPAttachment> getAttachmentByType(String type){
		return this.m_element.get(type);
	}
	
	public void addAttachment(String type, HAPAttachment attachment) {
		Map<String, HAPAttachment> byName = this.m_element.get(type);
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPAttachment>();
			this.m_element.put(type, byName);
		}
		byName.put(attachment.getName(), attachment);
	}

	//merge with parent
	public void merge(HAPAttachmentContainer parent, String mode) {
		if(mode==null)   mode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE))  return;
		
		for(String type : parent.m_element.keySet()) {
			Map<String, HAPAttachment> byName = parent.m_element.get(type);
			for(String name : byName.keySet()) {
				HAPAttachment parentEle = byName.get(name);
				HAPAttachment thisEle = this.getElement(type, name);
				if(thisEle==null || thisEle.getType().equals(HAPConstant.ATTACHMENT_TYPE_PLACEHOLDER)) {
					//element not exist, or empty, borrow from parent
					HAPAttachment newEle = parentEle.clone();
					HAPAttachmentUtility.setOverridenByParent(newEle);
					this.addAttachment(type, newEle);
				}
				else {
					if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT)&&HAPAttachmentUtility.isOverridenByParentMode(thisEle)) {
						//if configurable, then parent override child
						HAPAttachment newEle = parentEle.clone();
						this.addAttachment(type, newEle);
					}
				}
			}
		}
	}

	public HAPResourceIdSupplement toResourceIdSupplement(){
		Map<String, Map<String, HAPResourceId>> resourceIds = new LinkedHashMap<String, Map<String, HAPResourceId>>();
		for(String type : this.m_element.keySet()) {
			Map<String, HAPAttachment> byName = this.m_element.get(type);
			Map<String, HAPResourceId> byNameOut = new LinkedHashMap<>();
			for(String name : byName.keySet()) {
				HAPAttachment attachment = byName.get(name);
				if(attachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_REFERENCE)) {
					byNameOut.put(name, ((HAPAttachmentReference)attachment).getId());
				}
			}
			resourceIds.put(type, byNameOut);
		}
		return HAPResourceIdSupplement.newInstance(resourceIds);
	}
	
	public HAPAttachment getElement(String type, String name) {
		HAPAttachment out = null;
		Map<String, HAPAttachment> byName = this.m_element.get(type);
		if(byName!=null) {
			out = byName.get(name);
		}
		return out;
	}
}
