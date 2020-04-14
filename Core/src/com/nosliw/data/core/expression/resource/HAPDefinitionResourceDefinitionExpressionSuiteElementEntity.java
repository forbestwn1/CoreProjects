package com.nosliw.data.core.expression.resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElement;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;

public class HAPDefinitionResourceDefinitionExpressionSuiteElementEntity 
		extends HAPResourceDefinitionContainerElementEntityImpComponent 
		implements HAPDefinitionExpressionGroup{

	private Map<String, HAPDefinitionExpression> m_element;
	
	public HAPDefinitionResourceDefinitionExpressionSuiteElementEntity() {
		this.m_element = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	@Override
	public Map<String, HAPDefinitionExpression> getEntityElements() {  return this.m_element;  }

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.m_element.get(id); }

	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {
		//validate id
		HAPUtilityEntityInfo.processEntityId(expression);
		this.m_element.put(expression.getId(), expression); 
	}

	@Override
	public Set<HAPDefinitionConstant> getConstantDefinitions() {		return null;	}

	@Override
	public HAPDefinitionConstant getConstantDefinition(String name) {		return null;	}

	@Override
	public void addConstantDefinition(HAPDefinitionConstant constantDef) {	}

	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)this.cloneResourceDefinitionContainerElement(); }

	@Override
	public HAPResourceDefinitionContainerElement cloneResourceDefinitionContainerElement() {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out);
		for(String name : this.m_element.keySet()) {
			out.m_element.put(name, this.m_element.get(name).cloneDefinitionExpression());
		}
		return out;
	}

	@Override
	public HAPDefinitionExpressionGroup cloneExpressionGroupDefinition() {  return (HAPDefinitionExpressionGroup)this.cloneResourceDefinitionContainerElement(); }

}
