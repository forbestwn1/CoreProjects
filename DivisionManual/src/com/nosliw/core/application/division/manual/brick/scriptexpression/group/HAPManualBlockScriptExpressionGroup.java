package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import com.nosliw.core.application.common.scriptexpressio.HAPContainerScriptExpression;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.scriptexpression.group.HAPBlockScriptExpressionGroup;

public class HAPManualBlockScriptExpressionGroup extends HAPManualBrickImp implements HAPBlockScriptExpressionGroup{

	@Override
	public void init() {
		this.setValue(new HAPContainerScriptExpression());
	}
	
	@Override
	public HAPContainerScriptExpression getValue() {  return (HAPContainerScriptExpression)this.getAttributeValueOfValue(VALUE);  }   
	public void setValue(HAPContainerScriptExpression value) {    this.setAttributeValueWithValue(VALUE, value);     } 
	
}
