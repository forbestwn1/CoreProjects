package com.nosliw.data.core.component;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstant;

public class HAPAttachmentEntity extends HAPEntityInfoWritableImp implements HAPAttachment{

	@Override
	public String getType() {
		return HAPConstant.ATTACHMENT_TYPE_ENTITY;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public HAPAttachmentEntity clone() {
		return null;
	}
}
