package com.nosliw.data.core.domain.entity.expression.data;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

//normal expression group
public class HAPDefinitionEntityExpressionDataGroup extends HAPDefinitionEntityExpressionData implements HAPWithEntityElement<HAPDefinitionExpressionData>{

	final private static String ATTR_IDINDEX = "idIndex"; 
	
	public HAPDefinitionEntityExpressionDataGroup() {
		this.setAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpressionData>());
	}
	
	public String addExpression(HAPDefinitionExpressionData expression) {
		if(expression!=null) {
			if(HAPUtilityBasic.isStringEmpty(expression.getId())){
				expression.setId(this.createId());
			}
			this.getAllExpressionItems().add(expression);
			return expression.getId();
		}
		return null;
	}

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

	private String createId() {
		int idIndex = (Integer)this.getAttributeValue(ATTR_IDINDEX, Integer.valueOf(0));
		idIndex++;
		this.setAttributeValueObject(ATTR_IDINDEX, idIndex);
		return "generatedId_"+ idIndex;
	}
	
	@Override
	public List<HAPDefinitionExpressionData> getAllExpressionItems(){	return (List<HAPDefinitionExpressionData>)this.getAttributeValue(ELEMENT);	}

	@Override
	public void addEntityElement(HAPDefinitionExpressionData expression) {
		this.addExpression(expression);
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionDataGroup out = new HAPDefinitionEntityExpressionDataGroup();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
