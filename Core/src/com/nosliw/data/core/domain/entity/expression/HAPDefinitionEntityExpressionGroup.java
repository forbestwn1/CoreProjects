package com.nosliw.data.core.domain.entity.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

//normal expression group
public class HAPDefinitionEntityExpressionGroup extends HAPDefinitionEntityInDomainComplex implements HAPWithEntityElement<HAPDefinitionExpression>{

	private Map<String, HAPDefinitionConstant> m_constantDefinitions;
	
	public HAPDefinitionEntityExpressionGroup() {
		this.setNormalAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpression>());
		this.m_constantDefinitions = new LinkedHashMap<String, HAPDefinitionConstant>();
	}
	
	public void addExpression(HAPDefinitionExpression element) {	this.addEntityElement(element);	}

	@Override
	public List<HAPDefinitionExpression> getEntityElements() {		return this.getAllExpressions();	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {
		for(HAPDefinitionExpression expression : this.getAllExpressions()) {
			if(id.equals(expression.getId())) {
				return expression;
			}
		}
		return null;
	}

	private List<HAPDefinitionExpression> getAllExpressions(){	return (List<HAPDefinitionExpression>)this.getNormalAttributeValue(ELEMENT);	}
	
	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {  
		HAPUtilityEntityInfo.processEntityId(expression);
		this.getAllExpressions().add(expression);  
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionGroup out = new HAPDefinitionEntityExpressionGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
