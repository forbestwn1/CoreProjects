package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.HAPStringableValue;
import com.nosliw.common.strvalue.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String TYPE = "type";

	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue buildDefault();
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	public String getValueInfoType(){	return this.getAtomicAncestorValueString(HAPValueInfo.TYPE);	}

	public HAPValueInfo getSolidValueInfo(){		return this;	}
	public String getSolidValueInfoType(){		return this.getValueInfoType();	}
	
	@Override
	public void init(){
		super.init();
	}

	protected HAPValueInfoManager getValueInfoManager(){ return HAPValueInfoManager.getInstance(); }
	
	protected void cloneFrom(HAPValueInfo valueInfo){
		super.cloneFrom(valueInfo);
	}
}
