package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionAdapterDataAssociation extends HAPManualDefinitionBrick{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociation() {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.setAttributeValueWithValue(DEFINITION, new ArrayList<HAPDefinitionDataAssociation>());
	}
	
	public void addDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.getDataAssociation().add(dataAssciation);    }
	public List<HAPDefinitionDataAssociation> getDataAssociation() {   return (List<HAPDefinitionDataAssociation>)this.getAttributeValueOfValue(DEFINITION);     }

}
