package com.nosliw.core.application.brick.adapter.dataassociationfortask;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;

@HAPEntityWithAttribute
public class HAPAdapterDataAssociationForTask extends HAPBrickAdapter{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDataAssociationForTask dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	public HAPDataAssociationForTask getDataAssociation() {   return (HAPDataAssociationForTask)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}