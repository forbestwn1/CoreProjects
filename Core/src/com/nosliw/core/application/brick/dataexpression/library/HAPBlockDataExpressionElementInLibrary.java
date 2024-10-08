package com.nosliw.core.application.brick.dataexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockDataExpressionElementInLibrary extends HAPBrick, HAPEntityInfo{

	@HAPAttribute
	public static String VALUE = "value";
	
	HAPElementInLibraryDataExpression getValue();
	
}
