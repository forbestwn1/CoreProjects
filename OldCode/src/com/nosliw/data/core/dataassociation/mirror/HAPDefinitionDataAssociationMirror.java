package com.nosliw.data.core.dataassociation.mirror;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

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
