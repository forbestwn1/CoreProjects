package com.nosliw.data.datatype.importer;

import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPRelationship;
import com.nosliw.data.HAPRelationshipPath;

public class HAPRelationshipImp extends HAPDataTypeImp implements HAPRelationship{

	private HAPDataTypeIdImp m_sourceDataType; 
	
	@Override
	public HAPDataType getTargetDataType() {
		return this;
	}

	@Override
	public HAPRelationshipPath getPath() {
		return null;
	}

	public HAPDataTypeIdImp getSource(){
		return this.m_sourceDataType;
	}
	
}
