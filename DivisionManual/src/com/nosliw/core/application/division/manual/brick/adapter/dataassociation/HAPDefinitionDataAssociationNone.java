package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;

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
