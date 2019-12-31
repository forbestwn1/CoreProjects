package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstant;

public class HAPAttachmentPlaceHolder extends HAPEntityInfoWritableImp implements HAPAttachment{

	@Override
	public String getType() {
		return HAPConstant.ATTACHMENT_TYPE_PLACEHOLDER;
	}

	@Override
	public HAPAttachmentPlaceHolder clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
