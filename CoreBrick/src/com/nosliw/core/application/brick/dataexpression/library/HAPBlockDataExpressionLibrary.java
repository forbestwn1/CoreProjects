package com.nosliw.core.application.brick.dataexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

@HAPEntityWithAttribute
public interface HAPBlockDataExpressionLibrary extends HAPBrick{

	@HAPAttribute
	public static String ITEM = "item";

	HAPBrickContainer getItems();

}
