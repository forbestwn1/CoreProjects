package com.nosliw.data.core.imp;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPOperationVarInfoImp extends HAPStringableValueEntityWithID implements HAPOperationParmInfo, HAPOperationOutInfo{

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TYPE = "type";
	
	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	@Override
	public HAPDataTypeCriteria getCriteria() {		return (HAPDataTypeCriteria)this.getAtomicValueAncestorByPath(CRITERIA);	}

	public String getDataTypeId() {		return this.getAtomicAncestorValueString(DATATYPEID);	}
	public String getOperationId() {		return this.getAtomicAncestorValueString(OPERATIONID);	}
	public String getType() {		return this.getAtomicAncestorValueString(TYPE);	}

	@Override
	public boolean getIsBase() { return this.getAtomicAncestorValueBoolean(ISBASE); }
}
