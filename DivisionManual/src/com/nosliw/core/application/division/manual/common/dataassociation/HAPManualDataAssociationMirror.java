package com.nosliw.core.application.division.manual.common.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDataAssociationMirror extends HAPManualDataAssociation{

	public HAPManualDataAssociationMirror() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR);
	}
 
 	@Override
	public HAPManualDataAssociationMirror cloneDataAssocation() {
		HAPManualDataAssociationMirror out = new HAPManualDataAssociationMirror();
		this.cloneToEntityInfo(out);
		return out;
	}
	
}
