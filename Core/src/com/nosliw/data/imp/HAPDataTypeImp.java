package com.nosliw.data.imp;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	return (HAPDataTypeInfo)this.getAncestorByPath(INFO);	}

	@Override
	public String getDescription() {	return this.getBasicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {	return (HAPDataTypeInfo)this.getAncestorByPath(PARENTINFO);	}

	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAncestorByPath(LINKEDVERSION);	}

}
