package com.nosliw.core.application.brick.dataexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.dataexpression.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionElementInLibrary extends HAPBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String VALUE = "value";
	
	@Override
	public void init() {
		this.setAttributeValueWithValue(VALUE, new HAPElementInLibraryDataExpression());;
	}
	
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
