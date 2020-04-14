package com.nosliw.data.core.component.attachment;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;

//
@HAPEntityWithAttribute
public class HAPAttachmentReference extends HAPAttachmentImp{

	@HAPAttribute
	public static String REFERENCEID = "referenceId";
	
	private HAPResourceId m_referenceId;

	private HAPAttachmentReference() {	}
	
	public HAPAttachmentReference(String resourceType) {
		this.setResourceType(resourceType);
	}

	public HAPAttachmentReference(HAPResourceId resourceId) {
		this.m_referenceId = resourceId;
	}

	@Override
	public String getType() {  return HAPConstant.ATTACHMENT_TYPE_REFERENCE;  }
	
	public HAPResourceId getReferenceId() {	return this.m_referenceId;  }
	public void setId(HAPResourceId referenceId) {    
		this.m_referenceId = referenceId;
		this.setResourceType(this.m_referenceId.getType());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_referenceId!=null)		jsonMap.put(ID, this.m_referenceId.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		Object referenceId = jsonObj.opt(REFERENCEID);
		if(referenceId!=null) {
			this.m_referenceId = HAPResourceIdFactory.newInstance(this.getResourceType(), referenceId);
		}
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentReference) {
			HAPAttachmentReference ele = (HAPAttachmentReference)obj;
			if(super.equals(ele)) {
				if(HAPBasicUtility.isEquals(ele.m_referenceId, this.m_referenceId)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentReference cloneAttachment() {
		HAPAttachmentReference out = new HAPAttachmentReference();
		super.cloneToObject(out);
		out.m_referenceId = this.m_referenceId.clone();
		return out;
	}
}
