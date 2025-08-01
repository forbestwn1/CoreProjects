package com.nosliw.core.application.dynamic;

import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

public class HAPUtilityInfoDynamic {

	public static HAPInteractiveTask getTaskInterfaceForDynamic(HAPValueOfDynamic dynamicValue, HAPInfoDynamicTask dynamicInfo) {
		String interfaceId = dynamicValue.getInterfaceId();
		HAPInfoDynamicTaskElementLeaf dynamicEle = (HAPInfoDynamicTaskElementLeaf)dynamicInfo.getDescent(interfaceId);
		return dynamicEle.getTaskInterface();
	}
	
}
