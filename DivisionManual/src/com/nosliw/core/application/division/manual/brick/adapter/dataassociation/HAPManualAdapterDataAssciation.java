package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;

public class HAPManualAdapterDataAssciation extends HAPManualAdapter{

	public static final String DATAASSOCIATION = "dataAssociation";

	public HAPManualAdapterDataAssciation() {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.setAttributeWithValueValue(DATAASSOCIATION, new ArrayList<HAPManualDataAssociation>());
	}
	
	public void addDataAssciation(HAPManualDataAssociation dataAssciation) {    this.getDataAssociation().add(dataAssciation);    }
	public List<HAPManualDataAssociation> getDataAssociation() {   return (List<HAPManualDataAssociation>)this.getAttributeValueWithValue(DATAASSOCIATION);     }

}
