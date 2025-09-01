package com.nosliw.core.application.dynamic;

import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

public class HAPUtilityInfoDynamic {

	public static HAPInteractiveTask getTaskInterfaceForDynamic(HAPValueOfDynamic dynamicValue, HAPContainerInfoDynamic dynamicInfo) {
		String interfaceId = dynamicValue.getInterfaceId();
		HAPInfoDynamicLeaf dynamicEle = (HAPInfoDynamicLeaf)dynamicInfo.getDescent(interfaceId);
		return dynamicEle.getCriteria();
	}
	
}
