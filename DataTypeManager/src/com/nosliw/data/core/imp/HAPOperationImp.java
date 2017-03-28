package com.nosliw.data.core.imp;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPInfo;
import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;

public class HAPOperationImp extends HAPStringableValueEntityWithID implements HAPOperation{

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String TYPE = "type";
	
	@HAPAttribute
	public static String DATATYPNAME = "dataTypeName";
	
	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public HAPInfo getInfo() {		return (HAPInfo)this.getAtomicAncestorValueObject(INFO, HAPInfo.class);  }

	public HAPDataTypeId getDataTypeName(){ return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPNAME, HAPDataTypeId.class); }
	public void setDataTypeName(HAPDataTypeId dataTypeId){  this.updateAtomicChildObjectValue(DATATYPNAME, dataTypeId);  }
	
	@Override
	public Map<String, HAPOperationParmInfo> getParmsInfo() {
		HAPStringableValueMap map = this.getMapAncestorByPath(PAMRS);
		Map<String, HAPOperationParmInfo> out = (Map<String, HAPOperationParmInfo>)map.getMapValue();
		return out;
	}

	@Override
	public HAPOperationOutInfo getOutputInfo() {		return (HAPOperationOutInfo)this.getEntityAncestorByPath(OUTPUT);	}

	@Override
	public String getType() {  return this.getAtomicAncestorValueString(TYPE); }
}
