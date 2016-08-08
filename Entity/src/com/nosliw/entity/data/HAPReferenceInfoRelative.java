package com.nosliw.entity.data;

import com.nosliw.common.utils.HAPConstant;

public class HAPReferenceInfoRelative extends HAPReferenceInfo{
	private String m_path;

	public HAPReferenceInfoRelative(String path){
		this.m_path = path;
	}
	
	public String getPath(){  return this.m_path;  }
	
	@Override
	public String toStringValue(String format) {
	}

	@Override
	public int getType() {	return HAPConstant.CONS_REFERENCE_TYPE_RELATIVE;	}
}
