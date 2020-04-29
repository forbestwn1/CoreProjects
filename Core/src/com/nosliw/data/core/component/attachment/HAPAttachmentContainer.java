package com.nosliw.data.core.component.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
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
				Map<String, HAPResourceId> byId = resourceIds.get(type);
				for(String id : byId.keySet()) {
					HAPAttachmentReference ele = new HAPAttachmentReference(type);
					ele.setId(id);
					ele.setResourceId(byId.get(id));
					this.addAttachment(type, ele);
				}
			}
		}
	}
	
	public boolean isEmpty() {     return this.m_element.isEmpty();   }
	
	public Map<String, HAPAttachment> getAttachmentByType(String type){
		Map<String, HAPAttachment> out = this.m_element.get(type);
		if(out==null)   out = new LinkedHashMap<String, HAPAttachment>();
		return out;
	}
	
	public void addAttachment(String type, HAPAttachment attachment) {
		Map<String, HAPAttachment> byId = this.m_element.get(type);
		if(byId==null) {
			byId = new LinkedHashMap<String, HAPAttachment>();
			this.m_element.put(type, byId);
		}
		byId.put(attachment.getId(), attachment);
	}

	//merge with parent
	public void merge(HAPAttachmentContainer parent, String mode) {
		if(parent==null)  return;
		if(mode==null)   mode = HAPConfigureContextProcessor.VALUE_INHERITMODE_CHILD;
		if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE))  return;
		
		for(String type : parent.m_element.keySet()) {
			Map<String, HAPAttachment> byId = parent.m_element.get(type);
			for(String id : byId.keySet()) {
				HAPAttachment parentEle = byId.get(id);
				HAPAttachment thisEle = this.getElement(type, id);
				if(thisEle==null || thisEle.getType().equals(HAPConstant.ATTACHMENT_TYPE_PLACEHOLDER)) {
					//element not exist, or empty, borrow from parent
					HAPAttachment newEle = parentEle.cloneAttachment();
					HAPAttachmentUtility.setOverridenByParent(newEle);
					this.addAttachment(type, newEle);
				}
				else {
					if(mode.equals(HAPConfigureContextProcessor.VALUE_INHERITMODE_PARENT)&&HAPAttachmentUtility.isOverridenByParentMode(thisEle)) {
						//if configurable, then parent override child
						HAPAttachment newEle = parentEle.cloneAttachment();
						this.addAttachment(type, newEle);
					}
				}
			}
		}
	}

	public HAPResourceIdSupplement toResourceIdSupplement(){
		Map<String, Map<String, HAPResourceId>> resourceIds = new LinkedHashMap<String, Map<String, HAPResourceId>>();
		for(String type : this.m_element.keySet()) {
			Map<String, HAPAttachment> byId = this.m_element.get(type);
			Map<String, HAPResourceId> byIdOut = new LinkedHashMap<>();
			for(String id : byId.keySet()) {
				HAPAttachment attachment = byId.get(id);
				if(attachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_REFERENCE)) {
					byIdOut.put(id, ((HAPAttachmentReference)attachment).getReferenceId());
				}
			}
			resourceIds.put(type, byIdOut);
		}
		return HAPResourceIdSupplement.newInstance(resourceIds);
	}
	
	public HAPAttachment getElement(String type, String id) {
		HAPAttachment out = null;
		Map<String, HAPAttachment> byId = this.m_element.get(type);
		if(byId!=null) {
			out = byId.get(id);
		}
		return out;
	}
	
	public HAPAttachmentContainer cloneAttachmentContainer() {
		HAPAttachmentContainer out = new HAPAttachmentContainer();
		for(String type : this.m_element.keySet()) {
			Map<String, HAPAttachment> byId = this.m_element.get(type);
			for(String id : byId.keySet()) {
				out.addAttachment(type, byId.get(id));
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_element, HAPSerializationFormat.JSON));
	}

}