package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;

public class HAPManualAdapterDataAssociation extends HAPManualBrickAdapter{

	public static final String DEFINITION = "definition";

	public HAPManualAdapterDataAssociation() {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.setAttributeWithValueValue(DEFINITION, new ArrayList<HAPManualDataAssociation>());
	}
	
	public void addDataAssciation(HAPManualDataAssociation dataAssciation) {    this.getDataAssociation().add(dataAssciation);    }
	public List<HAPManualDataAssociation> getDataAssociation() {   return (List<HAPManualDataAssociation>)this.getAttributeValueWithValue(DEFINITION);     }

}
