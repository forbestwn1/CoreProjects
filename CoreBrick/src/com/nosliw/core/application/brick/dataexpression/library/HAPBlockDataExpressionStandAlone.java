package com.nosliw.core.application.brick.dataexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;

@HAPEntityWithAttribute
public interface HAPBlockDataExpressionStandAlone extends HAPBrick, HAPEntityInfo{

	@HAPAttribute
	public static String VALUE = "value";
	
	HAPDataExpressionStandAlone getValue();
	
}
