package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.data.core.domain.entity.HAPEmbededExecutable;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;

public class HAPExecutableEntityInDomainServiceProvider extends HAPExecutableEntity{

	public static final String ATTR_SERVICEID = "serviceId";

	public static final String ATTR_SERVICEINTERFACE = "serviceInterface";
	
	public void setServiceId(String serviceId) {	this.setNormalAttributeObject(ATTR_SERVICEID, new HAPEmbededExecutable(serviceId));	}

	public String getServiceId() {	return (String)this.getNormalAttributeValue(ATTR_SERVICEID);	}
	
	public void setServiceInterface(HAPServiceInterface serviceInterface) {	this.setNormalAttributeObject(ATTR_SERVICEINTERFACE, new HAPEmbededExecutable(serviceInterface));	}

	public HAPServiceInterface getServiceInterface() {	return (HAPServiceInterface)this.getNormalAttributeValue(ATTR_SERVICEINTERFACE);	}



}
