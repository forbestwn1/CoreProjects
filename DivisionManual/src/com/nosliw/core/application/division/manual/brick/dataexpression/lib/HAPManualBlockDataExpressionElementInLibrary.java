package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.division.manual.core.b.HAPManualBrickWithEntityInfo;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPElementInLibraryDataExpression;

public class HAPManualBlockDataExpressionElementInLibrary extends HAPManualBrickWithEntityInfo implements HAPBlockDataExpressionElementInLibrary{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPElementInLibraryDataExpression());;
	}
	
	@Override
	public HAPElementInLibraryDataExpression getValue(){	return (HAPElementInLibraryDataExpression)this.getAttributeValueOfValue(VALUE);	}
	
}
