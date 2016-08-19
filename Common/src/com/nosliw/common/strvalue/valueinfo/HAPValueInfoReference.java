package com.nosliw.common.strvalue.valueinfo;

import com.nosliw.common.utils.HAPConstant;

public class HAPValueInfoReference extends HAPValueInfo{

	public static final String ENTITY_PROPERTY_REFERENCE = "reference";
	
	private HAPValueInfo m_solidValueInfo;

	public static HAPValueInfoReference build(HAPValueInfoManager valueInfoMan){
		HAPValueInfoReference out = new HAPValueInfoReference();
		out.setValueInfoManager(valueInfoMan);
		out.init();
		return out;
	}

	@Override
	public void init(){
		super.init();
		this.updateBasicChild(ENTITY_PROPERTY_TYPE, HAPConstant.CONS_STRINGALBE_VALUEINFO_REFERENCE);
	}
	
	@Override
	public String getCategary() {
		return HAPConstant.CONS_STRINGALBE_VALUEINFO_REFERENCE;
	}
	
	@Override
	public String getValueDataType(){
		return this.getSolidValueInfo().getValueDataType();
	}
	
	@Override
	public HAPValueInfo getSolidValueInfo(){
		if(this.m_solidValueInfo==null){
			this.m_solidValueInfo = this.getValueInfoManager().getValueInfo(this.getReferencedName()).getSolidValueInfo();
		}
		return this.m_solidValueInfo;
	}
	
	private String getReferencedName(){
		return this.getBasicAncestorValueString(ENTITY_PROPERTY_REFERENCE);
	}

	@Override
	public HAPValueInfoReference clone(){
		HAPValueInfoReference out = new HAPValueInfoReference();
		out.cloneFrom(this);
		return out;
	}
}
