package com.nosliw.core.application.division.manual.common.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;

public class HAPManualDataAssociationNone extends HAPDefinitionDataAssociation{

	public HAPManualDataAssociationNone() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_NONE);
	}

	@Override
	public HAPDefinitionDataAssociation cloneDataAssocation() {
		HAPManualDataAssociationNone out = new HAPManualDataAssociationNone();
		this.cloneToDataAssociation(out);
		return out;
	}
}
