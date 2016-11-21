package com.nosliw.data.datatype.importer;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeInfo;
import com.nosliw.data.HAPDataTypeVersion;

@HAPEntityWithAttribute(parent="com.nosliw.data.HAPDataType")
public class HAPDataTypeImp extends HAPStringableValueEntity implements HAPDataType{

	@HAPAttribute
	public static String OPERATIONS = "operations";

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {	
		return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(NAME);
	}

	@Override
	public String getDescription() {	return this.getAtomicAncestorValueString(DESCRIPTION); }

	@Override
	public HAPDataTypeInfo getParentDataTypeInfo() {	return (HAPDataTypeInfo)this.getAtomicValueAncestorByPath(PARENTINFO);	}

	@Override
	public HAPDataTypeVersion getLinkedVersion() {  return (HAPDataTypeVersion)this.getAtomicValueAncestorByPath(LINKEDVERSION);	}

	public List<HAPOperationInfo> getDataOperationInfos(){
		HAPStringableValueList list = (HAPStringableValueList)this.getListAncestorByPath(OPERATIONS);
		return (List<HAPOperationInfo>)list.getListValue();
	}
}
