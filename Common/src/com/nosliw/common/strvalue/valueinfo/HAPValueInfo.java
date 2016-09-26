package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String TYPE = "type";

	abstract public String getCategary();
	
	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue buildDefault();
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	public String getValueDataType(){
		String type = this.getBasicAncestorValueString(HAPValueInfo.TYPE);
		return type;
	}

	public HAPValueInfo getSolidValueInfo(){
		return this;
	}
	
	@Override
	public void init(){
		super.init();
		this.updateBasicChild(TYPE, HAPConstant.STRINGABLE_BASICVALUETYPE_STRING);
	}

	protected HAPValueInfoManager getValueInfoManager(){ return HAPValueInfoManager.getInstance(); }
	
	protected void cloneFrom(HAPValueInfo valueInfo){
		super.cloneFrom(valueInfo);
	}
}
