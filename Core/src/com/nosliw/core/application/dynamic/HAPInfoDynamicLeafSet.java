package com.nosliw.core.application.dynamic;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPInfoDynamicLeafSet extends HAPInfoDynamicLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICTASK_INFO_TYPE_SET;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		return true;
	}
}
