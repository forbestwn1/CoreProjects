package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;

public class HAPDefinitionEntityExpressionScriptGroup extends HAPDefinitionEntityComplexWithDataExpressionGroup implements HAPWithEntityElement<HAPDefinitionExpression>{
	
	final private static String ATTR_IDINDEX = "idIndex"; 
	
	public HAPDefinitionEntityExpressionScriptGroup() {
		this.setAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpression>());
	}

	public String addExpression(HAPDefinitionExpression expression) {
		if(expression!=null) {
			if(HAPUtilityBasic.isStringEmpty(expression.getId())){
				expression.setId(this.createId());
			}
			this.getEntityElements().add(expression);
			return expression.getId();
		}
		return null;
	}

	@Override
	public List<HAPDefinitionExpression> getEntityElements() { 	return (List<HAPDefinitionExpression>)this.getAttributeValue(ELEMENT);	}

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
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionScriptGroup out = new HAPDefinitionEntityExpressionScriptGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

	private String createId() {
		int idIndex = (Integer)this.getAttributeValue(ATTR_IDINDEX, Integer.valueOf(0));
		idIndex++;
		this.setAttributeValueObject(ATTR_IDINDEX, idIndex);
		return "generatedId_"+ idIndex;
	}
}
