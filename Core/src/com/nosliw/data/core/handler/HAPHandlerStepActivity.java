package com.nosliw.data.core.handler;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPHandlerStepActivity extends HAPSerializableImp implements HAPHandlerStep{

	@Override
	public String getHandlerStepType() {  return HAPConstantShared.HANDLERSTEP_TYPE_ACTIVITY;  }

	@Override
	public HAPHandlerStep cloneHandlerStep() {
		return new HAPHandlerStepActivity();
	}

}
