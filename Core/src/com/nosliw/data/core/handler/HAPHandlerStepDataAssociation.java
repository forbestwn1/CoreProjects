package com.nosliw.data.core.handler;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPHandlerStepDataAssociation extends HAPSerializableImp implements HAPHandlerStep{

	@Override
	public String getHandlerStepType() {   return HAPConstant.HANDLERSTEP_TYPE_DATAASSOCIATION; }

	@Override
	public HAPHandlerStep cloneHandlerStep() {
		return new HAPHandlerStepDataAssociation();
	}
}
