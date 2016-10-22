package com.nosliw.data.datatype;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.HAPDataTypeVersion;

public class HAPDataTypeVersionImp extends HAPStringableValueEntity implements HAPDataTypeVersion{

	@Override
	public int getMajor() {		return this.getBasicAncestorValueInteger(MAJOR);	}

	@Override
	public int getMinor() {		return this.getBasicAncestorValueInteger(MINOR);	}

	@Override
	public String getRevision() {  return this.getBasicAncestorValueString(REVISION);   }

}
