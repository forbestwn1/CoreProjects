package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionDataAssociationMirror extends HAPDefinitionDataAssociation{

	public HAPDefinitionDataAssociationMirror() {
		super(HAPConstantShared.DATAASSOCIATION_TYPE_MIRROR);
	}
 
 	@Override
	public HAPDefinitionDataAssociationMirror cloneDataAssocation() {
		HAPDefinitionDataAssociationMirror out = new HAPDefinitionDataAssociationMirror();
		this.cloneToEntityInfo(out);
		return out;
	}
	
}
