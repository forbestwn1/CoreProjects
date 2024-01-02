package com.nosliw.data.core.domain.entity.adapter.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.HAPExecutableEntitySimple;

@HAPEntityWithAttribute
public class HAPExecutableEntityDataAssociationTask extends HAPExecutableEntitySimple{

	@HAPAttribute
	public static final String ATTR_DATAASSOCIATION = "dataAssociation";

	public void setDataAssciation(HAPExecutableGroupDataAssociationForTask dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPExecutableGroupDataAssociationForTask getDataAssociation() {   return (HAPExecutableGroupDataAssociationForTask)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

}
