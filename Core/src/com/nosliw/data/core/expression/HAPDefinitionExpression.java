package com.nosliw.data.core.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPDefinitionExpression  extends HAPComponentContainerElement{

	public HAPDefinitionExpression() {}
	
	public HAPDefinitionExpression(HAPDefinitionExpressionSuite suite, String name) {
		super(suite, name);
	}

	public HAPDefinitionExpressionSuiteElementEntity getExpression() {    return (HAPDefinitionExpressionSuiteElementEntity)this.getElement();   }
	public HAPDefinitionExpressionSuite getSuite() {   return (HAPDefinitionExpressionSuite)this.getContainer();  }

	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPDefinitionExpression out = new HAPDefinitionExpression();
		this.cloneToComponentContainerElement(out);
		return out;
	}
}
