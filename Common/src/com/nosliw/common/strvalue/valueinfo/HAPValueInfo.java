package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.strvalue.basic.HAPStringableValue;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntityBasic;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPValueInfo extends HAPStringableValueEntityBasic{

	public static final String ENTITY_PROPERTY_TYPE = "type";

	private HAPValueInfoManager m_valueInfoMan;
	
	abstract public String getCategary();
	
	abstract public HAPValueInfo clone();
	
	abstract public HAPStringableValue buildDefault();
	
	public HAPValueInfo getElement(String name){
		return null;
	}
	
	public String getValueDataType(){
		String type = this.getBasicAncestorValueString(HAPValueInfo.ENTITY_PROPERTY_TYPE);
		return type;
	}

	public HAPValueInfo getSolidValueInfo(){
		return this;
	}
	
	@Override
	public void init(){
		super.init();
		this.updateBasicChild(ENTITY_PROPERTY_TYPE, HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING);
	}

	protected HAPValueInfoManager getValueInfoManager(){ return this.m_valueInfoMan; }
	protected void setValueInfoManager(HAPValueInfoManager valueInfoMan){ this.m_valueInfoMan=valueInfoMan; }
	
	protected void cloneFrom(HAPValueInfo valueInfo){
		super.cloneFrom(valueInfo);
		this.setValueInfoManager(valueInfo.getValueInfoManager());
	}
}
