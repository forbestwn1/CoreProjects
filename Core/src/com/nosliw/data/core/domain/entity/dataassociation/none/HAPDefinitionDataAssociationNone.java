package com.nosliw.data.core.domain.entity.dataassociation.none;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionDataAssociationNone extends HAPDefinitionDataAssociation{

	public HAPDefinitionDataAssociationNone() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_NONE);
	}

	@Override
	public HAPDefinitionDataAssociation cloneDataAssocation() {
		HAPDefinitionDataAssociationNone out = new HAPDefinitionDataAssociationNone();
		this.cloneToDataAssociation(out);
		return out;
	}
}
