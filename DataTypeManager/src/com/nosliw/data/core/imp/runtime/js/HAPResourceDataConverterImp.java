package com.nosliw.data.core.imp.runtime.js;

import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.js.HAPResourceDataConverter;

public class HAPResourceDataConverterImp extends HAPStringableValueEntityWithID implements HAPResourceDataConverter{

	public static String _VALUEINFO_NAME;
	
	@Override
	public HAPDataTypeId getDataTypeName() {	return (HAPDataTypeId)this.getAtomicAncestorValueObject(DATATYPENAME, HAPDataTypeId.class);	}
	public void setDataTypeName(HAPDataTypeId dataTypeName){ this.updateAtomicChildObjectValue(DATATYPENAME, dataTypeName); }

	@Override
	public String getConverterType() {  return this.getAtomicAncestorValueString(CONVERTERTYPE); }
	public void setConverterType(String type){  this.updateAtomicChildStrValue(CONVERTERTYPE, type);  }

	@Override
	public String getValue(){  return this.getAtomicAncestorValueString(VALUE);  }
	public void setScript(String script){  this.updateAtomicChildStrValue(VALUE, script);  }
}
