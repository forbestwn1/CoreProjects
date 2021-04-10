package com.nosliw.data.core.expression.resource;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPDefinitionExpressionSuite;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;

public class HAPResourceDefinitionExpressionSuite 
				extends HAPResourceDefinitionContainer<HAPElementContainerResourceDefinitionEntityExpressionSuite> 
				implements HAPDefinitionExpressionSuite{

	public HAPResourceDefinitionExpressionSuite() {
	}

	@Override
	public Set<HAPDefinitionExpressionGroup> getEntityElements() {		return (Set)this.getContainerElements();	}

	@Override
	public HAPDefinitionExpressionGroup getEntityElement(String id) {  return this.getContainerElement(id); }

	@Override
	public void addEntityElement(HAPDefinitionExpressionGroup entityElement) {  this.addContainerElement((HAPElementContainerResourceDefinitionEntityExpressionSuite)entityElement); }

	@Override
	public HAPResourceDefinition getElementResourceDefinition(String eleId) {  return new HAPResourceDefinitionExpressionGroup(this, eleId);  }

	@Override
	public HAPResourceDefinitionOrId getChild(String path) {   return new HAPResourceDefinitionExpressionGroup(this, path);    }

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
	public HAPResourceDefinitionContainer<HAPElementContainerResourceDefinitionEntityExpressionSuite> cloneResourceDefinitionContainer() {
		HAPResourceDefinitionContainer<HAPElementContainerResourceDefinitionEntityExpressionSuite> out = new HAPResourceDefinitionExpressionSuite();
		this.cloneToResourceDefinitionContainer(out);
		return out;
	}

	@Override
	public HAPDefinitionExpressionSuite cloneExpressionSuiteDefinition() {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();
		this.cloneToResourceDefinitionContainer(out);
		return out;
	}

}
