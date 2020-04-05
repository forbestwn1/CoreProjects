package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPResourceDefinitionExpression  extends HAPComponentContainerElement implements HAPDefinitionExpression{

	public HAPResourceDefinitionExpression() {}
	
	public HAPResourceDefinitionExpression(HAPResourceDefinitionExpressionSuite suite, String name) {
		super(suite, name);
	}

	public HAPDefinitionExpressionSuiteElementEntity getExpressionBody() {    return (HAPDefinitionExpressionSuiteElementEntity)this.getElement();   }
	public HAPResourceDefinitionExpressionSuite getSuite() {   return (HAPResourceDefinitionExpressionSuite)this.getContainer();  }

	@Override
	public Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> getExpressionElements(){   return this.getExpressionBody().getExpressions();   }
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPResourceDefinitionExpression out = new HAPResourceDefinitionExpression();
		this.cloneToComponentContainerElement(out);
		return out;
	}
}
