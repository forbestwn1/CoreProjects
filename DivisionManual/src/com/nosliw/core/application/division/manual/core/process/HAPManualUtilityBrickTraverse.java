package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPHandlerBrickWrapper;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWithBrick;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.division.manual.core.HAPManualUtilityBrick;

public class HAPManualUtilityBrickTraverse {

	//traverse only leaves that is local complex entity
	public static void traverseTree(HAPManualContextProcessBrick processContext, HAPHandlerDownward processor, Object data) {
		HAPUtilityBrickTraverse.traverseTree(processContext.getCurrentBundle(), processContext.getRootBrickName(), processor, processContext.getBrickManager(), data);
	}

	public static void traverseTreeWithLocalBrick(HAPManualContextProcessBrick processContext, HAPHandlerDownward processor, Object data) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(processContext.getCurrentBundle(), processContext.getRootBrickName(), processor, processContext.getBrickManager(), data);
	}

	public static void traverseTreeWithLocalBrickComplex(HAPManualContextProcessBrick processContext, HAPHandlerDownward processor, Object data) {
		traverseTreeWithLocalBrick(
				processContext, 
			new HAPHandlerBrickWrapper(processor, true) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPWrapperValue attrValueInfo = attr.getValueWrapper();
					return HAPManualUtilityBrick.isBrickComplex(((HAPWithBrick)attrValueInfo).getBrick().getBrickType(), processContext.getManualBrickManager());
				}
			}, 
			data);
	}
	
}
