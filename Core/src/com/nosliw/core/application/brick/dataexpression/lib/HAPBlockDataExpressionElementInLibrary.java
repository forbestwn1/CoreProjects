package com.nosliw.core.application.brick.dataexpression.lib;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionElementInLibrary;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionElementInLibrary extends HAPBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String VALUE = "value";
	
	@Override
	public void init() {
		this.setAttributeValueWithValue(VALUE, new HAPDataExpressionElementInLibrary());;
	}
	
	public HAPDataExpressionElementInLibrary getValue(){	return (HAPDataExpressionElementInLibrary)this.getAttributeValueOfValue(VALUE);	}
	
	@Override
	public HAPContainerValuePorts getExternalValuePorts() {
		return this.getValue().getExternalValuePorts();
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {
		return this.getValue().getInternalValuePorts();
	}

}
