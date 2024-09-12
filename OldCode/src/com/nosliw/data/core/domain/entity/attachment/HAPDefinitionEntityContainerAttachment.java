package com.nosliw.data.core.domain.entity.attachment;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickSimple;
import com.nosliw.core.application.division.manual.common.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.domain.attachment.HAPConfigureComplexRelationAttachment;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;

//store all attachment by type and by name
@HAPEntityWithAttribute
public class HAPDefinitionEntityContainerAttachment extends HAPManualBrickSimple{

	@HAPAttribute
	public static final String ELEMENT = "element";

	public HAPDefinitionEntityContainerAttachment() {
		this.setAttributeValueObject(ELEMENT, new LinkedHashMap<>());
	}

	public boolean isEmpty() {     return this.getAllAttachment().isEmpty();   }
	
	public Map<String, Map<String, HAPAttachment>> getAllAttachment(){   return (Map<String, Map<String, HAPAttachment>>)this.getAttributeValueOfValue(ELEMENT);   }
	
	public Map<String, HAPAttachment> getAttachmentByType(String type){
		Map<String, HAPAttachment> out = this.getAllAttachment().get(type);
		if(out==null)   out = new LinkedHashMap<String, HAPAttachment>();
		return out;
	}
	
	public HAPAttachment getAttachment(String valueType, String name) {    return this.getAllAttachment().get(valueType).get(name);     }

	public void addAttachment(String name, HAPAttachment attachment) {
		String valueType = attachment.getValueType();
		Map<String, HAPAttachment> byName = this.getAllAttachment().get(valueType);
		if(byName==null) {
			byName = new LinkedHashMap<String, HAPAttachment>();
			this.getAllAttachment().put(valueType, byName);
		}
		byName.put(name, attachment);
	}

	
	public void merge(HAPDefinitionEntityContainerAttachment parent, HAPConfigureComplexRelationAttachment mode) {
		this.merge(parent, mode==null?null:mode.getMode());
	}
	
	//merge with parent
	public void merge(HAPDefinitionEntityContainerAttachment parent, String mode) {
		if(parent==null)  return;
		if(mode==null)   mode = HAPConstant.INHERITMODE_CHILD;
		if(mode.equals(HAPConstant.INHERITMODE_NONE))  return;
		
		for(String type : parent.getAllAttachment().keySet()) {
			Map<String, HAPAttachment> byName = parent.getAllAttachment().get(type);
			for(String name : byName.keySet()) {
				HAPAttachment parentEle = byName.get(name);
				HAPAttachment thisEle = this.getElement(type, name);
				boolean override = false;
				if(mode.equals(HAPConstant.INHERITMODE_CHILD)&&thisEle==null) override = true;
				else if(mode.equals(HAPConstant.INHERITMODE_PARENT)) override = true;

				if(override) {
					//if configurable, then parent override child
					HAPAttachment newEle = parentEle.cloneAttachment();
					HAPUtilityAttachment.setOverridenByParent(newEle);
					this.addAttachment(name, newEle);
				}
			}
		}
	}
	public HAPAttachment getElement(HAPReferenceAttachment attachmentReference) {   return this.getElement(attachmentReference.getDataType(), attachmentReference.getName());  }
	
	public HAPAttachment getElement(String type, String name) {
		HAPAttachment out = null;
		Map<String, HAPAttachment> byName = this.getAllAttachment().get(type);
		if(byName!=null) {
			out = byName.get(name);
		}
		return out;
	}
	
	public HAPDefinitionEntityContainerAttachment cloneAttachmentContainer() {
		HAPDefinitionEntityContainerAttachment out = new HAPDefinitionEntityContainerAttachment();
		super.cloneToDefinitionEntityInDomain(out);
		for(String type : this.getAllAttachment().keySet()) {
			Map<String, HAPAttachment> byName = this.getAllAttachment().get(type);
			for(String name : byName.keySet()) {
				out.addAttachment(type, byName.get(name));
			}
		}
		return out;
	}
	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		return this.cloneAttachmentContainer();
	}
}
