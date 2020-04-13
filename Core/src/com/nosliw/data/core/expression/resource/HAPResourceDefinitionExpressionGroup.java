package com.nosliw.data.core.expression.resource;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;

public class HAPResourceDefinitionExpressionGroup  extends HAPComponentContainerElement implements HAPDefinitionExpressionGroup{

	public HAPResourceDefinitionExpressionGroup() {}
	
	public HAPResourceDefinitionExpressionGroup(HAPResourceDefinitionExpressionSuite suite, String name) {
		super(suite, name);
	}

	public HAPDefinitionResourceDefinitionExpressionSuiteElementEntity getExpressionBody() {    return (HAPDefinitionResourceDefinitionExpressionSuiteElementEntity)this.getEntityElement();   }
	public HAPResourceDefinitionExpressionSuite getSuite() {   return (HAPResourceDefinitionExpressionSuite)this.getContainer();  }

	@Override
	public Map<String, HAPDefinitionExpression> getExpressions(){   return this.getExpressionBody().getExpressions();   }
	
	@Override
	public void addExpression(HAPDefinitionExpression expression) {   this.getExpressionBody().addExpression(expression); }

	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_EXPRESSION;  }

	@Override
	public HAPComponent cloneComponent() {
		HAPResourceDefinitionExpressionGroup out = new HAPResourceDefinitionExpressionGroup();
		this.cloneToComponentContainerElement(out);
		return out;
	}
}
