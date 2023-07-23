package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.HAPExecutableEntitySimple;

@HAPEntityWithAttribute
public class HAPExecutableEntityDataAssciation extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static final String ATTR_DATAASSOCIATION = "dataAssociation";

	public void setDataAssciation(HAPExecutableDataAssociation dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPExecutableDataAssociation getDataAssociation() {   return (HAPExecutableDataAssociation)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

}
