package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import com.nosliw.core.application.brick.scriptexpression.group.HAPBlockScriptExpressionGroup;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockScriptExpressionGroup extends HAPManualBrickImp implements HAPBlockScriptExpressionGroup{

	@Override
	public void init() {
		this.setValue(new HAPContainerScriptExpression());
	}
	
	@Override
	public HAPContainerScriptExpression getValue() {  return (HAPContainerScriptExpression)this.getAttributeValueOfValue(VALUE);  }   
	public void setValue(HAPContainerScriptExpression value) {    this.setAttributeValueWithValue(VALUE, value);     } 
	
}