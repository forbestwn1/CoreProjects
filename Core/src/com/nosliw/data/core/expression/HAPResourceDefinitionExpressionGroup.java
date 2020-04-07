package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;

public class HAPResourceDefinitionExpressionGroup  extends HAPComponentContainerElement implements HAPDefinitionExpressionGroup{

	public HAPResourceDefinitionExpressionGroup() {}
	
	public HAPResourceDefinitionExpressionGroup(HAPResourceDefinitionExpressionSuite suite, String name) {
		super(suite, name);
	}

	public HAPDefinitionResourceDefinitionExpressionSuiteElementEntity getExpressionBody() {    return (HAPDefinitionResourceDefinitionExpressionSuiteElementEntity)this.getElement();   }
	public HAPResourceDefinitionExpressionSuite getSuite() {   return (HAPResourceDefinitionExpressionSuite)this.getContainer();  }

	@Override
	public Map<String, HAPDefinitionExpression> getExpressions(){   return this.getExpressionBody().getExpressions();   }
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPResourceDefinitionExpressionGroup out = new HAPResourceDefinitionExpressionGroup();
		this.cloneToComponentContainerElement(out);
		return out;
	}
}
