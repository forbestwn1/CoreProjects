package com.nosliw.data.core.domain.entity.expression.data;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

//normal expression group
public class HAPDefinitionEntityExpressionDataGroup extends HAPDefinitionEntityExpressionData implements HAPWithEntityElement<HAPDefinitionExpressionData>{

	public HAPDefinitionEntityExpressionDataGroup() {
		this.setAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpressionData>());
	}
	
	public void addExpression(HAPDefinitionExpressionData element) {	this.addEntityElement(element);	}

	@Override
	public List<HAPDefinitionExpressionData> getEntityElements() {		return this.getAllExpressionItems();	}

	@Override
	public HAPDefinitionExpressionData getEntityElement(String id) {
		for(HAPDefinitionExpressionData expression : this.getAllExpressionItems()) {
			if(id.equals(expression.getId())) {
				return expression;
			}
		}
		return null;
	}

	@Override
	public List<HAPDefinitionExpressionData> getAllExpressionItems(){	return (List<HAPDefinitionExpressionData>)this.getAttributeValue(ELEMENT);	}
	

	@Override
	public void addEntityElement(HAPDefinitionExpressionData expression) {  
		if(expression!=null) {
			HAPUtilityEntityInfo.processEntityId(expression);
			this.getAllExpressionItems().add(expression);  
		}
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionDataGroup out = new HAPDefinitionEntityExpressionDataGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
