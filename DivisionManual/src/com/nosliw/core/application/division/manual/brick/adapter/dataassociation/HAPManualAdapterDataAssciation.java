package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualAdapter;

public class HAPManualAdapterDataAssciation extends HAPManualAdapter{

	public static final String DATAASSOCIATION = "dataAssociation";

	protected HAPManualAdapterDataAssciation() {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.setAttributeWithValueValue(DATAASSOCIATION, new ArrayList<HAPDefinitionDataAssociation>());
	}
	
	public void addDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.getDataAssociation().add(dataAssciation);    }
	public List<HAPDefinitionDataAssociation> getDataAssociation() {   return (List<HAPDefinitionDataAssociation>)this.getAttributeValueWithValue(DATAASSOCIATION);     }

}
