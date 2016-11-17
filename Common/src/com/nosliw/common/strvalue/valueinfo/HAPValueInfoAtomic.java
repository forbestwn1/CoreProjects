package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueAtomic;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoAtomic extends HAPValueInfo{

	public static final String DEFAULTVALUE = "default";

	public static final String DATATYPE = "dataType";

	public static final String DATASUBTYPE = "dataSubType";
	
	private HAPValueInfoAtomic(){}
	
	public static HAPValueInfoAtomic build(){
		HAPValueInfoAtomic out = new HAPValueInfoAtomic();
		out.init();
		return out;
	}

	@Override
	public String getValueInfoType(){	
		String out = super.getValueInfoType();
		if(out==null)  out = HAPConstant.STRINGALBE_VALUEINFO_ATOMIC;
		return out;
	}
	
	public String getDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.DATATYPE);	}
	public String getSubDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.DATASUBTYPE);	}
	
	
	@Override
	public HAPValueInfoAtomic clone(){
		HAPValueInfoAtomic out = new HAPValueInfoAtomic();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public HAPStringableValue buildDefault() {
		HAPStringableValue out = null;
		String defaultValue = this.getAtomicAncestorValueString(HAPValueInfoAtomic.DEFAULTVALUE);
		if(defaultValue!=null){
			out = new HAPStringableValueAtomic(defaultValue, this.getDataType(), this.getSubDataType());
		}
		return out;
	}
}
