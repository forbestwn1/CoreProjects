package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.literate.HAPLiterateType;
import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoAtomic extends HAPValueInfo{

	public static final String DEFAULTVALUE = "default";

	public static final String DATATYPE = "dataType";

	public static final String SUBDATATYPE = "subDataType";
	
	private HAPValueInfoAtomic(){}
	
	public static HAPValueInfoAtomic build(){
		HAPValueInfoAtomic out = new HAPValueInfoAtomic();
		out.init();
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_ATOMIC);
		this.updateAtomicChildStrValue(HAPValueInfoAtomic.DATATYPE, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING);
	}
	
	public String getDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.DATATYPE);	}
	public String getSubDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.SUBDATATYPE);	}
	public HAPLiterateType getLiterateType(){ return new HAPLiterateType(this.getDataType(), this.getSubDataType()); }
	
	@Override
	public HAPValueInfoAtomic clone(){
		HAPValueInfoAtomic out = new HAPValueInfoAtomic();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public HAPStringableValue newValue() {
		//Create new object only when default value is defined, otherwise, create empty one 
		HAPStringableValue out = null;
		String defaultValue = this.getAtomicAncestorValueString(HAPValueInfoAtomic.DEFAULTVALUE);
		out = new HAPStringableValueAtomic(defaultValue, this.getDataType(), this.getSubDataType());
		return out;
	}

	public HAPStringableValue newValue(String strValue) {
		return new HAPStringableValueAtomic(strValue, this.getDataType(), this.getSubDataType());
	}	
}
