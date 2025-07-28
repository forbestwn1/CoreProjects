package com.nosliw.core.application.common.dynamic;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPInfoDynamicTaskElementLeafSet extends HAPInfoDynamicTaskElementLeaf{

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
