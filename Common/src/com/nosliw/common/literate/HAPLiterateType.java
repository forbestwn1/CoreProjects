package com.nosliw.common.literate;

import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPLiterateType {

	private String m_type;
	private String m_subType;
	
	public HAPLiterateType(String type, String subType){
		this.m_type = type;
		this.m_subType = subType;
	}
	
	public String getType(){  return this.m_type; }
	public String getSubType(){  return this.m_subType;  }

	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPLiterateType){
			HAPLiterateType value = (HAPLiterateType)o;
			out = HAPUtilityBasic.isEquals(m_type, value.m_type) && HAPUtilityBasic.isEquals(m_subType, value.m_subType);
		}
		return out;
	}
	
	public HAPLiterateType clone(){
		HAPLiterateType out = new HAPLiterateType(this.m_type, this.m_subType);
		return out;
	}
	
}
