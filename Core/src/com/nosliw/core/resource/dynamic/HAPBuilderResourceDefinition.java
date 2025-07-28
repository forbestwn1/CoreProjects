package com.nosliw.core.resource.dynamic;

import java.util.Set;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPBuilderResourceDefinition extends HAPEntityInfo{

	@Override
	String getId();

	String getResourceType();
	
	HAPOutputBuilder build(Set<HAPParmDefinition> parms);

}
