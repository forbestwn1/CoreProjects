package com.nosliw.data.core.domain.entity.expression.resource;

import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPDefinitionEntityElementInContainerComponent;
import com.nosliw.data.core.domain.entity.expression.HAPDefinitionExpression;
import com.nosliw.data.core.domain.entity.expression.HAPDefinitionExpressionGroup1;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;

//expressioin group resource
public class HAPResourceEntityExpressionGroup  extends HAPDefinitionEntityElementInContainerComponent implements HAPDefinitionExpressionGroup1{

	public HAPResourceEntityExpressionGroup() {}
	
	public HAPResourceEntityExpressionGroup(HAPResourceEntityExpressionSuite suite, String id) {
		super(suite, id);
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE;  }

	public HAPElementContainerResourceDefinitionEntityExpressionSuite getExpressionGroupEntity() {    return (HAPElementContainerResourceDefinitionEntityExpressionSuite)this.getComponentEntity();   }
	public HAPResourceEntityExpressionSuite getSuite() {   return (HAPResourceEntityExpressionSuite)this.getResourceContainer();  }

	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {		return this.getExpressionGroupEntity().getEntityElements();	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.getExpressionGroupEntity().getEntityElement(id);  }

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		HAPResourceEntityExpressionGroup out = new HAPResourceEntityExpressionGroup();
		this.cloneToComponentContainerElement(out);
		return out;
	}

	@Override
	public HAPDefinitionExpressionGroup1 cloneExpressionGroupDefinition() { return (HAPDefinitionExpressionGroup1)cloneComponent();  }

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

	@Override
	public String getValueStructureTypeIfNotDefined() {
		// TODO Auto-generated method stub
		return null;
	}

}
