package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.data.core.domain.common.interactive.HAPExecutableEntityInteractive;
import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;

public class HAPExecutableEntityInDomainServiceProvider extends HAPExecutableEntityInteractive{

	public static final String ATTR_SERVICEID = "serviceId";

	public void setServiceId(String serviceId) {	this.setNormalAttributeObject(ATTR_SERVICEID, new HAPEmbededExecutable(serviceId));	}

	public String getServiceId() {	return (String)this.getNormalAttributeValue(ATTR_SERVICEID);	}
	
}
