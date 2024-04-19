package com.nosliw.core.application.brick.adapter.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;

@HAPEntityWithAttribute
public class HAPExecutableEntityDataAssciation extends HAPBrickAdapter{

	@HAPAttribute
	public static final String ATTR_DATAASSOCIATION = "dataAssociation";

	public void setDataAssciation(HAPExecutableDataAssociation dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPExecutableDataAssociation getDataAssociation() {   return (HAPExecutableDataAssociation)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

}
