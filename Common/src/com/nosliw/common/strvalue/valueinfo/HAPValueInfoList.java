package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueList;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPValueInfoList extends HAPValueInfoContainer{

	private HAPValueInfoList(){}
	
	public static HAPValueInfoList build(){
		HAPValueInfoList out = new HAPValueInfoList();
		out.init();
		return out;
	}
	
	@Override
	public void init(){
		super.init();
		this.updateAtomicChildStrValue(HAPValueInfo.TYPE, HAPConstantShared.STRINGALBE_VALUEINFO_LIST);
	}
	
	@Override
	public HAPValueInfoList clone(){
		HAPValueInfoList out = new HAPValueInfoList();
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public HAPStringableValue newValue() {
		return new HAPStringableValueList(this);
	}

}
