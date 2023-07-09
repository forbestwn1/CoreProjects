package com.nosliw.data.core.domain.entity.expression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

//normal expression group
public class HAPDefinitionEntityExpressionGroup extends HAPDefinitionEntityExpression implements HAPWithEntityElement<HAPDefinitionExpression>{

	public HAPDefinitionEntityExpressionGroup() {
		this.setNormalAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpression>());
	}
	
	public void addExpression(HAPDefinitionExpression element) {	this.addEntityElement(element);	}

	@Override
	public List<HAPDefinitionExpression> getEntityElements() {		return this.getAllExpressionItems();	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {
		for(HAPDefinitionExpression expression : this.getAllExpressionItems()) {
			if(id.equals(expression.getId())) {
				return expression;
			}
		}
		return null;
	}

	@Override
	public List<HAPDefinitionExpression> getAllExpressionItems(){	return (List<HAPDefinitionExpression>)this.getNormalAttributeValue(ELEMENT);	}
	

	@Override
	public void addEntityElement(HAPDefinitionExpression expression) {  
		if(expression!=null) {
			HAPUtilityEntityInfo.processEntityId(expression);
			this.getAllExpressionItems().add(expression);  
		}
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionGroup out = new HAPDefinitionEntityExpressionGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
