package com.nosliw.core.application.division.manual.common.scriptexpression;

import com.nosliw.common.container.HAPContainer;

public class HAPManualDefinitionContainerScriptExpression extends HAPContainer<HAPManualDefinitionScriptExpressionItemInContainer>{

	public String addScriptExpression(HAPManualDefinitionScriptExpression scriptExpression) {
		return this.addItem(new HAPManualDefinitionScriptExpressionItemInContainer(scriptExpression));
	}
	
	public String addScriptExpression(String scriptExpression) {
		return this.addItem(new HAPManualDefinitionScriptExpressionItemInContainer(new HAPManualDefinitionScriptExpression(scriptExpression)));
	}
}
