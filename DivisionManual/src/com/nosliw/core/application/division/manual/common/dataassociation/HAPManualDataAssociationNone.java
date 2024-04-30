package com.nosliw.core.application.division.manual.common.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDataAssociationNone extends HAPManualDataAssociation{

	public HAPManualDataAssociationNone() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_NONE);
	}

	@Override
	public HAPManualDataAssociation cloneDataAssocation() {
		HAPManualDataAssociationNone out = new HAPManualDataAssociationNone();
		this.cloneToDataAssociation(out);
		return out;
	}
}
