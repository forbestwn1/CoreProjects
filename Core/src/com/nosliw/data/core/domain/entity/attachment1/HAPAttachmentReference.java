package com.nosliw.data.core.domain.entity.attachment1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentImp;
import com.nosliw.data.core.resource.HAPFactoryResourceId;

//
@HAPEntityWithAttribute
public abstract class HAPAttachmentReference extends HAPAttachmentImp{

	@HAPAttribute
	public static String REFERENCEID = "referenceId";
	
	private HAPResourceId m_referenceId;

	public HAPAttachmentReference() {	}
	
	public HAPAttachmentReference(String resourceType) {
		super(resourceType);
	}
	
	public HAPAttachmentReference(HAPResourceId resourceId) {
		super(resourceId.getResourceType());
		this.m_referenceId = resourceId;
	}

	public HAPResourceId getReferenceId() {	return this.m_referenceId;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_referenceId!=null)		jsonMap.put(REFERENCEID, this.m_referenceId.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		Object referenceId = jsonObj.opt(REFERENCEID);
		if(referenceId!=null) {
			this.m_referenceId = HAPFactoryResourceId.newInstance(this.getValueType(), referenceId);
		}
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPAttachmentReference) {
			HAPAttachmentReference ele = (HAPAttachmentReference)obj;
			if(super.equals(ele)) {
				if(HAPUtilityBasic.isEquals(ele.m_referenceId, this.m_referenceId)) {
					out = true;
				}
			}
		}
		return out;
	}
	
	protected void cloneToReferenceAttachment(HAPAttachmentReference out) {
		super.cloneToAttachment(out);
		out.m_referenceId = this.m_referenceId.clone();
	}
}
