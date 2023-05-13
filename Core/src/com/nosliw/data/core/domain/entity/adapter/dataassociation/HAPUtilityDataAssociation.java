package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping.HAPDefinitionDataAssociationMapping;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.mapping.HAPUtilityDataAssociationnMapping;

public class HAPUtilityDataAssociation {

	public static HAPDefinitionDataAssociation reverseMapping(HAPDefinitionDataAssociation dataAssociation) {
		HAPDefinitionDataAssociation out = null;
		String type = dataAssociation.getType();
		if(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING.equals(type)) {
			out = HAPUtilityDataAssociationnMapping.reverseMapping((HAPDefinitionDataAssociationMapping)dataAssociation);
		}
		return out;
	}


}
