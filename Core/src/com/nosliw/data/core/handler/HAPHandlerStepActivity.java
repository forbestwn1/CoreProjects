package com.nosliw.data.core.handler;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public class HAPHandlerStepActivity extends HAPSerializableImp implements HAPHandlerStep{

	@Override
	public String getHandlerStepType() {  return HAPConstant.HANDLERSTEP_TYPE_ACTIVITY;  }

	@Override
	public HAPHandlerStep cloneHandlerStep() {
		return new HAPHandlerStepActivity();
	}

}
