package com.nosliw.core.application.brick.dataexpression.standalone;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionStandAlone;

public class HAPBlockDataExpressionStandAloneImp extends HAPBrickImp implements HAPBlockDataExpressionStandAlone{

	public HAPBlockDataExpressionStandAloneImp() {
		super(HAPEnumBrickType.DATAEXPRESSIONSTANDALONE_100);
		this.setAttributeValueWithValue(VALUE, new HAPDataExpressionStandAlone());;
	}
	
	@Override
	public HAPDataExpressionStandAlone getValue(){	return (HAPDataExpressionStandAlone)this.getAttributeValueOfValue(VALUE);	}
	
}
