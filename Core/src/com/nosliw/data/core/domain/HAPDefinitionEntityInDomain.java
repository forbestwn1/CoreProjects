package com.nosliw.data.core.domain;

import com.nosliw.common.info.HAPEntityInfo;

//entity definition used in domain
public interface HAPDefinitionEntityInDomain extends HAPEntityInfo{

	String getEntityType();

	boolean isComplexEntity();

	HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain();
}
