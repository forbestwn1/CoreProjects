package com.nosliw.common.strvalue.valueinfo;

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
		this.updateAtomicChild(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_ATOMIC);
		this.updateAtomicChild(HAPValueInfoAtomic.DATATYPE, HAPConstant.STRINGABLE_ATOMICVALUETYPE_STRING);
	}
	
	public String getDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.DATATYPE);	}
	public String getSubDataType(){	return this.getAtomicAncestorValueString(HAPValueInfoAtomic.SUBDATATYPE);	}
	
	
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
