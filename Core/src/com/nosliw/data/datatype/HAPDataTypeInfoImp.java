package com.nosliw.data.datatype;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeInfoImp extends HAPStringableValueEntity implements HAPDataTypeInfo{

	@Override
	public String getType() {		return this.getBasicAncestorValueString(TYPE);	}

	@Override
	public String getCategary() {		return this.getBasicAncestorValueString(CATEGARY);	}

	@Override
	public HAPDataTypeVersion getVersion() {	return (HAPDataTypeVersion)this.getEntityAncestorByPath(VERSION);	}

}
