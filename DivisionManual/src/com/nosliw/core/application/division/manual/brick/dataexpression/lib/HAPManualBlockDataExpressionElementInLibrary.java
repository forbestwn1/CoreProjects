package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickWithEntityInfoSimple;

public class HAPManualBlockDataExpressionElementInLibrary extends HAPManualBrickWithEntityInfoSimple implements HAPBlockDataExpressionElementInLibrary{

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPElementInLibraryDataExpression());;
	}
	
	@Override
	public HAPElementInLibraryDataExpression getValue(){	return (HAPElementInLibraryDataExpression)this.getAttributeValueOfValue(VALUE);	}
	
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(this.getValue().getExternalValuePortGroup(), true);
		return out;
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		HAPContainerValuePorts out = new HAPContainerValuePorts();
		out.addValuePortGroup(this.getValue().getInternalValuePortGroup(), true);
		return out;
	}
}
