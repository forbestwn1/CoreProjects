package com.nosliw.data.datatype.loader;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.data.HAPDataOperationInfo;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@HAPAttribute
	public static String OPERATIONS = "operations";

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	return (HAPDataTypeInfo)this.getAncestorByPath(NAME);	}

	@Override
	public String getDescription() {	return this.getBasicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {	return (HAPDataTypeInfo)this.getAncestorByPath(PARENTINFO);	}

	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAncestorByPath(LINKEDVERSION);	}

	public List<HAPDataOperationInfo> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPDataOperationInfo>)list.getListValue();
	}
}
