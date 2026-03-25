package com.nosliw.core.application.division.manual.brick.task.wrapper.dataexpression;

import com.nosliw.core.application.brick.task.wrapper.dataexpression.HAPBlockTaskWrapperDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockDataTaskWrapperDataExpression extends HAPManualBrickImp implements HAPBlockTaskWrapperDataExpression{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(DATAEXPRESSION, new HAPDataExpressionStandAlone());;
	}
	
	@Override
	public HAPDataExpressionStandAlone getDataExpression(){	return (HAPDataExpressionStandAlone)this.getAttributeValueOfValue(DATAEXPRESSION);	}
	
}
