package com.nosliw.data.core.domain;

//a domain containing entity
//entity in domain can be found by entity id
public interface HAPDomainEntity {

	HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId);
	
}
