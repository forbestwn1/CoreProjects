package com.nosliw.data.core.component.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;

//store all attachment by type and by name
@HAPEntityWithAttribute
public class HAPContainerAttachment extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	//all attachment by type and by name
	private Map<String, Map<String, HAPAttachment>> m_element;

	public HAPContainerAttachment() {
		this.m_element = new LinkedHashMap<>();
	}

	public boolean isEmpty() {     return this.m_element.isEmpty();   }
	
	public Map<String, Map<String, HAPAttachment>> getAllAttachment(){   return this.m_element;    }
	
	public Map<String, HAPAttachment> getAttachmentByType(String type){
		Map<String, HAPAttachment> out = this.m_element.get(type);
		if(out==null)   out = new LinkedHashMap<String, HAPAttachment>();
		return out;
	}
	
	public void addAttachment(String type, HAPAttachment attachment) {
		attachment.setValueType(type);
		Map<String, HAPAttachment> byName = this.m_element.get(type);
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPAttachment>();
			this.m_element.put(type, byName);
		}
		byName.put(attachment.getName(), attachment);
	}

	//merge with parent
	public void merge(HAPContainerAttachment parent, String mode) {
		if(parent==null)  return;
		if(mode==null)   mode = HAPConstant.INHERITMODE_CHILD;
		if(mode.equals(HAPConstant.INHERITMODE_NONE))  return;
		
		for(String type : parent.m_element.keySet()) {
			Map<String, HAPAttachment> byName = parent.m_element.get(type);
			for(String name : byName.keySet()) {
				HAPAttachment parentEle = byName.get(name);
				HAPAttachment thisEle = this.getElement(type, name);
				if(thisEle==null || thisEle.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_PLACEHOLDER)) {
					//element not exist, or empty, borrow from parent
					HAPAttachment newEle = parentEle.cloneAttachment();
					HAPUtilityAttachment.setOverridenByParent(newEle);
					this.addAttachment(type, newEle);
				}
				else {
					if(mode.equals(HAPConstant.INHERITMODE_PARENT)&&HAPUtilityAttachment.isOverridenByParentMode(thisEle)) {
						//if configurable, then parent override child
						HAPAttachment newEle = parentEle.cloneAttachment();
						HAPUtilityAttachment.setOverridenByParent(newEle);
						this.addAttachment(type, newEle);
					}
				}
			}
		}
	}

	public HAPAttachment getElement(HAPReferenceAttachment attachmentReference) {   return this.getElement(attachmentReference.getType(), attachmentReference.getName());  }
	
	public HAPAttachment getElement(String type, String name) {
		HAPAttachment out = null;
		Map<String, HAPAttachment> byName = this.m_element.get(type);
		if(byName!=null) {
			out = byName.get(name);
		}
		return out;
	}
	
	public HAPContainerAttachment cloneAttachmentContainer() {
		HAPContainerAttachment out = new HAPContainerAttachment();
		for(String type : this.m_element.keySet()) {
			Map<String, HAPAttachment> byName = this.m_element.get(type);
			for(String name : byName.keySet()) {
				out.addAttachment(type, byName.get(name));
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
