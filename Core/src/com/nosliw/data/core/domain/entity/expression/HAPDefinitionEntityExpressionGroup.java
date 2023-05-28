package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

//normal expression group
public class HAPDefinitionEntityExpressionGroup extends HAPDefinitionEntityInDomainComplex implements HAPWithEntityElement<HAPDefinitionExpression>{

//	private Map<String, HAPDefinitionExpression> m_elements;
	
	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionEntityExpressionGroup() {
		this.setNormalAttributeValueObject(ELEMENT, new LinkedHashMap<String, Object>());
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public void addExpression(HAPDefinitionExpression element) {
		this.addEntityElement(element);
	}

	@Override
	public Set<HAPDefinitionExpression> getEntityElements() {  return new HashSet<HAPDefinitionExpression>(this.getAllExpressions().values()); }

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {  return this.getAllExpressions().get(id);  }

	private Map<String, HAPDefinitionExpression> getAllExpressions(){	return (Map<String, HAPDefinitionExpression>)this.getNormalAttributeValue(ELEMENT);	}
	
	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {  
		HAPUtilityEntityInfo.processEntityId(expression);
		this.getAllExpressions().put(expression.getId(), expression);  
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionGroup out = new HAPDefinitionEntityExpressionGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
