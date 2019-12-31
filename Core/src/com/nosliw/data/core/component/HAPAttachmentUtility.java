package com.nosliw.data.core.component;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPAttachmentUtility {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 

	
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
