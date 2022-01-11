package com.nosliw.data.core.domain;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPDefinitionEntityInDomain extends HAPEntityInfo{

	String getEntityType();

	boolean isComplexEntity();
}
