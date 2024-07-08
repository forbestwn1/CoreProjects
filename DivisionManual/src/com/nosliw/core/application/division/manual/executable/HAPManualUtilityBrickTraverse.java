package com.nosliw.core.application.division.manual.executable;

import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPHandlerBrickWrapper;
import com.nosliw.core.application.HAPHandlerDownward;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrickTraverse;
import com.nosliw.core.application.HAPWithBrick;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;

public class HAPManualUtilityBrickTraverse {

	//traverse only leaves that is local complex entity
	public static void traverseTreeWithLocalBrickComplex(HAPWrapperBrickRoot rootBrickWrapper, HAPHandlerDownward processor, HAPManagerApplicationBrick brickMan, HAPManualManagerBrick manualBrickMan, Object data) {
		HAPUtilityBrickTraverse.traverseTreeWithLocalBrick(
				rootBrickWrapper, 
			new HAPHandlerBrickWrapper(processor, true) {
				@Override
				protected boolean isValidAttribute(HAPAttributeInBrick attr) {
					HAPWrapperValue attrValueInfo = attr.getValueWrapper();
					return HAPManualUtilityBrick.isBrickComplex(((HAPWithBrick)attrValueInfo).getBrick().getBrickType(), manualBrickMan);
				}
			}, 
			brickMan,
			data);
	}
	
}
