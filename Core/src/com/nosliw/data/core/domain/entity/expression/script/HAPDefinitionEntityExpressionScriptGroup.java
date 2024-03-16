package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.data.core.common.HAPWithEntityElement;

public class HAPDefinitionEntityExpressionScriptGroup extends HAPDefinitionEntityExpressionScript implements HAPWithEntityElement<HAPDefinitionExpression>{
	
	public HAPDefinitionEntityExpressionScriptGroup() {
		this.setAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpression>());
	}

	@Override
	public List<HAPDefinitionExpression> getAllExpressionItems() {   return (List<HAPDefinitionExpression>)this.getAttributeValue(ELEMENT);  }

	public String addExpression(HAPDefinitionExpression expression) {
		if(expression!=null) {
			if(HAPUtilityBasic.isStringEmpty(expression.getId())){
				expression.setId(this.generateId());
			}
			this.getEntityElements().add(expression);
			return expression.getId();
		}
		return null;
	}

	@Override
	public List<HAPDefinitionExpression> getEntityElements() { 	   return this.getAllExpressionItems(); 	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {
		for(HAPDefinitionExpression expression : this.getEntityElements()) {
			if(id.equals(expression.getId())) {
				return expression;
			}
		}
		return null;
	}

	@Override
	public void addEntityElement(HAPDefinitionExpression entityElement) {	this.addExpression(entityElement);	}

	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionScriptGroup out = new HAPDefinitionEntityExpressionScriptGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
