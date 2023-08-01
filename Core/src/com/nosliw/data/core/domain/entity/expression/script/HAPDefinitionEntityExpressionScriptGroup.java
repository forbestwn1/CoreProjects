package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;

public class HAPDefinitionEntityExpressionScriptGroup extends HAPDefinitionEntityComplexWithDataExpressionGroup implements HAPWithEntityElement<HAPDefinitionExpression>{
	
	public HAPDefinitionEntityExpressionScriptGroup() {
		this.setAttributeValueObject(ELEMENT, new ArrayList<HAPDefinitionExpression>());
	}

	@Override
	public List<HAPDefinitionExpression> getEntityElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionExpression getEntityElement(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEntityElement(HAPDefinitionExpression entityElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
