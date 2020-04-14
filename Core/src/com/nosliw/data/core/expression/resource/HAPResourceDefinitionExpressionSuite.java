package com.nosliw.data.core.expression.resource;

import java.util.Map;

import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPResourceDefinitionExpressionSuite 
				extends HAPResourceDefinitionContainer<HAPDefinitionResourceDefinitionExpressionSuiteElementEntity> 
				implements HAPDefinitionExpressionSuite{

	public HAPResourceDefinitionExpressionSuite() {
	}

	@Override
	public Map<String, HAPDefinitionExpressionGroup> getEntityElements() {		return (Map)this.getContainerElements();	}

	@Override
	public HAPDefinitionExpressionGroup getEntityElement(String id) {  return this.getContainerElement(id); }

	@Override
	public void addEntityElement(HAPDefinitionExpressionGroup entityElement) {  this.addContainerElement((HAPDefinitionResourceDefinitionExpressionSuiteElementEntity)entityElement); }

	@Override
	public HAPResourceDefinition getElementResourceDefinition(String eleId) {  return new HAPResourceDefinitionExpressionGroup(this, eleId);  }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	public HAPResourceDefinitionContainer<HAPDefinitionResourceDefinitionExpressionSuiteElementEntity> cloneResourceDefinitionContainer() {
		HAPResourceDefinitionContainer<HAPDefinitionResourceDefinitionExpressionSuiteElementEntity> out = new HAPResourceDefinitionExpressionSuite();
		this.cloneToResourceDefinitionContainer(out);
		return out;
	}

	@Override
	public HAPDefinitionExpressionSuite cloneExpressionSuiteDefinition() {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();
		this.cloneToResourceDefinitionContainer(out);
		return out;
	}

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		// TODO Auto-generated method stub
		return null;
	}

}
