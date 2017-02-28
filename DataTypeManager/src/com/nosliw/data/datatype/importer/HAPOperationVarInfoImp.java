package com.nosliw.data.datatype.importer;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPDataTypeCriteria;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;

public class HAPOperationVarInfoImp extends HAPStringableValueEntity implements HAPOperationParmInfo, HAPOperationOutInfo{

	@HAPAttribute
	public static String ID = "id";
	
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

	public String getId() {		return this.getAtomicAncestorValueString(ID);	}
	public String getDataTypeId() {		return this.getAtomicAncestorValueString(DATATYPEID);	}
	public String getOperationId() {		return this.getAtomicAncestorValueString(OPERATIONID);	}
	public String getType() {		return this.getAtomicAncestorValueString(TYPE);	}
	
}
