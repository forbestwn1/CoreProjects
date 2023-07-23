package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.data.core.domain.common.interactive.HAPExecutableEntityInteractive;

public class HAPExecutableEntityInDomainServiceProvider extends HAPExecutableEntityInteractive{

	public static final String ATTR_SERVICEID = "serviceId";

	public void setServiceId(String serviceId) {	this.setAttributeValueObject(ATTR_SERVICEID, serviceId);	}

	public String getServiceId() {	return (String)this.getAttributeValue(ATTR_SERVICEID);	}
	
}
