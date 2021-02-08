package com.nosliw.data.core.expression.resource;

import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentContainerElement;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;

public class HAPResourceDefinitionExpressionGroup  extends HAPComponentContainerElement implements HAPDefinitionExpressionGroup{

	public HAPResourceDefinitionExpressionGroup() {}
	
	public HAPResourceDefinitionExpressionGroup(HAPResourceDefinitionExpressionSuite suite, String id) {
		super(suite, id);
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_EXPRESSION;  }

	public HAPDefinitionResourceDefinitionExpressionSuiteElementEntity getExpressionGroupEntity() {    return (HAPDefinitionResourceDefinitionExpressionSuiteElementEntity)this.getComponentEntity();   }
	public HAPResourceDefinitionExpressionSuite getSuite() {   return (HAPResourceDefinitionExpressionSuite)this.getResourceContainer();  }

	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {		return this.getExpressionGroupEntity().getEntityElements();	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.getExpressionGroupEntity().getEntityElement(id);  }

	@Override
	public HAPComponent cloneComponent() {
		HAPResourceDefinitionExpressionGroup out = new HAPResourceDefinitionExpressionGroup();
		this.cloneToComponentContainerElement(out);
		return out;
	}

	@Override
	public HAPDefinitionExpressionGroup cloneExpressionGroupDefinition() { return (HAPDefinitionExpressionGroup)cloneComponent();  }

	@Override
	public void addEntityElement(HAPDefinitionExpression entityElement) {
		// TODO Auto-generated method stub
	}

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionConstant getConstantDefinition(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {
		// TODO Auto-generated method stub
	}

}
