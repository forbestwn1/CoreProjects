package com.nosliw.data.datatype;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	return (HAPDataTypeInfo)this.getEntityAncestorByPath(INFO);	}

	@Override
	public String getDescription() {	return this.getBasicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {		return (HAPDataTypeInfo)this.getEntityAncestorByPath(PARENTINFO);	}

	@Override
	public HAPDataTypeInfo getLinkedDataTypeInfo() {	return (HAPDataTypeInfo)this.getEntityAncestorByPath(LINKEDINFO);	}

}
