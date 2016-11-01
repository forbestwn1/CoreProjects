package com.nosliw.data.datatype.importer;

import java.util.Map;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueMap;
import com.nosliw.data.HAPDataOperationInfo;
import com.nosliw.data.HAPDataOperationOutInfo;
import com.nosliw.data.HAPDataOperationParmInfo;

public class HAPDataOperationInfoImp extends HAPStringableValueEntity implements HAPDataOperationInfo{

	@Override
	public String getName() {		return this.getBasicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getBasicAncestorValueString(DESCRIPTION);  }

	@Override
	public Map<String, HAPDataOperationParmInfo> getParmsInfo() {
		HAPStringableValueMap map = this.getMapAncestorByPath(PAMRS);
		Map<String, HAPDataOperationParmInfo> out = (Map<String, HAPDataOperationParmInfo>)map.getMapValue();
		return out;
	}

	@Override
	public HAPDataOperationOutInfo getOutputInfo() {		return (HAPDataOperationOutInfo)this.getEntityAncestorByPath(OUTPUT);	}

}
