package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.dataexpression.group.HAPBlockDataExpressionGroup;

public class HAPManualBlockDataExpressionGroup extends HAPManualBrickImp implements HAPBlockDataExpressionGroup{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPContainerDataExpression());
	}
	
	@Override
	public HAPContainerDataExpression getValue(){	return (HAPContainerDataExpression)this.getAttributeValueOfValue(VALUE);	}


}
