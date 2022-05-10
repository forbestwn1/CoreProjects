package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

//id for entity globally (accross different complex resource)
public class HAPIdComplexEntityInGlobal {

	//root resource id
	private HAPResourceIdSimple m_rootResourceId;
	
	//entity id within resource domain
	private HAPIdEntityInDomain m_entityIdInDomain;
	
}
