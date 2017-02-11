package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.common.strvalue.HAPStringableValueObject;
import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoObject extends HAPValueInfo{

	public static final String CLASSNAME = "class";

	public static HAPValueInfoObject build(){
		HAPValueInfoObject out = new HAPValueInfoObject();
		out.init();
		return out;
	}
	
	@Override
	public void init(){
		super.init();
		this.updateAtomicChild(HAPValueInfo.TYPE, HAPConstant.STRINGALBE_VALUEINFO_OBJECT);
	}
	
	@Override
	public HAPValueInfoObject clone(){
		HAPValueInfoObject out = new HAPValueInfoObject();
		out.cloneFrom(this);
		return out;
	}

	public HAPStringableValueObject buildValue(String strValue){
		HAPStringableValueObject out = null;
		try{
			String className = this.getAtomicAncestorValueString(HAPValueInfoObject.CLASSNAME);
			if(className==null)    	className = HAPStringableValueEntity.class.getName();
			out = (HAPStringableValueObject)Class.forName(className).getConstructor(String.class).newInstance(strValue);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	@Override
	public HAPStringableValue newValue() {  return null;   }
}
