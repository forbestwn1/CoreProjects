package com.nosliw.core.data;

import java.util.Set;

public interface HAPDataTypeFamily {

	HAPDataType getTargetDataType();

	HAPRelationship getRelationship(HAPDataTypeId dataTypeInfo);

	Set<? extends HAPRelationship> getRelationships();
}
