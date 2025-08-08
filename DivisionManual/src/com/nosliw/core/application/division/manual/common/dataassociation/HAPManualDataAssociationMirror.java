package com.nosliw.core.application.division.manual.common.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;

public class HAPManualDataAssociationMirror extends HAPDefinitionDataAssociation{

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
