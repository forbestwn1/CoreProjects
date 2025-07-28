package com.nosliw.data.core.handler;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPHandlerStepDataAssociation extends HAPSerializableImp implements HAPHandlerStep{

	@Override
	public String getHandlerStepType() {   return HAPConstantShared.HANDLERSTEP_TYPE_DATAASSOCIATION; }

	@Override
	public HAPHandlerStep cloneHandlerStep() {
		return new HAPHandlerStepDataAssociation();
	}
}
