package com.nosliw.data.datatype.importer;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataOperationParmInfo;
import com.nosliw.data.HAPDataTypeInfo;

public class HAPOperationParmImp extends HAPStringableValueEntity implements HAPDataOperationParmInfo{

	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	@Override
	public HAPDataTypeInfo getDataTypeInfo() {		return (HAPDataTypeInfo)this.getObjectAncestorByPath(DATATYPE);	}

}
