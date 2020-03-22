package com.nosliw.data.core.component.attachment;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.data.HAPContextDataFactory;
import com.nosliw.data.core.script.context.data.HAPContextDataFlat;

public class HAPAttachmentUtility {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 

	public static HAPContextDataFlat getContextDataFromAttachment(HAPAttachmentContainer attContainer, String type, String name) {
		HAPContextDataFlat out = new HAPContextDataFlat();
		HAPAttachmentEntity attachment = (HAPAttachmentEntity)attContainer.getElement(type, name);
		if(attachment!=null) {
			JSONObject dataObjJson = attachment.getEntity();
			out = HAPContextDataFactory.newContextDataFlat(dataObjJson);
		}
		return out;
	}
	
	public static HAPResourceDefinition getResourceDefinition(HAPAttachmentContainer attContainer, String type, String name, HAPManagerResourceDefinition resourceDefMan) {
		HAPResourceDefinition out = null;
		HAPAttachment attachment = attContainer.getElement(type, name);
		if(attachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_REFERENCE)) {
			HAPResourceId resourceId = ((HAPAttachmentReference)attachment).getId();
			out = resourceDefMan.getResourceDefinition(resourceId);
		}
		else if(attachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_ENTITY)){
			out = resourceDefMan.parseResourceDefinition(type, ((HAPAttachmentEntity)attachment).getEntity());
		}
		return out;
	}
	
	public static void parseDefinition(JSONObject attachmentDefJson, HAPAttachmentContainer attachmentContainer) {
		for(Object key : attachmentDefJson.keySet()) {
			String type = (String)key;
			JSONArray byNameArray = attachmentDefJson.getJSONArray(type);
			for(int i=0; i<byNameArray.length(); i++) {
				JSONObject attachmentJson = byNameArray.getJSONObject(i);
				HAPAttachment attachment = null;
				if(attachmentJson.opt(HAPAttachmentReference.ID)!=null) {
					attachment = new HAPAttachmentReference(type);
					attachment.buildObject(attachmentJson, HAPSerializationFormat.JSON);
				}
				else if(attachmentJson.opt(HAPAttachmentEntity.ENTITY)!=null){
					attachment = new HAPAttachmentEntity(type);
					attachment.buildObject(attachmentJson, HAPSerializationFormat.JSON);
				}
				else {
					attachment = new HAPAttachmentPlaceHolder(type);
					attachment.buildObject(attachmentJson, HAPSerializationFormat.JSON);
				}
				attachmentContainer.addAttachment(type, attachment);
			}
		}
	}

	public static void setOverridenByParent(HAPAttachment ele) {
		ele.getInfo().setValue(ATTRIBUTE_FLAG_OVERRIDE, Boolean.TRUE);
	}
	
	public static boolean isOverridenByParent(HAPAttachment ele) {
		return Boolean.TRUE.equals(ele.getInfo().getValue(ATTRIBUTE_FLAG_OVERRIDE));
	}
	
	public static boolean isOverridenByParentMode(HAPAttachment ele) {
		String mode = (String)ele.getInfo().getValue(ATTRIBUTE_OVERRIDE_MODE);
		if(mode==null)   mode =  OVERRIDE_MODE_NONE;
		return OVERRIDE_MODE_CONFIGURABLE.equals(mode);
	}
	
}
