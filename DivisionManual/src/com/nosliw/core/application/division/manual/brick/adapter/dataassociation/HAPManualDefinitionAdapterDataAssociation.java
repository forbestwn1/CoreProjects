package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;

public class HAPManualDefinitionAdapterDataAssociation extends HAPManualDefinitionBrick{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociation() {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.setAttributeValueWithValue(DEFINITION, new ArrayList<HAPManualDataAssociation>());
	}
	
	public void addDataAssciation(HAPManualDataAssociation dataAssciation) {    this.getDataAssociation().add(dataAssciation);    }
	public List<HAPManualDataAssociation> getDataAssociation() {   return (List<HAPManualDataAssociation>)this.getAttributeValueOfValue(DEFINITION);     }

}
