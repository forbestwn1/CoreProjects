package com.nosliw.core.application.brick.scriptexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;

@HAPEntityWithAttribute
public class HAPBlockScriptExpressionElementInLibrary extends HAPManualBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String VALUE = "value";
	
	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPElementInLibraryScriptExpression());;
	}
	
	public HAPElementInLibraryScriptExpression getValue(){	return (HAPElementInLibraryScriptExpression)this.getAttributeValueOfValue(VALUE);	}
	
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
