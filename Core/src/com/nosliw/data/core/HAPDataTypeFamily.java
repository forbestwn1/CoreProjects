package com.nosliw.data.core;

import java.util.Set;

public interface HAPDataTypeFamily {

	HAPDataType getTargetDataType();

	HAPRelationship getRelationship(HAPDataTypeId dataTypeInfo);

	Set<? extends HAPRelationship> getRelationships();
}
