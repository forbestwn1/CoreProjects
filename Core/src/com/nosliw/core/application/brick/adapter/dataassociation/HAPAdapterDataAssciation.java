package com.nosliw.core.application.brick.adapter.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPAdapter;

@HAPEntityWithAttribute
public class HAPAdapterDataAssciation extends HAPAdapter{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";

	public void setDataAssciation(HAPDataAssociation dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	public HAPDataAssociation getDataAssociation() {   return (HAPDataAssociation)this.getAttributeValueOfValue(DATAASSOCIATION);     }

}