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
import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.resource.HAPResourceIdLocal;

//store all attachment by type and by name
@HAPEntityWithAttribute
public class HAPContainerAttachment extends HAPSerializableImp{

	@HAPAttribute
	public static final String ELEMENT = "element";

	@HAPAttribute
	public static final String LOCALREFBASE = "localRefBase";

	//all attachment by type and by name
	private Map<String, Map<String, HAPAttachment>> m_element;

	//dir path that is used as base for local resource id reference attachment within this container
	private HAPLocalReferenceBase m_localReferenceBase;
	
	public HAPContainerAttachment() {
		this.m_element = new LinkedHashMap<>();
	}

	public HAPLocalReferenceBase getLocalReferenceBase() {    return this.m_localReferenceBase;    }
	
	public void setLocalReferenceBase(HAPLocalReferenceBase localRefBase) {   
		this.m_localReferenceBase = localRefBase;
		if(this.m_localReferenceBase!=null) {
			for(Map<String, HAPAttachment> subEles : this.m_element.values()) {
				for(HAPAttachment ele : subEles.values()) {
					this.setLocalReferenceBase(ele, this.m_localReferenceBase);
				}
			}
		}
	}

	private void setLocalReferenceBase(HAPAttachment att, HAPLocalReferenceBase localRefBase) {
		if(att.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL)){
			HAPAttachmentReferenceLocal refAttr = (HAPAttachmentReferenceLocal)att;
			HAPResourceIdLocal localResourceId = refAttr.getLocalReferenceId();
			if(localResourceId.getBasePath()==null) {
				localResourceId.setBasePath(localRefBase);
			}
		}
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
		if(this.m_localReferenceBase!=null)   this.setLocalReferenceBase(attachment, this.m_localReferenceBase);
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
		if(this.m_localReferenceBase!=null)   out.m_localReferenceBase = this.m_localReferenceBase.cloneLocalReferenceBase();
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, HAPJsonUtility.buildJson(this.m_element, HAPSerializationFormat.JSON));
		jsonMap.put(LOCALREFBASE, HAPJsonUtility.buildJson(this.m_localReferenceBase, HAPSerializationFormat.LITERATE));
	}

}
