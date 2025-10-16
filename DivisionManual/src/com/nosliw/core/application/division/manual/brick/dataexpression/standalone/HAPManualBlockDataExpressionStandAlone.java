package com.nosliw.core.application.division.manual.brick.dataexpression.standalone;

import com.nosliw.core.application.brick.dataexpression.standalone.HAPBlockDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockDataExpressionStandAlone extends HAPManualBrickImp implements HAPBlockDataExpressionStandAlone{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPDataExpressionStandAlone());;
	}
	
	@Override
	public HAPDataExpressionStandAlone getValue(){	return (HAPDataExpressionStandAlone)this.getAttributeValueOfValue(VALUE);	}
	
}
