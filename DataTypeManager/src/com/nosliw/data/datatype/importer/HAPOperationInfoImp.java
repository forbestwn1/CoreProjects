package com.nosliw.data.datatype.importer;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.HAPOperationInfo;
import com.nosliw.data.HAPOperationOutInfo;
import com.nosliw.data.HAPOperationParmInfo;

public class HAPOperationInfoImp extends HAPStringableValueEntity implements HAPOperationInfo{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String DATATYPID = "dataTypeId";
	
	public HAPOperationInfoImp(){}
	
	public HAPOperationInfoImp(String id, String name, String description){
		this.updateAtomicChildStrValue(ID, id);
		this.updateAtomicChildStrValue(NAME, name);
		this.updateAtomicChildStrValue(DESCRIPTION, description);
	}

	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	public String getId(){		return this.getAtomicAncestorValueString(ID);	}

	public HAPDataTypeIdImp getDataTypeId(){ return (HAPDataTypeIdImp)this.getAtomicAncestorValueObject(DATATYPID, HAPDataTypeIdImp.class); }
	
	@Override
	public Map<String, HAPOperationParmInfo> getParmsInfo() {
		HAPStringableValueMap map = this.getMapAncestorByPath(PAMRS);
		Map<String, HAPOperationParmInfo> out = (Map<String, HAPOperationParmInfo>)map.getMapValue();
		return out;
	}

	@Override
	public HAPOperationOutInfo getOutputInfo() {		return (HAPOperationOutInfo)this.getEntityAncestorByPath(OUTPUT);	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
