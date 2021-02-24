package com.nosliw.data.core.component.attachment;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPAttachmentPlaceHolder extends HAPAttachmentImp{

	public HAPAttachmentPlaceHolder(String resourceType) {
		super(resourceType);
	}
	
	@Override
	public String getType() {
		return HAPConstantShared.ATTACHMENT_TYPE_PLACEHOLDER;
	}

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
		boolean out = false;
		if(obj instanceof HAPAttachmentPlaceHolder) {
			HAPAttachmentPlaceHolder ele = (HAPAttachmentPlaceHolder)obj;
			if(super.equals(ele)) {
				out = true;
			}
		}
		return out;
	}
	
	@Override
	public HAPAttachmentPlaceHolder cloneAttachment() {
		HAPAttachmentPlaceHolder out = new HAPAttachmentPlaceHolder(this.getValueType());
		return out;
	}	
}
