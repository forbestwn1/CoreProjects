package com.nosliw.data.core.component;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.domain.attachment.HAPReferenceAttachment;

public class HAPEmbededAttachmentReference extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPDefinitionWrapperTask<HAPReferenceAttachment> m_embeded;

	
	public HAPReferenceAttachment getAttachmentReference() {	return this.m_embeded.getTaskDefinition();	}
	
	public HAPDefinitionWrapperTask<HAPReferenceAttachment> getEmbeded(){   return this.m_embeded;  }
	
	public void buildEmbededAttachmentReferenceByJson(JSONObject jsonObj, String type) {
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_embeded = new HAPDefinitionWrapperTask<HAPReferenceAttachment>();
		this.m_embeded.buildObj(jsonObj, HAPReferenceAttachment.newInstance(jsonObj.getJSONObject(REFERENCE), type));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEmbededAttachmentReferenceByJson(jsonObj, null);
		return true;  
	}

}
