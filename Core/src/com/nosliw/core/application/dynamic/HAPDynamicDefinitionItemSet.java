package com.nosliw.core.application.dynamic;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPDynamicDefinitionItemSet extends HAPDynamicDefinitionItemLeaf{

	@Override
	public String getType() {
		return HAPConstantShared.DYNAMICDEFINITION_ITEMTYPE_SET;
	}
}
