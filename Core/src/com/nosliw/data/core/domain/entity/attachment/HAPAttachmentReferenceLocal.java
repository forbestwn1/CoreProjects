package com.nosliw.data.core.domain.entity.attachment;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;

//
@HAPEntityWithAttribute
public class HAPAttachmentReferenceLocal extends HAPAttachmentReference{

	public HAPAttachmentReferenceLocal() {	}

	public HAPAttachmentReferenceLocal(String resourceType) {
		super(resourceType);
	}

	public HAPAttachmentReferenceLocal(HAPResourceId resourceId) {
		super(resourceId);
	}

	@Override
	public String getType() {  return HAPConstantShared.ATTACHMENT_TYPE_REFERENCELOCAL;  }
	
	public HAPResourceIdLocal getLocalReferenceId() {    return (HAPResourceIdLocal)this.getReferenceId();   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		return true;  
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public HAPAttachmentReferenceLocal cloneAttachment() {
		HAPAttachmentReferenceLocal out = new HAPAttachmentReferenceLocal();
		super.cloneToReferenceAttachment(out);
		return out;
	}
}
