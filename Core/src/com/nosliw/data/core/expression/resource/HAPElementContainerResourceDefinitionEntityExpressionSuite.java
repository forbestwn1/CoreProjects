package com.nosliw.data.core.expression.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.complex.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPElementInContainerEntityDefinitionImpComponent;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;

//expression group within suite
public class HAPElementContainerResourceDefinitionEntityExpressionSuite 
		extends HAPElementInContainerEntityDefinitionImpComponent 
		implements HAPDefinitionExpressionGroup{

	private Map<String, HAPDefinitionExpression> m_element;
	
	public HAPElementContainerResourceDefinitionEntityExpressionSuite() {
		this.m_element = new LinkedHashMap<String, HAPDefinitionExpression>();
	}
	
	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {  return new HashSet<HAPDefinitionExpression>(this.m_element.values());  }

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
	public HAPDefinitionEntityComponent cloneComponent() {  return (HAPDefinitionEntityComponent)this.cloneDefinitionEntityElementInContainer(); }

	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
		HAPElementContainerResourceDefinitionEntityExpressionSuite out = new HAPElementContainerResourceDefinitionEntityExpressionSuite();
		this.cloneToResourceDefinitionContainerElementEntityImpComponent(out, true);
		for(String name : this.m_element.keySet()) {
			out.m_element.put(name, this.m_element.get(name).cloneDefinitionExpression());
		}
		return out;
	}

	@Override
	public HAPDefinitionExpressionGroup cloneExpressionGroupDefinition() {  return (HAPDefinitionExpressionGroup)this.cloneDefinitionEntityElementInContainer(); }

}
